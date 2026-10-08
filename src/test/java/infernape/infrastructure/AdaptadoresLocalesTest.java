package infernape.infrastructure;

import java.util.ArrayList;
import java.util.List;
import infernape.application.*;
import infernape.domain.*;
import org.junit.jupiter.api.Test;
import static infernape.support.DatosEnterprise.*;
import static org.junit.jupiter.api.Assertions.*;

class AdaptadoresLocalesTest {
    @Test
    void repositorioFiltraSedeYDisponibilidadYConservaSnapshot() {
        // Arrange
        DroneEnterprise eci = drone("D-01", Sede.ECI, 90, true);
        List<DroneEnterprise> mutable = new ArrayList<>(List.of(eci, drone("D-02", Sede.UNAL, 90, true), drone("D-03", Sede.ECI, 90, false)));
        RepositorioFlotaMemoria repo = new RepositorioFlotaMemoria(mutable);
        mutable.clear();
        // Act
        List<DroneEnterprise> disponibles = repo.findDisponibles(Sede.ECI);
        // Assert
        assertEquals(List.of(eci), disponibles);
        assertTrue(repo.findDisponibles(Sede.EAFIT).isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> disponibles.clear());
    }

    @Test
    void duplicadosNoCreanUnaFlotaAmbigua() {
        // Arrange
        DroneEnterprise drone = drone("D-01", Sede.ECI, 90, true);
        // Act
        var error = assertThrows(IllegalArgumentException.class, () -> new RepositorioFlotaMemoria(List.of(drone, drone)));
        // Assert
        assertFalse(error.getMessage().isBlank());
    }

    @Test
    void adaptadoresLocalesYPoliticaDeBateriaSeIntegranSinRed() {
        // Arrange
        DroneEnterprise elegido = drone("D-02", Sede.ECI, 80, true);
        RepositorioFlota repo = new RepositorioFlotaMemoria(List.of(drone("D-01", Sede.ECI, 30, true), elegido,
            drone("D-03", Sede.UNAL, 100, true), drone("D-04", Sede.ECI, 29, true)));
        List<DroneEnterprise> avisos = new ArrayList<>();
        var asignador = new AsignadorMisionEnterprise(repo, new ServicioClimaConfigurable(true), new AsignacionMayorBateria(), (s, d) -> avisos.add(d));
        // Act
        DroneEnterprise resultado = asignador.asignar(solicitud()).orElseThrow();
        // Assert
        assertEquals(elegido.enMision(), resultado);
        assertEquals(List.of(resultado), avisos);
        assertFalse(new ServicioClimaConfigurable(false).condicionesAptas(Sede.ECI, Sede.UNAL));
    }

    @Test
    void politicaFiltraCapacidadYDesempataPorId() {
        // Arrange
        DroneEnterprise uno = drone("D-01", Sede.ECI, 80, true);
        DroneEnterprise dos = drone("D-02", Sede.ECI, 80, true);
        SolicitudAsignacion pesada = new SolicitudAsignacion("M", Sede.ECI, Sede.UNAL, 501, Prioridad.NORMAL);
        // Act
        var ganador = new AsignacionMayorBateria().seleccionar(List.of(dos, uno), solicitud());
        var incompatible = new AsignacionMayorBateria().seleccionar(List.of(uno), pesada);
        // Assert
        assertEquals(uno, ganador.orElseThrow());
        assertTrue(incompatible.isEmpty());
    }
}
