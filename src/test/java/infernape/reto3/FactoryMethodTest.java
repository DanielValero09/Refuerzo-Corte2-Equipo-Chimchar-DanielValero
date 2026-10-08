package infernape.reto3;

import java.util.Optional;
import java.util.stream.Stream;
import infernape.domain.*;
import infernape.reto3.factory.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static infernape.support.DatosEnterprise.*;
import static org.junit.jupiter.api.Assertions.*;

class FactoryMethodTest {
    @ParameterizedTest
    @MethodSource("perfiles")
    void creadorConcretoProduceDroneAptoParaLaEtapa(CreadorDroneEtapa creador, PerfilDrone esperado, int peso) {
        // Arrange
        EtapaRuta etapa = viaCarga().etapas().get(0);
        // Act
        DroneEnterprise drone = creador.crear("D-E-01", Sede.ECI, 30, peso, etapa).orElseThrow();
        // Assert
        assertEquals(esperado, drone.perfil());
        assertTrue(drone.aptoPara(peso));
        assertEquals(Sede.ECI, drone.sede());
    }

    static Stream<Arguments> perfiles() {
        return Stream.of(Arguments.of(new CreadorMini(), PerfilDrone.MINI, 500),
            Arguments.of(new CreadorExpress(), PerfilDrone.EXPRESS, 800), Arguments.of(new CreadorCargo(), PerfilDrone.CARGO, 2000));
    }

    @ParameterizedTest
    @MethodSource("rechazos")
    void factoryRespetaBateriaCapacidadYMinimoCargo(CreadorDroneEtapa creador, int bateria, int peso) {
        // Arrange
        EtapaRuta etapa = viaCarga().etapas().get(0);
        // Act
        Optional<DroneEnterprise> resultado = creador.crear("D-01", Sede.ECI, bateria, peso, etapa);
        // Assert
        assertTrue(resultado.isEmpty());
    }

    static Stream<Arguments> rechazos() {
        return Stream.of(Arguments.of(new CreadorMini(), 29, 200), Arguments.of(new CreadorMini(), 90, 501),
            Arguments.of(new CreadorExpress(), 90, 801), Arguments.of(new CreadorCargo(), 90, 99));
    }
}
