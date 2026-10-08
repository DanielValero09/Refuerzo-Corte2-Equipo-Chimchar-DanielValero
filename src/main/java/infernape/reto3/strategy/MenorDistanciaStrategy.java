package infernape.reto3.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import infernape.reto3.ruta.Ruta;

public final class MenorDistanciaStrategy implements EstrategiaOptimizacionRuta {
    @Override public Optional<Ruta> elegir(List<Ruta> candidatas) {
        return candidatas.stream().min(Comparator.comparingDouble(Ruta::distanciaKm)
            .thenComparingLong(Ruta::numeroRecargas).thenComparing(Ruta::clave));
    }
}
