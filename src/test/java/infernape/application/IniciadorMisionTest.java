package infernape.application;

import infernape.domain.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IniciadorMisionTest {
    @Mock AsignadorMisionEnterprise asignador;
    @Mock AutorizadorRutaInterSede autorizador;
    @Mock ServicioClima clima;
    @Mock RepositorioEstadoSede sedes;
    @Mock RepositorioMisionesEnterprise misiones;
    IniciadorMision servicio;
    SolicitudInicioMision solicitud;

    @BeforeEach void preparar() {
        var config = new ConfiguracionOperacionSede(Sede.ECI, 10, new LimitesOperacionRegulada(10, 120));
        servicio = new IniciadorMision(asignador, autorizador, clima, sedes, misiones, Map.of(Sede.ECI, config));
        solicitud = solicitud(300, Prioridad.NORMAL);
    }
    private SolicitudInicioMision solicitud(int peso, Prioridad prioridad) {
        return new SolicitudInicioMision(new SolicitudAsignacion("ME-100", Sede.ECI, Sede.UNAL, peso, prioridad), 6, 100);
    }
    private void habilitar() {
        when(misiones.buscarPorId("ME-100")).thenReturn(Optional.empty());
        when(sedes.activa(Sede.ECI)).thenReturn(true);
        when(sedes.activa(Sede.UNAL)).thenReturn(true);
        when(clima.condicionesAptas(Sede.ECI, Sede.UNAL)).thenReturn(true);
    }
    @Test void seleccionValidaSeGuardaConElDroneReal() {
        // Arrange
        habilitar();
        when(autorizador.autorizar(any(), any(), eq(6.0), eq(100))).thenReturn(true);
        var drone = new DroneEnterprise("DE-01", Sede.ECI, PerfilDrone.MINI, 91, false);
        when(asignador.asignar(solicitud.asignacion())).thenReturn(Optional.of(drone));
        // Act
        var resultado = servicio.iniciar(solicitud);
        // Assert
        assertEquals(drone, resultado.drone());
        assertEquals(EstadoMision.EN_VUELO, resultado.estado());
        verify(misiones, times(1)).guardar(resultado);
        verify(asignador, times(1)).asignar(solicitud.asignacion());
    }
    @Test void climaAdversoNoSeleccionaNiGuarda() {
        // Arrange
        when(misiones.buscarPorId(anyString())).thenReturn(Optional.empty());
        when(sedes.activa(any())).thenReturn(true);
        when(clima.condicionesAptas(any(), any())).thenReturn(false);
        // Act
        var error = assertThrows(ReglaOperacionException.class, () -> servicio.iniciar(solicitud));
        // Assert
        assertEquals(CodigoRechazo.CLIMA_ADVERSO, error.codigo());
        verify(asignador, never()).asignar(any());
        verify(misiones, never()).guardar(any());
    }
    @Test void sinCandidatosNoPersiste() {
        // Arrange
        habilitar();
        when(autorizador.autorizar(any(), any(), anyDouble(), anyInt())).thenReturn(true);
        when(asignador.asignar(any())).thenReturn(Optional.empty());
        // Act
        var error = assertThrows(ReglaOperacionException.class, () -> servicio.iniciar(solicitud));
        // Assert
        assertEquals(CodigoRechazo.NO_DRONES_APTOS, error.codigo());
        verify(misiones, never()).guardar(any());
    }
    @Test void paqueteExcesivoSeRechazaAntesDeConsultarServicios() {
        // Arrange
        var pesada = solicitud(2001, Prioridad.NORMAL);
        // Act
        var error = assertThrows(ReglaOperacionException.class, () -> servicio.iniciar(pesada));
        // Assert
        assertEquals(CodigoRechazo.PESO_EXCESIVO, error.codigo());
        verifyNoInteractions(asignador, autorizador, clima, sedes, misiones);
    }
    @Test void aerocivilRechazaAntesDeAsignar() {
        // Arrange
        habilitar();
        when(autorizador.autorizar(any(), any(), anyDouble(), anyInt())).thenReturn(false);
        // Act
        var error = assertThrows(ReglaOperacionException.class, () -> servicio.iniciar(solicitud));
        // Assert
        assertEquals(CodigoRechazo.AEROCIVIL_RECHAZA, error.codigo());
        verify(asignador, never()).asignar(any());
        verify(misiones, never()).guardar(any());
    }
    @Test void sedeInactivaNoConsultaClimaNiAsigna() {
        // Arrange
        when(misiones.buscarPorId(anyString())).thenReturn(Optional.empty());
        when(sedes.activa(Sede.ECI)).thenReturn(false);
        // Act
        var error = assertThrows(ReglaOperacionException.class, () -> servicio.iniciar(solicitud));
        // Assert
        assertEquals(CodigoRechazo.SEDE_INACTIVA, error.codigo());
        verifyNoInteractions(clima, autorizador, asignador);
        verify(misiones, never()).guardar(any());
    }
    @Test void prioridadSeConservaAlDelegarLaSeleccion() {
        // Arrange
        habilitar();
        var urgente = solicitud(300, Prioridad.URGENTE);
        var express = new DroneEnterprise("DE-EXP", Sede.ECI, PerfilDrone.EXPRESS, 70, false);
        when(autorizador.autorizar(any(), any(), anyDouble(), anyInt())).thenReturn(true);
        when(asignador.asignar(urgente.asignacion())).thenReturn(Optional.of(express));
        // Act
        var resultado = servicio.iniciar(urgente);
        // Assert
        assertEquals(Prioridad.URGENTE, resultado.prioridad());
        assertEquals(PerfilDrone.EXPRESS, resultado.drone().perfil());
        verify(asignador).asignar(urgente.asignacion());
    }
    @Test void configuracionAusenteBloqueaConCausaExplicita() {
        // Arrange
        habilitar();
        servicio = new IniciadorMision(asignador, autorizador, clima, sedes, misiones, Map.of());
        // Act
        var error = assertThrows(ReglaOperacionException.class, () -> servicio.iniciar(solicitud));
        // Assert
        assertEquals(CodigoRechazo.CONFIGURACION_NO_DISPONIBLE, error.codigo());
        verifyNoInteractions(asignador, autorizador);
    }
    @Test void idDuplicadoNoVuelveAAsignar() {
        // Arrange
        var drone = new DroneEnterprise("DE-01", Sede.ECI, PerfilDrone.MINI, 91, false);
        var existente = new MisionRegistrada("ME-100", Sede.ECI, Sede.UNAL, 300, Prioridad.NORMAL, drone, EstadoMision.EN_VUELO);
        when(misiones.buscarPorId("ME-100")).thenReturn(Optional.of(existente));
        // Act
        var error = assertThrows(ReglaOperacionException.class, () -> servicio.iniciar(solicitud));
        // Assert
        assertEquals(CodigoRechazo.MISION_DUPLICADA, error.codigo());
        verifyNoInteractions(clima, autorizador, asignador, sedes);
    }
}
