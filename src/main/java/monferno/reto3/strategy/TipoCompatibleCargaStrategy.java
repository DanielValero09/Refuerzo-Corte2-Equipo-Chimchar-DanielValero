package monferno.reto3.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.Mision;
import monferno.reto3.validation.CadenaValidacion;
import monferno.reto3.validation.ValidadorDrone;

public class TipoCompatibleCargaStrategy implements EstrategiaAsignacion {
    private final ValidadorDrone validador;

    public TipoCompatibleCargaStrategy() {
        this(new CadenaValidacion());
    }

    public TipoCompatibleCargaStrategy(ValidadorDrone validador) {
        this.validador = Objects.requireNonNull(validador);
    }

    @Override
    public Optional<Drone> seleccionar(List<Drone> drones, Mision mision) {
        return drones.stream().filter(drone -> validador.validar(drone, mision))
            .min(Comparator.comparingInt((Drone drone) -> drone.tipo().capacidadGramos()).thenComparing(Drone::id));
    }
}
