package monferno.reto1;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.EstadoMision;
import monferno.model.Mision;
import monferno.model.Prioridad;
import monferno.model.TipoDrone;
import org.junit.jupiter.api.Test;

import static monferno.support.DatosMonferno.*;
import static org.junit.jupiter.api.Assertions.*;

class EstadisticasMisionesTest {
    private final EstadisticasMisiones estadisticas = new EstadisticasMisiones();
    private final Drone mini = drone("D-01", TipoDrone.MINI, 85, 1);
    private final Drone cargo = drone("D-02", TipoDrone.CARGO, 70, 9);
    private final Drone express = drone("D-03", TipoDrone.EXPRESS, 91, 2);

    @Test
    void cuentaEntregadasHoyPorLosTresTiposYExcluyeOtrosEstadosYFechas() {
        // Arrange
        List<Mision> misiones = List.of(
            entregada("M-01", mini, AHORA.minusHours(2), AHORA),
            entregada("M-02", mini, AHORA.minusHours(1), AHORA),
            entregada("M-03", cargo, AHORA.minusHours(1), AHORA),
            entregada("M-04", express, AHORA.minusHours(1), AHORA),
            entregada("M-05", cargo, AHORA.minusDays(1), AHORA.minusDays(1)), fallida("M-06", mini));
        // Act
        Map<TipoDrone, Long> resultado = estadisticas.completadasHoyPorTipo(misiones, AHORA.toLocalDate());
        // Assert
        assertEquals(Map.of(TipoDrone.MINI, 2L, TipoDrone.CARGO, 1L, TipoDrone.EXPRESS, 1L), resultado);
    }

    @Test
    void cuentaEntregaDeHoyAunqueSeCreoAyer() {
        // Arrange
        List<Mision> misiones = List.of(entregada("M-01", mini, AHORA.minusDays(1), AHORA));
        // Act
        Map<TipoDrone, Long> resultado = estadisticas.completadasHoyPorTipo(misiones, AHORA.toLocalDate());
        // Assert
        assertEquals(Map.of(TipoDrone.MINI, 1L), resultado);
    }

    @Test
    void ganadorSeCalculaContandoMisionesEntregadasNoElContadorDelDrone() {
        // Arrange
        List<Mision> misiones = List.of(entregada("M-01", mini, AHORA, AHORA),
            entregada("M-02", mini, AHORA, AHORA), entregada("M-03", cargo, AHORA, AHORA), fallida("M-04", cargo));
        // Act
        Optional<Drone> ganador = estadisticas.droneConMasCompletadas(misiones);
        // Assert
        assertEquals(Optional.of(mini), ganador);
    }

    @Test
    void agrupaPorIdAunqueLasMisionesContenganSnapshotsDiferentes() {
        // Arrange
        Drone actualizado = drone("D-01", TipoDrone.MINI, 60, 4);
        List<Mision> misiones = List.of(entregada("M-01", mini, AHORA, AHORA),
            entregada("M-02", actualizado, AHORA, AHORA), entregada("M-03", cargo, AHORA, AHORA));
        // Act
        String idGanador = estadisticas.droneConMasCompletadas(misiones).orElseThrow().id();
        // Assert
        assertEquals("D-01", idGanador);
    }

    @Test
    void desempataPorIdAscendenteIndependientementeDelOrden() {
        // Arrange
        Mision primera = entregada("M-01", mini, AHORA, AHORA);
        Mision segunda = entregada("M-02", express, AHORA, AHORA);
        // Act
        Optional<Drone> directo = estadisticas.droneConMasCompletadas(List.of(primera, segunda));
        Optional<Drone> inverso = estadisticas.droneConMasCompletadas(List.of(segunda, primera));
        // Assert
        assertAll(() -> assertEquals(Optional.of(mini), directo), () -> assertEquals(directo, inverso));
    }

    @Test
    void calculaPorcentajeFallidasSobreTodosLosEstados() {
        // Arrange
        Mision vuelo = new Mision("M-03", Optional.of(express), "Biblioteca", 100,
            Prioridad.NORMAL, EstadoMision.EN_VUELO, AHORA, Optional.empty());
        List<Mision> misiones = List.of(fallida("M-01", mini), entregada("M-02", mini, AHORA, AHORA), vuelo, pendiente(100));
        // Act
        double porcentaje = estadisticas.porcentajeFallidas(misiones);
        // Assert
        assertEquals(25.0, porcentaje, 0.0001);
    }

    @Test
    void listasVaciasDevuelvenMapaVacioOptionalVacioCeroYFalse() {
        // Arrange
        List<Mision> misiones = List.of();
        // Act
        Map<TipoDrone, Long> conteo = estadisticas.completadasHoyPorTipo(misiones, AHORA.toLocalDate());
        Optional<Drone> ganador = estadisticas.droneConMasCompletadas(misiones);
        double porcentaje = estadisticas.porcentajeFallidas(misiones);
        boolean urgente = estadisticas.existeUrgentePendienteAntigua(misiones, AHORA);
        // Assert
        assertAll(() -> assertTrue(conteo.isEmpty()), () -> assertTrue(ganador.isEmpty()),
            () -> assertEquals(0.0, porcentaje), () -> assertFalse(urgente));
    }

    @Test
    void urgentePendienteHace11MinutosDevuelveTrue() {
        // Arrange
        Mision mission = Mision.pendiente("M-01", "Biblioteca", 100, Prioridad.URGENTE, AHORA.minusMinutes(11));
        // Act
        boolean resultado = estadisticas.existeUrgentePendienteAntigua(List.of(mission), AHORA);
        // Assert
        assertTrue(resultado);
    }

    @Test
    void urgentePendienteHaceExactamente10MinutosDevuelveFalse() {
        // Arrange
        Mision mission = Mision.pendiente("M-01", "Biblioteca", 100, Prioridad.URGENTE, AHORA.minusMinutes(10));
        // Act
        boolean resultado = estadisticas.existeUrgentePendienteAntigua(List.of(mission), AHORA);
        // Assert
        assertFalse(resultado);
    }

    @Test
    void normalPendienteAntiguaDevuelveFalse() {
        // Arrange
        Mision mission = Mision.pendiente("M-01", "Biblioteca", 100, Prioridad.NORMAL, AHORA.minusHours(1));
        // Act
        boolean resultado = estadisticas.existeUrgentePendienteAntigua(List.of(mission), AHORA);
        // Assert
        assertFalse(resultado);
    }

    @Test
    void urgenteEnVueloAntiguaDevuelveFalse() {
        // Arrange
        Mision mission = new Mision("M-01", Optional.of(express), "Biblioteca", 100, Prioridad.URGENTE,
            EstadoMision.EN_VUELO, AHORA.minusHours(1), Optional.empty());
        // Act
        boolean resultado = estadisticas.existeUrgentePendienteAntigua(List.of(mission), AHORA);
        // Assert
        assertFalse(resultado);
    }

    @Test
    void urgenteConFechaFuturaNoSeConsideraAntigua() {
        // Arrange
        Mision mission = Mision.pendiente("M-01", "Biblioteca", 100, Prioridad.URGENTE, AHORA.plusMinutes(1));
        // Act
        boolean resultado = estadisticas.existeUrgentePendienteAntigua(List.of(mission), AHORA);
        // Assert
        assertFalse(resultado);
    }
}
