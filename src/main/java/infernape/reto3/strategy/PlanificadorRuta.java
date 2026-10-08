package infernape.reto3.strategy;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import infernape.reto3.ruta.Ruta;

public final class PlanificadorRuta {
    private final EstrategiaOptimizacionRuta estrategia;

    public PlanificadorRuta(EstrategiaOptimizacionRuta estrategia) {
        this.estrategia = Objects.requireNonNull(estrategia);
    }

    public Optional<Ruta> planificar(List<Ruta> candidatas) {
        return estrategia.elegir(List.copyOf(candidatas));
    }
}
