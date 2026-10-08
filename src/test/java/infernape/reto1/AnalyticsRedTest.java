package infernape.reto1;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import infernape.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static infernape.support.DatosEnterprise.*;
import static org.junit.jupiter.api.Assertions.*;

class AnalyticsRedTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("escenarios")
    void calculaTodasLasSedesSinInventarActividad(String nombre, List<MisionEnterprise> entrada,
                                                 Map<Sede, MetricasSede> esperadas) {
        // Arrange
        AnalyticsRed analytics = new AnalyticsRed();
        // Act
        Map<Sede, Optional<MetricasSede>> resultado = analytics.calcular(entrada);
        // Assert
        assertEquals(Set.of(Sede.values()), resultado.keySet());
        Arrays.stream(Sede.values()).forEach(sede -> assertEquals(Optional.ofNullable(esperadas.get(sede)), resultado.get(sede)));
        assertThrows(UnsupportedOperationException.class, () -> resultado.clear());
    }

    static Stream<Arguments> escenarios() {
        return Stream.of(Arguments.of("sede vacia", List.of(), Map.of()), unica(), empateDrones(), empateSedes(), redCompleta());
    }

    private static Arguments unica() {
        return Arguments.of("una mision", List.of(entregada("M-1", Sede.ECI, "D-01", Prioridad.URGENTE, 10)),
            Map.of(Sede.ECI, metrica(100, 10, "D-01", 100)));
    }

    private static Arguments empateDrones() {
        return Arguments.of("empate uso drones", List.of(entregada("M-1", Sede.ECI, "D-09", Prioridad.NORMAL, 10),
            fallida("M-2", Sede.ECI, "D-01", Prioridad.URGENTE)), Map.of(Sede.ECI, metrica(50, 10, "D-01", 50)));
    }

    private static Arguments empateSedes() {
        return Arguments.of("empate entre sedes", List.of(entregada("M-1", Sede.ECI, "D-01", Prioridad.NORMAL, 10),
            entregada("M-2", Sede.UNAL, "D-02", Prioridad.NORMAL, 10)),
            Map.of(Sede.ECI, metrica(100, 10, "D-01", 0), Sede.UNAL, metrica(100, 10, "D-02", 0)));
    }

    private static Arguments redCompleta() {
        return Arguments.of("red completa", List.of(entregada("M-1", Sede.ECI, "D-01", Prioridad.NORMAL, 8),
            entregada("M-2", Sede.UNAL, "D-02", Prioridad.URGENTE, 12), entregada("M-3", Sede.UNIANDES, "D-03", Prioridad.NORMAL, 14),
            entregada("M-4", Sede.EAFIT, "D-04", Prioridad.URGENTE, 20)), Map.of(Sede.ECI, metrica(100, 8, "D-01", 0),
            Sede.UNAL, metrica(100, 12, "D-02", 100), Sede.UNIANDES, metrica(100, 14, "D-03", 0), Sede.EAFIT, metrica(100, 20, "D-04", 100)));
    }

    private static MetricasSede metrica(double exito, double minutos, String drone, double urgente) {
        return new MetricasSede(exito, OptionalDouble.of(minutos), Optional.of(drone), urgente);
    }

    @Test
    void actividadPendienteSinDroneNoInventaPromedioNiGanador() {
        // Arrange
        MisionEnterprise pendiente = new MisionEnterprise("M-1", Sede.ECI, Optional.empty(),
            EstadoMision.PENDIENTE, Prioridad.NORMAL, OptionalDouble.empty());
        // Act
        MetricasSede resultado = new AnalyticsRed().calcular(List.of(pendiente)).get(Sede.ECI).orElseThrow();
        // Assert
        assertEquals(new MetricasSede(0, OptionalDouble.empty(), Optional.empty(), 0), resultado);
    }

    @Test
    void promedioSoloIncluyeEntregadasYElGanadorCuentaTodasLasAsignadas() {
        // Arrange
        List<MisionEnterprise> misiones = List.of(entregada("M-1", Sede.ECI, "D-09", Prioridad.NORMAL, 10),
            entregada("M-2", Sede.ECI, "D-09", Prioridad.NORMAL, 20), fallida("M-3", Sede.ECI, "D-01", Prioridad.URGENTE));
        // Act
        MetricasSede resultado = new AnalyticsRed().calcular(misiones).get(Sede.ECI).orElseThrow();
        // Assert
        assertEquals(15, resultado.tiempoPromedioEntrega().orElseThrow());
        assertEquals(200.0 / 3, resultado.tasaExito());
        assertEquals(Optional.of("D-09"), resultado.droneMasUtilizado());
        assertEquals(100.0 / 3, resultado.porcentajeUrgentes());
    }

    @Test
    void listaDeMisionesSeLeeUnaSolaVez() {
        // Arrange
        AtomicInteger lecturas = new AtomicInteger();
        List<MisionEnterprise> datos = List.of(entregada("M-1", Sede.ECI, "D-01", Prioridad.NORMAL, 10),
            fallida("M-2", Sede.UNAL, "D-02", Prioridad.NORMAL));
        List<MisionEnterprise> contada = new AbstractList<>() {
            public int size() { return datos.size(); }
            public MisionEnterprise get(int i) { lecturas.incrementAndGet(); return datos.get(i); }
        };
        // Act
        new AnalyticsRed().calcular(contada);
        // Assert
        assertEquals(datos.size(), lecturas.get());
    }

    @Test
    void combinadorFusionaParticionesSinPerderEntregasUrgentesNiUso() {
        // Arrange
        AcumuladorSede uno = new AcumuladorSede();
        AcumuladorSede dos = new AcumuladorSede();
        uno.aceptar(entregada("M-1", Sede.ECI, "D-09", Prioridad.NORMAL, 10));
        dos.aceptar(fallida("M-2", Sede.ECI, "D-01", Prioridad.URGENTE));
        // Act
        MetricasSede resultado = uno.combinar(dos).resultado();
        // Assert
        assertEquals(metrica(50, 10, "D-01", 50), resultado);
    }
}
