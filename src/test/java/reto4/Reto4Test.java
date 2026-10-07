package reto4;

import org.junit.jupiter.api.Test;
import support.ConsoleCapture;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Reto4Test {
    @Test
    void demostracionIntegraAsignacionRepositorioAlertaReporteYRutas() {
        String salida = ConsoleCapture.ejecutar(() -> Reto4.main(new String[0]));
        assertAll(
            () -> assertTrue(salida.contains("Mision asignada: Mission[id=M-04")),
            () -> assertTrue(salida.contains("Mision guardada y recuperada: M-04 - Drone: D-03")),
            () -> assertTrue(salida.contains("Alerta para Operador SkyCampus: Mision M-04 asignada a D-03")),
            () -> assertTrue(salida.contains("M-04 | Biblioteca | PENDIENTE")),
            () -> assertTrue(salida.contains("Ruta directa: Bloque C -> Biblioteca")),
            () -> assertTrue(salida.contains("Ruta evitando obstaculos: Bloque C -> corredor seguro (evita obstaculos y edificios altos) -> Biblioteca"))
        );
    }
}
