package monferno.reto3;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import monferno.model.Drone;
import monferno.model.EstadoDrone;
import monferno.model.Mision;
import monferno.model.TipoDrone;
import monferno.reto3.strategy.EstrategiaAsignacion;
import monferno.reto3.strategy.MayorBateriaStrategy;
import monferno.reto3.strategy.MenorUsoAcumuladoStrategy;
import monferno.reto3.strategy.TipoCompatibleCargaStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static monferno.support.DatosMonferno.*;
import static org.junit.jupiter.api.Assertions.*;

class EstrategiasAsignacionTest {
    @ParameterizedTest
    @MethodSource("estrategias")
    void todasExigenBateriaDisponibilidadEstadoYCapacidad(EstrategiaAsignacion estrategia) {
        // Arrange
        Drone apto = drone("D-01", TipoDrone.MINI, 30, 7);
        List<Drone> flota = List.of(
            new Drone("D-02", TipoDrone.MINI, 100, false, EstadoDrone.DISPONIBLE, 0),
            new Drone("D-03", TipoDrone.MINI, 100, true, EstadoDrone.FALLO, 0),
            new Drone("D-04", TipoDrone.MINI, 100, true, EstadoDrone.EN_VUELO, 0),
            new Drone("D-05", TipoDrone.MINI, 100, true, EstadoDrone.ATERRIZANDO, 0),
            drone("D-06", TipoDrone.MINI, 29, 0), apto);
        // Act
        Optional<Drone> seleccionado = estrategia.seleccionar(flota, pendiente(500));
        // Assert
        assertEquals(Optional.of(apto), seleccionado);
    }

    @ParameterizedTest
    @MethodSource("estrategias")
    void todasRechazanCargoPara99GramosYAceptan100(EstrategiaAsignacion estrategia) {
        // Arrange
        Drone cargo = drone("D-01", TipoDrone.CARGO, 85, 0);
        // Act
        Optional<Drone> demasiadoLigero = estrategia.seleccionar(List.of(cargo), pendiente(99));
        Optional<Drone> limite = estrategia.seleccionar(List.of(cargo), pendiente(100));
        // Assert
        assertAll(() -> assertTrue(demasiadoLigero.isEmpty()), () -> assertEquals(Optional.of(cargo), limite));
    }

    @ParameterizedTest
    @MethodSource("estrategias")
    void todasDevuelvenVacioSinCandidatosOConPaqueteExcesivo(EstrategiaAsignacion estrategia) {
        // Arrange
        List<Drone> flota = List.of(drone("D-01", TipoDrone.MINI, 90, 0),
            drone("D-02", TipoDrone.EXPRESS, 90, 0), drone("D-03", TipoDrone.CARGO, 90, 0));
        // Act
        Optional<Drone> sinFlota = estrategia.seleccionar(List.of(), pendiente(100));
        Optional<Drone> excesivo = estrategia.seleccionar(flota, pendiente(2001));
        // Assert
        assertAll(() -> assertTrue(sinFlota.isEmpty()), () -> assertTrue(excesivo.isEmpty()));
    }

    @Test
    void mayorBateriaSeleccionaElMaximoCompatible() {
        // Arrange
        Drone miniIncompatible = drone("D-01", TipoDrone.MINI, 100, 0);
        Drone elegido = drone("D-03", TipoDrone.CARGO, 91, 3);
        List<Drone> flota = List.of(miniIncompatible, drone("D-02", TipoDrone.CARGO, 80, 1), elegido);
        // Act
        Optional<Drone> resultado = new MayorBateriaStrategy().seleccionar(flota, pendiente(900));
        // Assert
        assertEquals(Optional.of(elegido), resultado);
    }

    @Test
    void mayorBateriaDesempataPorIdAscendente() {
        // Arrange
        Drone primero = drone("D-01", TipoDrone.MINI, 85, 1);
        Drone segundo = drone("D-02", TipoDrone.MINI, 85, 0);
        // Act
        Optional<Drone> resultado = new MayorBateriaStrategy().seleccionar(List.of(segundo, primero), pendiente(100));
        // Assert
        assertEquals(Optional.of(primero), resultado);
    }

    @Test
    void menorUsoSeleccionaElMenorContadorCompatible() {
        // Arrange
        Drone incompatible = drone("D-01", TipoDrone.MINI, 90, 0);
        Drone elegido = drone("D-03", TipoDrone.CARGO, 50, 1);
        List<Drone> flota = List.of(incompatible, drone("D-02", TipoDrone.CARGO, 90, 10), elegido);
        // Act
        Optional<Drone> resultado = new MenorUsoAcumuladoStrategy().seleccionar(flota, pendiente(900));
        // Assert
        assertEquals(Optional.of(elegido), resultado);
    }

    @Test
    void menorUsoDesempataPorIdAscendente() {
        // Arrange
        Drone primero = drone("D-01", TipoDrone.MINI, 30, 1);
        Drone segundo = drone("D-02", TipoDrone.MINI, 90, 1);
        // Act
        Optional<Drone> resultado = new MenorUsoAcumuladoStrategy().seleccionar(List.of(segundo, primero), pendiente(100));
        // Assert
        assertEquals(Optional.of(primero), resultado);
    }

    @ParameterizedTest
    @CsvSource({"0,MINI", "99,MINI", "500,MINI", "501,EXPRESS", "800,EXPRESS", "801,CARGO", "2000,CARGO"})
    void tipoCompatiblePrefiereLaMenorCapacidadSuficiente(int peso, TipoDrone esperado) {
        // Arrange
        List<Drone> flota = List.of(drone("D-01", TipoDrone.CARGO, 90, 0),
            drone("D-02", TipoDrone.EXPRESS, 90, 0), drone("D-03", TipoDrone.MINI, 90, 0));
        // Act
        TipoDrone seleccionado = new TipoCompatibleCargaStrategy().seleccionar(flota, pendiente(peso)).orElseThrow().tipo();
        // Assert
        assertEquals(esperado, seleccionado);
    }

    @Test
    void tipoCompatibleUsaUnaCapacidadMayorSiFaltaElPreferido() {
        // Arrange
        Drone express = drone("D-02", TipoDrone.EXPRESS, 90, 0);
        Drone cargo = drone("D-01", TipoDrone.CARGO, 90, 0);
        TipoCompatibleCargaStrategy estrategia = new TipoCompatibleCargaStrategy();
        // Act
        Optional<Drone> sinMini = estrategia.seleccionar(List.of(cargo, express), pendiente(100));
        Optional<Drone> sinExpress = estrategia.seleccionar(List.of(cargo), pendiente(501));
        // Assert
        assertAll(() -> assertEquals(Optional.of(express), sinMini), () -> assertEquals(Optional.of(cargo), sinExpress));
    }

    @Test
    void tipoCompatibleDesempataDentroDelTipoPorIdAscendente() {
        // Arrange
        Drone primero = drone("D-01", TipoDrone.MINI, 30, 9);
        Drone segundo = drone("D-02", TipoDrone.MINI, 90, 0);
        // Act
        Optional<Drone> resultado = new TipoCompatibleCargaStrategy().seleccionar(List.of(segundo, primero), pendiente(100));
        // Assert
        assertEquals(Optional.of(primero), resultado);
    }

    private static Stream<EstrategiaAsignacion> estrategias() {
        return Stream.of(new MayorBateriaStrategy(), new MenorUsoAcumuladoStrategy(), new TipoCompatibleCargaStrategy());
    }
}
