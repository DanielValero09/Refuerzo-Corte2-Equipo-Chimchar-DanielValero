package infernape.api;

import infernape.domain.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = EnterpriseApi.class, properties = {
    "spring.datasource.url=jdbc:h2:mem:enterprise-notificacion;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false"})
@AutoConfigureMockMvc
class NotificacionPersistenciaTest {
    @Autowired MockMvc mvc;
    @MockitoBean ServicioClima clima;
    @MockitoBean ServicioAerocivil aerocivil;
    @MockitoBean RepositorioFlota flota;
    @MockitoBean RepositorioEstadoSede sedes;
    @MockitoBean RepositorioMisionesEnterprise misiones;
    @MockitoBean ObservadorAsignacion observador;

    @BeforeEach void preparar() {
        when(sedes.activa(any())).thenReturn(true);
        when(clima.condicionesAptas(any(), any())).thenReturn(true);
        when(aerocivil.verificar(any(), any(), any())).thenReturn(Optional.of(new CondicionesEspacioAereo(true, 120, Optional.empty())));
        when(flota.findDisponibles(Sede.ECI)).thenReturn(List.of(new DroneEnterprise("DE-01", Sede.ECI, PerfilDrone.MINI, 95, true)));
        when(misiones.buscarPorId(any())).thenReturn(Optional.empty());
    }
    private String solicitud() {
        return """
            {"id":"ME-PERSISTENCIA","origen":"ECI","destino":"UNAL","pesoPaquete":300,
             "prioridad":"NORMAL","distanciaPlanificadaKm":6,"alturaMetros":100}
            """;
    }
    @Test void falloAlGuardarNoNotificaInicioDeVuelo() throws Exception {
        // Arrange
        doThrow(new DataAccessResourceFailureException("fallo de almacenamiento simulado")).when(misiones).guardar(any());
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(solicitud()));
        // Assert
        respuesta.andExpect(status().isServiceUnavailable()).andExpect(jsonPath("$.codigo").value("DEPENDENCIA_NO_DISPONIBLE"));
        verify(misiones, times(1)).guardar(any());
        verify(observador, never()).onAsignada(any(), any());
    }
    @Test void exitoNotificaUnaVezDespuesDeGuardar() throws Exception {
        // Arrange
        var orden = inOrder(misiones, observador);
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(solicitud()));
        // Assert
        respuesta.andExpect(status().isCreated()).andExpect(jsonPath("$.droneAsignado.id").value("DE-01"));
        orden.verify(misiones).guardar(argThat(m -> m.estado() == EstadoMision.EN_VUELO));
        orden.verify(observador).onAsignada(any(), argThat(d -> !d.disponible() && d.id().equals("DE-01")));
        verify(observador, times(1)).onAsignada(any(), any());
    }
}
