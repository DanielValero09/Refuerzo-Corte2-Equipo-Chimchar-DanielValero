package infernape.reto3.strategy;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import infernape.domain.EstacionCarga;
import infernape.domain.PuntoRuta;
import infernape.reto3.ruta.Ruta;

public final class PlanificadorRuta {
    private final EstrategiaOptimizacionRuta estrategia;

    public PlanificadorRuta(EstrategiaOptimizacionRuta estrategia) {
        this.estrategia = Objects.requireNonNull(estrategia);
    }

    public Optional<Ruta> planificar(List<Ruta> candidatas) {
        List<Ruta> operables = List.copyOf(candidatas).stream().filter(this::operable).toList();
        return estrategia.elegir(operables);
    }

    private boolean operable(Ruta ruta) {
        return ruta.etapas().stream()
            .allMatch(etapa -> disponible(etapa.origen()) && disponible(etapa.destino()));
    }

    private boolean disponible(PuntoRuta punto) {
        return punto.estacion().map(EstacionCarga::disponible).orElse(true);
    }
}
