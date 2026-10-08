package monferno.reto3.observer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.EstadoDrone;

public class PanelOperador implements ObservadorDrone {
    private final Map<String, EstadoDrone> estados = new LinkedHashMap<>();

    @Override
    public void onEstadoCambiado(Drone drone, EstadoDrone nuevoEstado) {
        estados.put(drone.id(), nuevoEstado);
    }

    public Optional<EstadoDrone> estadoDe(String id) {
        return Optional.ofNullable(estados.get(id));
    }
}
