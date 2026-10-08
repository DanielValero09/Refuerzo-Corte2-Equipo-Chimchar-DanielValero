package infernape.reto3;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import infernape.domain.EstacionCarga;
import infernape.domain.PuntoRuta;
import infernape.reto3.ruta.Ruta;
import infernape.reto3.ruta.RutaCompuesta;
import infernape.reto3.strategy.MenorDistanciaStrategy;
import infernape.reto3.strategy.PlanificadorRuta;
import static infernape.support.DatosEnterprise.*;
import static org.junit.jupiter.api.Assertions.*;

class PlanificadorRutaHotfixTest {
    @Test
    void descartaLaRutaCortaSiSuEstacionDeCargaNoEstaDisponible() {
        // Arrange
        Ruta directa = simple(ECI, UNAL, 10);
        PlanificadorRuta planificador = new PlanificadorRuta(new MenorDistanciaStrategy());
        // Act
        Optional<Ruta> resultado = planificador.planificar(List.of(viaEstacionCerrada(), directa));
        // Assert
        assertEquals(Optional.of(directa), resultado);
    }

    @Test
    void devuelveVacioSiTodasLasRutasRequierenUnaEstacionCerrada() {
        // Arrange
        PlanificadorRuta planificador = new PlanificadorRuta(new MenorDistanciaStrategy());
        // Act
        Optional<Ruta> resultado = planificador.planificar(List.of(viaEstacionCerrada()));
        // Assert
        assertTrue(resultado.isEmpty());
    }

    private static Ruta viaEstacionCerrada() {
        PuntoRuta cerrada = PuntoRuta.carga(new EstacionCarga("C-116", "Estacion calle 116", false));
        return new RutaCompuesta(List.of(simple(ECI, cerrada, 3), simple(cerrada, UNAL, 3)));
    }
}
