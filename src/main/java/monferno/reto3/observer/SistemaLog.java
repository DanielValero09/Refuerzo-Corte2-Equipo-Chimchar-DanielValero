package monferno.reto3.observer;

import java.util.ArrayList;
import java.util.List;
import monferno.model.Drone;
import monferno.model.EstadoDrone;

public class SistemaLog implements ObservadorDrone {
    private final List<String> registros = new ArrayList<>();

    @Override
    public void onEstadoCambiado(Drone drone, EstadoDrone nuevoEstado) {
        registros.add(drone.id() + " -> " + nuevoEstado);
    }

    public List<String> registros() {
        return List.copyOf(registros);
    }
}
