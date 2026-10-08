package monferno.reto3.validation;

import monferno.model.Drone;
import monferno.model.EstadoDrone;
import monferno.model.Mision;

public class ValidadorDisponibilidad implements ValidadorDrone {
    @Override
    public boolean validar(Drone drone, Mision mision) {
        return drone.disponible() && drone.estado() == EstadoDrone.DISPONIBLE;
    }
}
