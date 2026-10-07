package reto12;

import model.Drone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidadorMisionTest {
    private final ValidadorMision validador = new ValidadorMision();

    @Test
    void bateria29EsInsuficiente() {
        // Arrange
        Drone drone = new Drone("D-01", "DJI Mini 3", 29, true, "Bloque A");
        // Act
        boolean resultado = validador.tieneBateriaSuficiente(drone);
        // Assert
        assertFalse(resultado);
    }

    @Test
    void bateria30EsSuficiente() {
        // Arrange
        Drone drone = new Drone("D-01", "DJI Mini 3", 30, true, "Bloque A");
        // Act
        boolean resultado = validador.tieneBateriaSuficiente(drone);
        // Assert
        assertTrue(resultado);
    }

    @Test
    void bateria91EsSuficienteAunqueNoEsteDisponible() {
        // Arrange
        Drone drone = new Drone("D-03", "DJI Mini 3", 91, false, "Bloque C");
        // Act
        boolean resultado = validador.tieneBateriaSuficiente(drone);
        // Assert
        assertTrue(resultado);
    }

    @Test
    void bloqueAEsDestinoValido() {
        // Arrange
        String destino = "Bloque A";
        // Act
        Executable accion = () -> validador.validarDestino(destino);
        // Assert
        assertDoesNotThrow(accion);
    }

    @Test
    void bibliotecaEsDestinoValido() {
        // Arrange
        String destino = "Biblioteca";
        // Act
        Executable accion = () -> validador.validarDestino(destino);
        // Assert
        assertDoesNotThrow(accion);
    }

    @Test
    void edificioInexistenteLanzaExcepcion() {
        // Arrange
        String destino = "Edificio Inexistente";
        // Act
        Executable accion = () -> validador.validarDestino(destino);
        // Assert
        assertThrows(DestinoInvalidoException.class, accion);
    }

    @Test
    void droneDisponibleDevuelveTrue() {
        // Arrange
        Drone drone = new Drone("D-01", "DJI Mini 3", 85, true, "Bloque A");
        // Act
        boolean resultado = validador.droneEstaDisponible(drone);
        // Assert
        assertTrue(resultado);
    }

    @Test
    void droneNoDisponibleDevuelveFalse() {
        // Arrange
        Drone drone = new Drone("D-02", "DJI Mini 3", 42, false, "Biblioteca");
        // Act
        boolean resultado = validador.droneEstaDisponible(drone);
        // Assert
        assertFalse(resultado);
    }

    @Test
    void droneD04DisponibleConBateria18DevuelveTrue() {
        // Arrange
        Drone drone = new Drone("D-04", "DJI Mini 3", 18, true, "Bloque B");
        // Act
        boolean resultado = validador.droneEstaDisponible(drone);
        // Assert
        assertTrue(resultado);
    }
}
