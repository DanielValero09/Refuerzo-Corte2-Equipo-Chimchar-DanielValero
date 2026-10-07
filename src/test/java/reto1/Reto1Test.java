package reto1;

import org.junit.jupiter.api.Test;
import support.ConsoleCapture;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Reto1Test {
    @Test
    void demostracionDevuelveLasCuatroConsultasDelMvp() {
        String salida = ConsoleCapture.ejecutar(() -> reto1.main(new String[0]));
        assertAll(
            () -> assertTrue(salida.contains("Consulta 1: [D-03, D-01, D-05]")),
            () -> assertTrue(salida.contains("Consulta 2: true")),
            () -> assertTrue(salida.contains("Consulta 3: 1")),
            () -> assertTrue(salida.contains("Consulta 4: [D-01: 85%, D-02: 42%, D-03: 91%, D-04: 18%, D-05: 67%]"))
        );
    }
}
