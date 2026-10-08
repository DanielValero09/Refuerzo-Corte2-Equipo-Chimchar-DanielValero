package infernape.reto3.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import infernape.reto3.ruta.Ruta;

public final class MenorNumeroRecargasStrategy implements EstrategiaOptimizacionRuta {
    @Override public Optional<Ruta> elegir(List<Ruta> candidatas) {
        return candidatas.stream().min(Comparator.comparingLong(Ruta::numeroRecargas)
            .thenComparingDouble(Ruta::distanciaKm).thenComparing(Ruta::clave));
    }
}
