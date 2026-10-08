package infernape.api;

import infernape.domain.*;
import infernape.infrastructure.persistence.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EnterpriseApi.class, properties = {
    "spring.datasource.url=jdbc:h2:mem:enterprise-endpoint;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false"})
@AutoConfigureMockMvc
class MisionesEndpointTest {
    @Autowired MockMvc mvc;
    @Autowired MisionesJpaRepository tabla;
    @MockitoBean ServicioClima clima;
    @MockitoBean ServicioAerocivil aerocivil;
    @MockitoBean RepositorioFlota flota;
    @MockitoBean RepositorioEstadoSede sedes;
    @MockitoBean ObservadorAsignacion observador;

    @BeforeEach void preparar() {
        tabla.deleteAll();
        when(sedes.activa(any())).thenReturn(true);
        when(clima.condicionesAptas(any(), any())).thenReturn(true);
        when(aerocivil.verificar(any(), any(), any())).thenReturn(Optional.of(new CondicionesEspacioAereo(true, 120, Optional.empty())));
        when(flota.findDisponibles(Sede.ECI)).thenReturn(List.of(
            new DroneEnterprise("DE-01", Sede.ECI, PerfilDrone.MINI, 95, true),
            new DroneEnterprise("DE-02", Sede.ECI, PerfilDrone.EXPRESS, 70, true)));
    }
    private String json(int peso, String prioridad) {
        return "{\"id\":\"ME-100\",\"origen\":\"ECI\",\"destino\":\"UNAL\",\"pesoPaquete\":"+peso+
            ",\"prioridad\":\""+prioridad+"\",\"distanciaPlanificadaKm\":6,\"alturaMetros\":100}";
    }
    @Test void exitoRecorreRestNegocioYH2() throws Exception {
        // Arrange
        String body = json(300, "NORMAL");
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(body));
        // Assert
        respuesta.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value("ME-100"))
            .andExpect(jsonPath("$.estado").value("EN_VUELO")).andExpect(jsonPath("$.droneAsignado.id").value("DE-01"));
        assertTrue(tabla.findById("ME-100").isPresent());
        verify(observador, times(1)).onAsignada(any(), argThat(d -> d.id().equals("DE-01") && !d.disponible()));
    }
    @Test void climaAdversoDevuelve422SinRegistro() throws Exception {
        // Arrange
        when(clima.condicionesAptas(any(), any())).thenReturn(false);
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(json(300, "NORMAL")));
        // Assert
        respuesta.andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.codigo").value("CLIMA_ADVERSO"));
        assertEquals(0, tabla.count());
        verify(observador, never()).onAsignada(any(), any());
    }
    @Test void sinDronesDevuelve422SinRegistro() throws Exception {
        // Arrange
        when(flota.findDisponibles(any())).thenReturn(List.of());
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(json(300, "NORMAL")));
        // Assert
        respuesta.andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.codigo").value("NO_DRONES_APTOS"));
        assertEquals(0, tabla.count());
        verify(observador, never()).onAsignada(any(), any());
    }
    @Test void paquetePesadoDevuelve422SinRegistro() throws Exception {
        // Arrange
        String body = json(2001, "NORMAL");
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(body));
        // Assert
        respuesta.andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.codigo").value("PESO_EXCESIVO"));
        assertEquals(0, tabla.count());
        verify(observador, never()).onAsignada(any(), any());
    }
    @Test void aerocivilRechazaDevuelve422SinRegistro() throws Exception {
        // Arrange
        when(aerocivil.verificar(any(), any(), any())).thenReturn(Optional.of(new CondicionesEspacioAereo(false, 0, Optional.of("Restriccion simulada"))));
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(json(300, "NORMAL")));
        // Assert
        respuesta.andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.codigo").value("AEROCIVIL_RECHAZA"));
        assertEquals(0, tabla.count());
        verify(observador, never()).onAsignada(any(), any());
    }
    @Test void sedeInactivaDevuelve422SinRegistro() throws Exception {
        // Arrange
        when(sedes.activa(Sede.UNAL)).thenReturn(false);
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(json(300, "NORMAL")));
        // Assert
        respuesta.andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.codigo").value("SEDE_INACTIVA"));
        assertEquals(0, tabla.count());
        verify(observador, never()).onAsignada(any(), any());
    }
    @Test void urgentePrefiereExpressCompatible() throws Exception {
        // Arrange
        String body = json(300, "URGENTE");
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(body));
        // Assert
        respuesta.andExpect(status().isCreated()).andExpect(jsonPath("$.droneAsignado.id").value("DE-02"));
        assertEquals("DE-02", tabla.findById("ME-100").orElseThrow().aDominio().drone().id());
    }
    @Test void jsonMalformadoEs400SinDetallesInternos() throws Exception {
        // Arrange
        String body = "{invalido";
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(body));
        // Assert
        respuesta.andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("DATOS_INVALIDOS"))
            .andExpect(jsonPath("$.stackTrace").doesNotExist());
        assertEquals(0, tabla.count());
    }
    @Test void pesoCeroEs400() throws Exception {
        // Arrange
        String body = json(0, "NORMAL");
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(body));
        // Assert
        respuesta.andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("DATOS_INVALIDOS"));
        assertEquals(0, tabla.count());
    }
    @Test void idDuplicadoEs409SinSegundaNotificacion() throws Exception {
        // Arrange
        String body = json(300, "NORMAL");
        mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated());
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(body));
        // Assert
        respuesta.andExpect(status().isConflict()).andExpect(jsonPath("$.codigo").value("MISION_DUPLICADA"));
        assertEquals(1, tabla.count());
        verify(observador, times(1)).onAsignada(any(), any());
    }
    @Test void alturaSuperiorAAutorizadaBloquea() throws Exception {
        // Arrange
        when(aerocivil.verificar(any(), any(), any())).thenReturn(Optional.of(new CondicionesEspacioAereo(true, 80, Optional.empty())));
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(json(300, "NORMAL")));
        // Assert
        respuesta.andExpect(status().isUnprocessableEntity());
        assertEquals(0, tabla.count());
    }
    @Test void autorizacionNoVerificableBloquea() throws Exception {
        // Arrange
        when(aerocivil.verificar(any(), any(), any())).thenReturn(Optional.empty());
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(json(300, "NORMAL")));
        // Assert
        respuesta.andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.codigo").value("AEROCIVIL_RECHAZA"));
        assertEquals(0, tabla.count());
    }
    @Test void dependenciaSimuladaIndisponibleEs503SinFiltrarDetalles() throws Exception {
        // Arrange
        when(aerocivil.verificar(any(), any(), any())).thenThrow(new IllegalStateException("detalle interno simulado"));
        // Act
        var respuesta = mvc.perform(post("/api/v3/misiones").contentType(MediaType.APPLICATION_JSON).content(json(300, "NORMAL")));
        // Assert
        respuesta.andExpect(status().isServiceUnavailable()).andExpect(jsonPath("$.codigo").value("DEPENDENCIA_NO_DISPONIBLE"))
            .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("detalle interno"))));
        assertEquals(0, tabla.count());
        verify(observador, never()).onAsignada(any(), any());
    }
}
