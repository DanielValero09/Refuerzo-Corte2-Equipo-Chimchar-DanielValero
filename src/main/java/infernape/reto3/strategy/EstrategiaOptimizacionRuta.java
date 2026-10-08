package infernape.reto3.strategy;

import java.util.List;
import java.util.Optional;
import infernape.reto3.ruta.Ruta;

@FunctionalInterface
public interface EstrategiaOptimizacionRuta {
    Optional<Ruta> elegir(List<Ruta> candidatas);
}
