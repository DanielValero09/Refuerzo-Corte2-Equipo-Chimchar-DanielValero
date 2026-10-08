package infernape.api;

import infernape.application.IniciadorMision;
import infernape.domain.*;
import infernape.infrastructure.persistence.MisionesJpaRepository;
import java.util.*;
import javax.sql.DataSource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes=EnterpriseApi.class, properties={
    "spring.datasource.url=jdbc:h2:mem:enterprise-integration;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false"})
class PersistenciaMisionIntegrationTest {
    @Autowired IniciadorMision servicio;
    @Autowired RepositorioMisionesEnterprise repositorio;
    @Autowired MisionesJpaRepository tabla;
    @Autowired JdbcTemplate jdbc;
    @Autowired DataSource datasource;
    @MockitoBean ServicioClima clima;
    @MockitoBean ServicioAerocivil aerocivil;
    @MockitoBean RepositorioFlota flota;
    @MockitoBean RepositorioEstadoSede sedes;
    @MockitoBean ObservadorAsignacion observador;
    @BeforeEach void aislar() {
        tabla.deleteAll();
        when(sedes.activa(any())).thenReturn(true);
        when(clima.condicionesAptas(any(), any())).thenReturn(true);
        when(aerocivil.verificar(any(), any(), any())).thenReturn(Optional.of(new CondicionesEspacioAereo(true, 120, Optional.empty())));
        when(flota.findDisponibles(Sede.ECI)).thenReturn(List.of(new DroneEnterprise("DE-C", Sede.ECI, PerfilDrone.CARGO, 80, true)));
    }
    private SolicitudInicioMision solicitud() {
        return new SolicitudInicioMision(new SolicitudAsignacion("ME-H2", Sede.ECI, Sede.UNAL, 1500, Prioridad.NORMAL), 6, 100);
    }
    @Test void insercionYRecuperacionSeRealizanEnH2() throws Exception {
        // Arrange
        var inicio = solicitud();
        // Act
        var creada = servicio.iniciar(inicio);
        var recuperada = repositorio.buscarPorId("ME-H2").orElseThrow();
        // Assert
        assertEquals(creada, recuperada);
        assertEquals(1, jdbc.queryForObject("select count(*) from enterprise_misiones", Integer.class));
        try (var conexion = datasource.getConnection()) {
            assertEquals("H2", conexion.getMetaData().getDatabaseProductName());
            assertTrue(conexion.getMetaData().getURL().startsWith("jdbc:h2:mem:"));
        }
        verify(observador, times(1)).onAsignada(any(), any());
    }
    @Test void rechazoNoInsertaUnaMisionExitosa() {
        // Arrange
        when(clima.condicionesAptas(any(), any())).thenReturn(false);
        // Act
        var error = assertThrows(ReglaOperacionException.class, () -> servicio.iniciar(solicitud()));
        // Assert
        assertEquals(CodigoRechazo.CLIMA_ADVERSO, error.codigo());
        assertEquals(0, jdbc.queryForObject("select count(*) from enterprise_misiones", Integer.class));
        assertTrue(repositorio.buscarPorId("ME-H2").isEmpty());
        verify(observador, never()).onAsignada(any(), any());
    }
    @Test void cadaPruebaComienzaSinRegistrosPrevios() {
        // Arrange
        var inicio = solicitud();
        // Act
        var vacia = repositorio.buscarPorId(inicio.asignacion().id());
        // Assert
        assertTrue(vacia.isEmpty());
        assertEquals(0, tabla.count());
    }
    @Test void perfilYPesoSeConservanEnElMapeoPersistido() {
        // Arrange
        var inicio = solicitud();
        // Act
        servicio.iniciar(inicio);
        var resultado = repositorio.buscarPorId("ME-H2").orElseThrow();
        // Assert
        assertEquals(1500, resultado.pesoPaquete());
        assertEquals(PerfilDrone.CARGO, resultado.drone().perfil());
        assertFalse(resultado.drone().disponible());
        assertEquals(Sede.UNAL, resultado.destino());
    }
}
