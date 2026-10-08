package monferno.reto12;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.Mision;
import monferno.model.TipoDrone;
import monferno.reto3.strategy.EstrategiaAsignacion;
import monferno.reto3.validation.CadenaValidacion;
import monferno.reto3.validation.ValidadorDrone;

public class EstrategiaUrgenteExpress implements EstrategiaAsignacion {
    private static final Map<TipoDrone, Integer> ORDEN_RAPIDEZ = Map.of(
        TipoDrone.EXPRESS, 0, TipoDrone.MINI, 1, TipoDrone.CARGO, 2);
    private final ValidadorDrone validador = new CadenaValidacion();

    @Override
    public Optional<Drone> seleccionar(List<Drone> flota, Mision mision) {
        return flota.stream().filter(drone -> validador.validar(drone, mision))
            .min(Comparator.comparingInt((Drone drone) -> ORDEN_RAPIDEZ.get(drone.tipo()))
                .thenComparing(Comparator.comparingInt(Drone::bateria).reversed()).thenComparing(Drone::id));
    }
}
