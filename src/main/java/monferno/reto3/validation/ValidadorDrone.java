package monferno.reto3.validation;

import monferno.model.Drone;
import monferno.model.Mision;

@FunctionalInterface
public interface ValidadorDrone {
    boolean validar(Drone drone, Mision mision);
}
