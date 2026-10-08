package infernape.application;

import java.util.List;
import java.util.Optional;
import infernape.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static infernape.support.DatosEnterprise.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsignadorMisionEnterpriseTest {
    @Mock RepositorioFlota repo;
    @Mock ServicioClima clima;
    @Mock EstrategiaAsignacionEnterprise estrategia;
    @Mock ObservadorAsignacion observador;

    @Test
    void asignaSinHTTPNiBDConsultandoLaSedeYRespetandoLaEstrategiaInyectada() {
        // Arrange
        DroneEnterprise mayor = drone("D-01", Sede.ECI, 95, true);
        DroneEnterprise elegido = drone("D-02", Sede.ECI, 70, true);
        List<DroneEnterprise> disponibles = List.of(mayor, elegido);
        SolicitudAsignacion solicitud = solicitud();
        preparar(disponibles, solicitud, Optional.of(elegido));
        // Act
        DroneEnterprise resultado = asignador().asignar(solicitud).orElseThrow();
        // Assert
        assertEquals(elegido.enMision(), resultado);
        assertTrue(elegido.disponible());
        verify(repo).findDisponibles(Sede.ECI);
        verify(clima).condicionesAptas(Sede.ECI, Sede.UNAL);
        verify(estrategia).seleccionar(disponibles, solicitud);
        verify(observador, times(1)).onAsignada(solicitud, resultado);
        verifyNoMoreInteractions(observador);
    }

    @Test
    void climaAdversoNoConsultaFlotaNiSeleccionaNiNotifica() {
        // Arrange
        SolicitudAsignacion solicitud = solicitud();
        when(clima.condicionesAptas(Sede.ECI, Sede.UNAL)).thenReturn(false);
        // Act
        var resultado = asignador().asignar(solicitud);
        // Assert
        assertTrue(resultado.isEmpty());
        verify(clima).condicionesAptas(Sede.ECI, Sede.UNAL);
        verifyNoInteractions(repo, estrategia, observador);
    }

    @Test
    void sinCandidatoNoNotificaUnaAsignacionInexistente() {
        // Arrange
        SolicitudAsignacion solicitud = solicitud();
        preparar(List.of(), solicitud, Optional.empty());
        // Act
        var resultado = asignador().asignar(solicitud);
        // Assert
        assertTrue(resultado.isEmpty());
        verify(estrategia).seleccionar(List.of(), solicitud);
        verifyNoInteractions(observador);
    }

    @Test
    void candidatoFueraDelRepositorioSeRechazaAunqueLaEstrategiaLoProponga() {
        // Arrange
        SolicitudAsignacion solicitud = solicitud();
        preparar(List.of(), solicitud, Optional.of(drone("D", Sede.ECI, 90, true)));
        // Act
        var resultado = asignador().asignar(solicitud);
        // Assert
        assertTrue(resultado.isEmpty());
        verifyNoInteractions(observador);
    }

    @Test
    void candidatoDeOtraSedeNoPuedeAsignarse() {
        // Arrange
        SolicitudAsignacion solicitud = solicitud();
        DroneEnterprise otraSede = drone("D", Sede.UNAL, 90, true);
        preparar(List.of(otraSede), solicitud, Optional.of(otraSede));
        // Act
        var resultado = asignador().asignar(solicitud);
        // Assert
        assertTrue(resultado.isEmpty());
        verifyNoInteractions(observador);
    }

    @Test
    void candidatoConBateriaInsuficienteNoPasaLaValidacionFinal() {
        // Arrange
        SolicitudAsignacion solicitud = solicitud();
        DroneEnterprise bajo = drone("D", Sede.ECI, 29, true);
        preparar(List.of(bajo), solicitud, Optional.of(bajo));
        // Act
        var resultado = asignador().asignar(solicitud);
        // Assert
        assertTrue(resultado.isEmpty());
        verifyNoInteractions(observador);
    }

    private AsignadorMisionEnterprise asignador() {
        return new AsignadorMisionEnterprise(repo, clima, estrategia, observador);
    }

    private void preparar(List<DroneEnterprise> drones, SolicitudAsignacion solicitud, Optional<DroneEnterprise> elegido) {
        when(clima.condicionesAptas(solicitud.origen(), solicitud.destino())).thenReturn(true);
        when(repo.findDisponibles(solicitud.origen())).thenReturn(drones);
        when(estrategia.seleccionar(drones, solicitud)).thenReturn(elegido);
    }
}
