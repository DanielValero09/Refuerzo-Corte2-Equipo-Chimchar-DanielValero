package monferno.reto3.validation;

import monferno.model.Drone;
import monferno.model.Mision;

public class ValidadorBateria implements ValidadorDrone {
    @Override
    public boolean validar(Drone drone, Mision mision) {
        return drone.bateria() >= 30;
    }
}
