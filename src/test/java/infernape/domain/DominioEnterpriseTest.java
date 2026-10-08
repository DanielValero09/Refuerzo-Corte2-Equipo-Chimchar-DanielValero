package infernape.domain;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static infernape.support.DatosEnterprise.*;
import static org.junit.jupiter.api.Assertions.*;

class DominioEnterpriseTest {
    @ParameterizedTest
    @MethodSource("invalidos")
    void invariantesImpidenDatosNoRepresentables(Supplier<?> crear) {
        // Arrange
        Supplier<?> entrada = crear;
        // Act
        var error = assertThrows(IllegalArgumentException.class, entrada::get);
        // Assert
        assertFalse(error.getMessage().isBlank());
    }

    static Stream<Supplier<?>> invalidos() {
        return Stream.of(() -> drone(" ", Sede.ECI, 30, true), () -> drone("D", Sede.ECI, -1, true),
            () -> drone("D", Sede.ECI, 101, true), () -> new PerfilDrone("TEST", 0, 0),
            () -> new PerfilDrone("TEST", 500, -1), () -> new PerfilDrone("TEST", 500, 501),
            () -> entregada("M", Sede.ECI, "D", Prioridad.NORMAL, 0),
            () -> entregada("M", Sede.ECI, "D", Prioridad.NORMAL, Double.NaN),
            () -> new SolicitudAsignacion("M", Sede.ECI, Sede.UNAL, 0, Prioridad.NORMAL),
            () -> new MisionEnterprise("M", Sede.ECI, Optional.empty(), EstadoMision.ENTREGADA, Prioridad.NORMAL, OptionalDouble.of(10)),
            () -> new MisionEnterprise("M", Sede.ECI, Optional.of("D"), EstadoMision.ENTREGADA, Prioridad.NORMAL, OptionalDouble.empty()),
            () -> new MisionEnterprise("M", Sede.ECI, Optional.of("D"), EstadoMision.FALLIDA, Prioridad.NORMAL, OptionalDouble.of(10)));
    }

    @Test
    void perfilConfiguradoAmpliaCapacidadSinInventarUnTipoOficial() {
        // Arrange
        PerfilDrone perfilDePrueba = new PerfilDrone("PERFIL_DE_PRUEBA", 2500, 1);
        DroneEnterprise drone = new DroneEnterprise("D-TEST", Sede.UNAL, perfilDePrueba, 30, true);
        // Act
        boolean apto = drone.aptoPara(2500);
        // Assert
        assertTrue(apto);
        assertFalse(drone.aptoPara(2501));
        assertFalse(drone.enMision().aptoPara(100));
        assertTrue(drone.disponible());
    }

    @Test
    void identidadDeEstacionNoDependeDeUnSnapshotDeDisponibilidad() {
        // Arrange
        PuntoRuta anterior = PuntoRuta.carga(new EstacionCarga("C-116", "116", true));
        PuntoRuta actual = PuntoRuta.carga(new EstacionCarga("C-116", "116", false));
        // Act
        boolean mismoLugar = anterior.mismoLugar(actual);
        // Assert
        assertTrue(mismoLugar);
        assertFalse(actual.mismoLugar(PuntoRuta.campus(Sede.UNAL)));
    }
}
