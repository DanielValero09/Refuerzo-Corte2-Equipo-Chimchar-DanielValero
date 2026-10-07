package reto3;

import org.junit.jupiter.api.Test;
import support.ConsoleCapture;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Reto3Test {
    @Test
    void demostracionConservaOpcionalesValidaCadenaYSeleccionaD03() {
        String salida = ConsoleCapture.ejecutar(() -> Reto3.main(new String[0]));
        assertAll(
            () -> assertTrue(salida.contains("prioridad=1")),
            () -> assertTrue(salida.contains("notas=Entrega prioritaria")),
            () -> assertTrue(salida.contains("horaMaximaEntrega=14:00")),
            () -> assertTrue(salida.contains("validaciones?: true")),
            () -> assertTrue(salida.contains("Drone seleccionado: D-03 - Batería: 91%"))
        );
    }
}
