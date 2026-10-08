package monferno.reto3.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.Mision;
import monferno.reto3.validation.CadenaValidacion;
import monferno.reto3.validation.ValidadorDrone;

public class MayorBateriaStrategy implements EstrategiaAsignacion {
    private final ValidadorDrone validador;

    public MayorBateriaStrategy() {
        this(new CadenaValidacion());
    }

    public MayorBateriaStrategy(ValidadorDrone validador) {
        this.validador = Objects.requireNonNull(validador);
    }

    @Override
    public Optional<Drone> seleccionar(List<Drone> drones, Mision mision) {
        return drones.stream().filter(drone -> validador.validar(drone, mision))
            .max(Comparator.comparingInt(Drone::bateria).thenComparing(Drone::id, Comparator.reverseOrder()));
    }
}
