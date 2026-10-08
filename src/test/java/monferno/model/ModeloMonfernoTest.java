package monferno.model;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import monferno.support.DatosMonferno;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModeloMonfernoTest {
    @Test
    void flotaInicialTiene20IdsUnicosYLosTresTipos() {
        // Arrange
        int cantidadEsperada = 20;
        // Act
        List<Drone> flota = FlotaInicial.crear();
        // Assert
        assertAll(
            () -> assertEquals(cantidadEsperada, flota.size()),
            () -> assertEquals(20, flota.stream().map(Drone::id).distinct().count()),
            () -> assertEquals(3, flota.stream().map(Drone::tipo).distinct().count()),
            () -> assertTrue(flota.stream().allMatch(Drone::disponible)),
            () -> assertThrows(UnsupportedOperationException.class, () -> flota.add(flota.get(0)))
        );
    }

    @Test
    void constructorDeDroneSinUsoAplicaCero() {
        // Arrange
        TipoDrone tipo = TipoDrone.MINI;
        // Act
        Drone drone = new Drone("D-01", tipo, 85, true, EstadoDrone.DISPONIBLE);
        // Assert
        assertEquals(0, drone.misionesCompletadas());
    }

    @Test
    void modeloRechazaDatosInvalidos() {
        // Arrange
        TipoDrone tipo = TipoDrone.MINI;
        // Act
        Runnable bateriaNegativa = () -> DatosMonferno.drone("D-01", tipo, -1, 0);
        Runnable bateriaExcesiva = () -> DatosMonferno.drone("D-01", tipo, 101, 0);
        Runnable usoNegativo = () -> DatosMonferno.drone("D-01", tipo, 85, -1);
        Runnable pesoNegativo = () -> DatosMonferno.pendiente(-1);
        // Assert
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, bateriaNegativa::run),
            () -> assertThrows(IllegalArgumentException.class, bateriaExcesiva::run),
            () -> assertThrows(IllegalArgumentException.class, usoNegativo::run),
            () -> assertThrows(IllegalArgumentException.class, pesoNegativo::run)
        );
    }

    @Test
    void asignarDroneConservaLaMisionOriginalYTodosSusDatos() {
        // Arrange
        Mision original = Mision.pendiente("M-01", "Bloque C", 800, Prioridad.URGENTE, DatosMonferno.AHORA);
        Drone drone = DatosMonferno.drone("D-03", TipoDrone.EXPRESS, 90, 1);
        // Act
        Mision asignada = original.conDrone(drone);
        // Assert
        assertAll(
            () -> assertTrue(original.drone().isEmpty()),
            () -> assertNotSame(original, asignada),
            () -> assertEquals(Optional.of(drone), asignada.drone()),
            () -> assertEquals(original.id(), asignada.id()),
            () -> assertEquals(original.destino(), asignada.destino()),
            () -> assertEquals(original.pesoPaqueteGramos(), asignada.pesoPaqueteGramos()),
            () -> assertEquals(original.prioridad(), asignada.prioridad()),
            () -> assertEquals(original.estado(), asignada.estado()),
            () -> assertEquals(original.creadaEn(), asignada.creadaEn()),
            () -> assertFalse(asignada.entregadaEn().isPresent())
        );
    }

    @Test
    void entregaRequiereInstanteRealNoAnteriorALaCreacion() {
        // Arrange
        Drone drone = DatosMonferno.drone("D-01", TipoDrone.MINI, 85, 2);
        // Act
        Runnable sinInstante = () -> new Mision("M-01", Optional.of(drone), "Biblioteca", 100,
            Prioridad.NORMAL, EstadoMision.ENTREGADA, DatosMonferno.AHORA, Optional.empty());
        Runnable instanteAnterior = () -> DatosMonferno.entregada("M-01", drone,
            DatosMonferno.AHORA, DatosMonferno.AHORA.minusMinutes(1));
        // Assert
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, sinInstante::run),
            () -> assertThrows(IllegalArgumentException.class, instanteAnterior::run)
        );
    }
}
