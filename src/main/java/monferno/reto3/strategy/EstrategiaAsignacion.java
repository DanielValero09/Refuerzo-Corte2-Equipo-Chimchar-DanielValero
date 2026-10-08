package monferno.reto3.strategy;

import java.util.List;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.Mision;

@FunctionalInterface
public interface EstrategiaAsignacion {
    Optional<Drone> seleccionar(List<Drone> drones, Mision mision);
}
