package monferno.reto12;

import java.util.List;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.EstadoDrone;
import monferno.model.Mision;
import monferno.model.Prioridad;
import monferno.model.TipoDrone;
import monferno.reto3.observer.ObservadorDrone;
import monferno.support.DatosMonferno;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AsignadorMisionTest {
    @Mock ApiMeteorologica clima;
    @Mock ObservadorDrone notificador;

    @Test
    void misionNormalSeleccionaMayorBateriaIniciaVueloYNotificaUnaVez() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        Drone menor = DatosMonferno.drone("D-01", TipoDrone.MINI, 70, 4);
        Drone mayor = DatosMonferno.drone("D-03", TipoDrone.EXPRESS, 91, 2);
        List<Drone> flota = List.of(menor, mayor);
        Mision mision = mision(200, Prioridad.NORMAL);
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Drone resultado = asignador.asignar(flota, mision).orElseThrow();
        // Assert
        assertAll(() -> assertEquals("D-03", resultado.id()), () -> assertEquals(mayor.tipo(), resultado.tipo()),
            () -> assertEquals(91, resultado.bateria()), () -> assertEquals(2, resultado.misionesCompletadas()),
            () -> assertEquals(EstadoDrone.EN_VUELO, resultado.estado()), () -> assertFalse(resultado.disponible()),
            () -> assertEquals(EstadoDrone.DISPONIBLE, mayor.estado()), () -> assertTrue(mayor.disponible()),
            () -> assertEquals(List.of(menor, mayor), flota), () -> assertTrue(mision.drone().isEmpty()));
        Mockito.verify(clima, times(1)).esApto();
        Mockito.verify(notificador, times(1)).onEstadoCambiado(resultado, EstadoDrone.EN_VUELO);
        Mockito.verifyNoMoreInteractions(notificador);
    }

    @Test
    void climaAdversoBloqueaAsignacionSinNotificarVuelo() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(false);
        Drone drone = DatosMonferno.drone("D-01", TipoDrone.MINI, 85, 0);
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Optional<Drone> resultado = asignador.asignar(List.of(drone), mision(200, Prioridad.NORMAL));
        // Assert
        assertTrue(resultado.isEmpty());
        Mockito.verify(clima).esApto();
        Mockito.verify(notificador, never()).onEstadoCambiado(any(), eq(EstadoDrone.EN_VUELO));
    }

    @Test
    void sinDronesAptosRetornaVacioYNoNotifica() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        List<Drone> flota = List.of(DatosMonferno.drone("D-01", TipoDrone.MINI, 29, 0),
            new Drone("D-02", TipoDrone.CARGO, 90, true, EstadoDrone.FALLO, 0),
            DatosMonferno.drone("D-03", TipoDrone.MINI, 95, 0));
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Optional<Drone> resultado = asignador.asignar(flota, mision(900, Prioridad.NORMAL));
        // Assert
        assertTrue(resultado.isEmpty());
        Mockito.verify(clima).esApto();
        Mockito.verifyNoInteractions(notificador);
    }

    @Test
    void paqueteDe2001GramosSeRechazaAntesDeConsultarClimaONotificar() {
        // Arrange
        Drone cargo = DatosMonferno.drone("D-01", TipoDrone.CARGO, 95, 0);
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Optional<Drone> resultado = asignador.asignar(List.of(cargo), mision(2001, Prioridad.NORMAL));
        // Assert
        assertTrue(resultado.isEmpty());
        Mockito.verify(clima, never()).esApto();
        Mockito.verifyNoInteractions(notificador);
    }

    @Test
    void urgenteSeleccionaExpressAunqueMiniTengaMayorBateria() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        Drone mini = DatosMonferno.drone("D-01", TipoDrone.MINI, 95, 0);
        Drone express = DatosMonferno.drone("D-02", TipoDrone.EXPRESS, 70, 3);
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Drone resultado = asignador.asignar(List.of(mini, express), mision(300, Prioridad.URGENTE)).orElseThrow();
        // Assert
        assertAll(() -> assertEquals("D-02", resultado.id()), () -> assertEquals(TipoDrone.EXPRESS, resultado.tipo()),
            () -> assertEquals(70, resultado.bateria()), () -> assertEquals(EstadoDrone.EN_VUELO, resultado.estado()),
            () -> assertFalse(resultado.disponible()));
        Mockito.verify(clima).esApto();
        Mockito.verify(notificador, times(1)).onEstadoCambiado(resultado, EstadoDrone.EN_VUELO);
        Mockito.verifyNoMoreInteractions(notificador);
    }

    private Mision mision(int peso, Prioridad prioridad) {
        return Mision.pendiente("M-V2-01", "Biblioteca", peso, prioridad, DatosMonferno.AHORA);
    }
}
