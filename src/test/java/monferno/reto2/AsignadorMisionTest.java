package monferno.reto2;

import java.util.List;
import monferno.model.Drone;
import monferno.model.Mision;
import monferno.model.TipoDrone;
import monferno.reto3.GestorMisiones;
import monferno.reto3.strategy.MayorBateriaStrategy;
import org.junit.jupiter.api.Test;

import static monferno.support.DatosMonferno.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
