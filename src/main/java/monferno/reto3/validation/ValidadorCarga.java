package monferno.reto3.validation;

import monferno.model.Drone;
import monferno.model.Mision;
import monferno.model.TipoDrone;

public class ValidadorCarga implements ValidadorDrone {
    @Override
    public boolean validar(Drone drone, Mision mision) {
        int peso = mision.pesoPaqueteGramos();
        return peso <= drone.tipo().capacidadGramos()
            && (drone.tipo() != TipoDrone.CARGO || peso >= 100);
    }
}
