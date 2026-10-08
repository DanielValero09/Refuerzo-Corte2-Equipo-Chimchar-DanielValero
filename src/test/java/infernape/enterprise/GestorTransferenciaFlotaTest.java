package infernape.enterprise;

import infernape.application.GestorTransferenciaFlota;
import infernape.domain.*;
import org.junit.jupiter.api.Test;
import static infernape.support.DatosEnterprise.drone;
import static org.junit.jupiter.api.Assertions.*;

class GestorTransferenciaFlotaTest {
    @Test
    void transfiereDisponibleConservandoIdentidadPerfilYBateriaSinMutarOriginal() {
        // Arrange
        var original = drone("D-01", Sede.ECI, 85, true);
        // Act
        var transferido = new GestorTransferenciaFlota().transferir(original, Sede.UNAL).orElseThrow();
        // Assert
        assertEquals(new DroneEnterprise("D-01", Sede.UNAL, original.perfil(), 85, true), transferido);
        assertEquals(Sede.ECI, original.sede());
    }

    @Test
    void droneOcupadoNoPuedeTransferirse() {
        // Arrange
        var ocupado = drone("D-02", Sede.ECI, 85, false);
        // Act
        var resultado = new GestorTransferenciaFlota().transferir(ocupado, Sede.EAFIT);
        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void origenYDestinoIgualesSeRechazan() {
        // Arrange
        var disponible = drone("D-03", Sede.UNIANDES, 91, true);
        // Act
        var resultado = new GestorTransferenciaFlota().transferir(disponible, Sede.UNIANDES);
        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void cambioLogicoDeAdscripcionNoSimulaUnVueloNiConsumeBateria() {
        // Arrange
        var disponible = drone("D-04", Sede.ECI, 18, true);
        // Act
        var resultado = new GestorTransferenciaFlota().transferir(disponible, Sede.EAFIT).orElseThrow();
        // Assert
        assertEquals(18, resultado.bateria());
        assertFalse(resultado.aptoPara(200));
    }
}
