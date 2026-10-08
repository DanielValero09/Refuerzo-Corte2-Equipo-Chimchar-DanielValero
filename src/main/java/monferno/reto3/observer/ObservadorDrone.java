package monferno.reto3.observer;

import monferno.model.Drone;
import monferno.model.EstadoDrone;

@FunctionalInterface
public interface ObservadorDrone {
    void onEstadoCambiado(Drone drone, EstadoDrone nuevoEstado);
}
