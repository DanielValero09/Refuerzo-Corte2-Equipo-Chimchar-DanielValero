package monferno.reto2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.Mision;
import monferno.model.TipoDrone;
import monferno.reto3.GestorMisiones;
import monferno.reto3.strategy.MayorBateriaStrategy;
import org.junit.jupiter.api.Test;

import static monferno.support.DatosMonferno.*;
import static org.junit.jupiter.api.Assertions.*;

class AsignadorMisionTest {
    @Test
    void conservaDatosDeUnaMisionYaAsociada() {
        // Arrange
        Drone drone = drone("D-01", TipoDrone.MINI, 85, 0);
        Mision original = pendiente(100).conDrone(drone);
        AsignadorMision asignador = new AsignadorMision(new GestorMisiones(new MayorBateriaStrategy()), mision -> { });
        // Act
        Mision resultado = asignador.asignar(List.of(drone), original).orElseThrow();
        // Assert
        assertEquals(original, resultado);
    }

    @Test
    void asignaAutomaticamenteYNotificaUnaVezConElResultado() {
        // Arrange
        Drone elegido = drone("D-03", TipoDrone.EXPRESS, 91, 2);
        List<Drone> flota = List.of(drone("D-01", TipoDrone.MINI, 85, 1), elegido);
        List<Mision> notificaciones = new ArrayList<>();
        Mision original = pendiente(100);
        AsignadorMision asignador = new AsignadorMision(new GestorMisiones(new MayorBateriaStrategy()), notificaciones::add);
        // Act
        Mision resultado = asignador.asignar(flota, original).orElseThrow();
        // Assert
        assertAll(() -> assertEquals(Optional.of(elegido), resultado.drone()),
            () -> assertEquals(List.of(resultado), notificaciones),
            () -> assertSame(resultado, notificaciones.get(0)),
            () -> assertTrue(original.drone().isEmpty()),
            () -> assertEquals(original.id(), resultado.id()),
            () -> assertEquals(original.creadaEn(), resultado.creadaEn()));
    }

    @Test
    void sinCandidatoNoAsignaNiNotifica() {
        // Arrange
        List<Mision> notificaciones = new ArrayList<>();
        AsignadorMision asignador = new AsignadorMision(new GestorMisiones(new MayorBateriaStrategy()), notificaciones::add);
        Drone cargo = drone("D-01", TipoDrone.CARGO, 90, 0);
        // Act
        Optional<Mision> sinFlota = asignador.asignar(List.of(), pendiente(100));
        Optional<Mision> cargaProhibida = asignador.asignar(List.of(cargo), pendiente(99));
        // Assert
        assertAll(() -> assertTrue(sinFlota.isEmpty()), () -> assertTrue(cargaProhibida.isEmpty()),
            () -> assertTrue(notificaciones.isEmpty()));
    }
}
