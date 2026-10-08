package monferno.reto4;

import java.util.List;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.Mision;
import monferno.model.TipoDrone;
import monferno.reto3.GestorMisiones;
import monferno.reto3.strategy.MayorBateriaStrategy;
import monferno.reto3.strategy.MenorUsoAcumuladoStrategy;
import monferno.reto3.strategy.TipoCompatibleCargaStrategy;
import monferno.reto3.validation.CadenaValidacion;
import monferno.reto3.validation.ValidadorBateria;
import monferno.reto3.validation.ValidadorCarga;
import monferno.reto3.validation.ValidadorDisponibilidad;
import org.junit.jupiter.api.Test;

import static monferno.support.DatosMonferno.*;
import static org.junit.jupiter.api.Assertions.*;

class GestorMisionesStrategyTest {
    @Test
    void mismaInstanciaFuncionaConLasTresEstrategiasSinCambiarSuCodigo() {
        // Arrange
        Drone mayorBateria = drone("D-01", TipoDrone.CARGO, 95, 10);
        Drone menorUso = drone("D-02", TipoDrone.EXPRESS, 70, 1);
        Drone menorCapacidad = drone("D-03", TipoDrone.MINI, 80, 5);
        List<Drone> flota = List.of(mayorBateria, menorUso, menorCapacidad);
        Mision mision = pendiente(100);
        GestorMisiones gestor = new GestorMisiones(new MayorBateriaStrategy());
        // Act
        Optional<Drone> porBateria = gestor.asignar(flota, mision);
        gestor.cambiarEstrategia(new MenorUsoAcumuladoStrategy());
        Optional<Drone> porUso = gestor.asignar(flota, mision);
        gestor.cambiarEstrategia(new TipoCompatibleCargaStrategy());
        Optional<Drone> porTipo = gestor.asignar(flota, mision);
        // Assert
        assertAll(() -> assertEquals(Optional.of(mayorBateria), porBateria),
            () -> assertEquals(Optional.of(menorUso), porUso), () -> assertEquals(Optional.of(menorCapacidad), porTipo));
    }

    @Test
    void admiteUnaImplementacionLocalSinConocerSuTipo() {
        // Arrange
        Drone esperado = drone("D-01", TipoDrone.MINI, 85, 0);
        GestorMisiones gestor = new GestorMisiones((flota, mision) -> flota.stream().findFirst());
        // Act
        Optional<Drone> resultado = gestor.asignar(List.of(esperado), pendiente(100));
        // Assert
        assertEquals(Optional.of(esperado), resultado);
    }

    @Test
    void nuevoValidadorSeComponeSinEditarEstrategiaNiGestor() {
        // Arrange
        Drone bloqueado = drone("D-01", TipoDrone.MINI, 95, 0);
        Drone permitido = drone("D-02", TipoDrone.MINI, 85, 0);
        CadenaValidacion cadena = new CadenaValidacion(List.of(new ValidadorBateria(),
            new ValidadorDisponibilidad(), new ValidadorCarga(), (drone, mision) -> !drone.id().equals("D-01")));
        GestorMisiones gestor = new GestorMisiones(new MayorBateriaStrategy(cadena));
        // Act
        Optional<Drone> resultado = gestor.asignar(List.of(bloqueado, permitido), pendiente(100));
        // Assert
        assertEquals(Optional.of(permitido), resultado);
    }
}
