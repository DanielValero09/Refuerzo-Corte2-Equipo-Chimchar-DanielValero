package infernape.reto3;

import java.util.List;
import java.util.Optional;
import infernape.domain.*;
import infernape.reto3.ruta.*;
import infernape.reto3.strategy.*;
import org.junit.jupiter.api.Test;
import static infernape.support.DatosEnterprise.*;
import static org.junit.jupiter.api.Assertions.*;

class RutasYStrategyTest {
    @Test
    void compositeAnidadoConservaOrdenDistanciaYRecargas() {
        // Arrange
        Ruta parte = viaCarga();
        Ruta finalRuta = simple(UNAL, UNIANDES, 2);
        Ruta ruta = new RutaCompuesta(List.of(new RutaCompuesta(List.of(parte)), finalRuta));
        // Act
        double distancia = ruta.distanciaKm();
        // Assert
        assertEquals(8, distancia);
        assertEquals(1, ruta.numeroRecargas());
        assertEquals(List.of(parte.etapas().get(0), parte.etapas().get(1), finalRuta.etapas().get(0)), ruta.etapas());
        assertThrows(UnsupportedOperationException.class, () -> ruta.etapas().clear());
    }

    @Test
    void mismaInterfazAdmiteOptimizacionesDiferentes() {
        // Arrange
        Ruta directa = simple(ECI, UNAL, 10);
        Ruta recarga = viaCarga();
        List<Ruta> rutas = List.of(directa, recarga);
        // Act
        var corta = new PlanificadorRuta(new MenorDistanciaStrategy()).planificar(rutas);
        var sinRecargas = new PlanificadorRuta(new MenorNumeroRecargasStrategy()).planificar(rutas);
        // Assert
        assertEquals(Optional.of(recarga), corta);
        assertEquals(Optional.of(directa), sinRecargas);
    }

    @Test
    void empateEsDeterministaYNoDependeDelOrdenDeCandidatos() {
        // Arrange
        Ruta unal = simple(ECI, UNAL, 5);
        Ruta uniandes = simple(ECI, UNIANDES, 5);
        var estrategia = new MenorDistanciaStrategy();
        // Act
        var primero = estrategia.elegir(List.of(unal, uniandes));
        var segundo = estrategia.elegir(List.of(uniandes, unal));
        // Assert
        assertEquals(primero, segundo);
    }

    @Test
    void ausenciaDeRutasNoDevuelveNull() {
        // Arrange
        List<Ruta> candidatas = List.of();
        // Act
        var corta = new MenorDistanciaStrategy().elegir(candidatas);
        var recargas = new MenorNumeroRecargasStrategy().elegir(candidatas);
        // Assert
        assertTrue(corta.isEmpty());
        assertTrue(recargas.isEmpty());
    }

    @Test
    void compositeRechazaPartesVaciasODesconectadas() {
        // Arrange
        Ruta uno = simple(ECI, UNAL, 3);
        Ruta dos = simple(ECI, UNIANDES, 2);
        // Act
        var desconectada = assertThrows(IllegalArgumentException.class, () -> new RutaCompuesta(List.of(uno, dos)));
        // Assert
        assertFalse(desconectada.getMessage().isBlank());
        assertThrows(IllegalArgumentException.class, () -> new RutaCompuesta(List.of()));
    }

    @Test
    void etapaRechazaDistanciaInvalidaYMismoLugar() {
        // Arrange
        PuntoRuta inicio = ECI;
        // Act
        var repetida = assertThrows(IllegalArgumentException.class, () -> new EtapaRuta(inicio, inicio, 3));
        // Assert
        assertFalse(repetida.getMessage().isBlank());
        assertThrows(IllegalArgumentException.class, () -> new EtapaRuta(ECI, UNAL, Double.POSITIVE_INFINITY));
    }
}
