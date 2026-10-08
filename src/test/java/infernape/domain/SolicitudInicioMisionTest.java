package infernape.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudInicioMisionTest {
    private final SolicitudAsignacion solicitud = new SolicitudAsignacion("ME-S", Sede.ECI, Sede.UNAL, 300, Prioridad.NORMAL);
    @ParameterizedTest @ValueSource(doubles={0, -1, Double.NaN, Double.POSITIVE_INFINITY})
    void distanciaNoOperableEsRechazada(double distancia) {
        // Arrange
        var asignacion = solicitud;
        // Act
        var error = assertThrows(IllegalArgumentException.class, () -> new SolicitudInicioMision(asignacion, distancia, 100));
        // Assert
        assertFalse(error.getMessage().isBlank());
    }
    @ParameterizedTest @ValueSource(ints={0, -1})
    void alturaNoPositivaEsRechazada(int altura) {
        // Arrange
        var asignacion = solicitud;
        // Act
        var error = assertThrows(IllegalArgumentException.class, () -> new SolicitudInicioMision(asignacion, 6, altura));
        // Assert
        assertFalse(error.getMessage().isBlank());
    }
    @Test void origenIgualADestinoNoEsUnaSolicitudInterSede() {
        // Arrange
        var local = new SolicitudAsignacion("ME-L", Sede.ECI, Sede.ECI, 300, Prioridad.NORMAL);
        // Act
        var error = assertThrows(IllegalArgumentException.class, () -> new SolicitudInicioMision(local, 6, 100));
        // Assert
        assertTrue(error.getMessage().contains("sedes distintas"));
    }
    @Test void misionPersistibleNoAceptaPesoCero() {
        // Arrange
        var drone = new DroneEnterprise("DE-01", Sede.ECI, PerfilDrone.MINI, 85, false);
        // Act
        var error = assertThrows(IllegalArgumentException.class, () -> new MisionRegistrada("ME-01", Sede.ECI, Sede.UNAL, 0, Prioridad.NORMAL, drone, EstadoMision.EN_VUELO));
        // Assert
        assertTrue(error.getMessage().contains("Peso"));
    }
}
