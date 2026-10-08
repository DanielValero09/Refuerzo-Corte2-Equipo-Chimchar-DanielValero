package infernape.application;

import infernape.domain.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaPrioridadEnterpriseTest {
    private final PoliticaPrioridadEnterprise politica = new PoliticaPrioridadEnterprise();
    private DroneEnterprise drone(String id, PerfilDrone perfil, int bateria) {
        return new DroneEnterprise(id, Sede.ECI, perfil, bateria, true);
    }
    private SolicitudAsignacion solicitud(int peso, Prioridad prioridad) {
        return new SolicitudAsignacion("ME-P", Sede.ECI, Sede.UNAL, peso, prioridad);
    }
    @Test void normalEligeMayorBateriaConEmpatePorIdAscendente() {
        // Arrange
        var flota = List.of(drone("DE-02", PerfilDrone.MINI, 95), drone("DE-01", PerfilDrone.EXPRESS, 95));
        // Act
        var elegido = politica.seleccionar(flota, solicitud(300, Prioridad.NORMAL)).orElseThrow();
        // Assert
        assertEquals("DE-01", elegido.id());
    }
    @Test void urgentePrefiereExpressAunqueTengaMenosBateria() {
        // Arrange
        var flota = List.of(drone("DE-M", PerfilDrone.MINI, 95), drone("DE-E", PerfilDrone.EXPRESS, 30));
        // Act
        var elegido = politica.seleccionar(flota, solicitud(300, Prioridad.URGENTE)).orElseThrow();
        // Assert
        assertEquals("DE-E", elegido.id());
    }
    @Test void expressNoSoporta901GramosYSeUsaCargo() {
        // Arrange
        var flota = List.of(drone("DE-E", PerfilDrone.EXPRESS, 95), drone("DE-C", PerfilDrone.CARGO, 60));
        // Act
        var elegido = politica.seleccionar(flota, solicitud(901, Prioridad.URGENTE)).orElseThrow();
        // Assert
        assertEquals("DE-C", elegido.id());
    }
    @Test void cargoNoPuedeTransportarMenosDe100Gramos() {
        // Arrange
        var flota = List.of(drone("DE-C", PerfilDrone.CARGO, 95));
        // Act
        var elegido = politica.seleccionar(flota, solicitud(99, Prioridad.NORMAL));
        // Assert
        assertTrue(elegido.isEmpty());
    }
    @Test void excluyeDroneDeOtraSedeYOcupado() {
        // Arrange
        var flota = List.of(new DroneEnterprise("DE-U", Sede.UNAL, PerfilDrone.MINI, 95, true),
            new DroneEnterprise("DE-O", Sede.ECI, PerfilDrone.MINI, 95, false));
        // Act
        var elegido = politica.seleccionar(flota, solicitud(300, Prioridad.BAJO));
        // Assert
        assertTrue(elegido.isEmpty());
    }
    @Test void urgenteSinExpressHaceFallbackCompatibleYRespetaBateria() {
        // Arrange
        var flota = List.of(drone("DE-M", PerfilDrone.MINI, 30), drone("DE-E", PerfilDrone.EXPRESS, 29));
        // Act
        var elegido = politica.seleccionar(flota, solicitud(300, Prioridad.URGENTE)).orElseThrow();
        // Assert
        assertEquals("DE-M", elegido.id());
    }
}
