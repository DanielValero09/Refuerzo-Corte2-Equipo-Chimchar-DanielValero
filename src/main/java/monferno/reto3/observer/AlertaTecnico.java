package monferno.reto3.observer;

import java.util.ArrayList;
import java.util.List;
import monferno.model.Drone;
import monferno.model.EstadoDrone;

public class AlertaTecnico implements ObservadorDrone {
    private final List<String> alertas = new ArrayList<>();

    @Override
    public void onEstadoCambiado(Drone drone, EstadoDrone nuevoEstado) {
        if (nuevoEstado == EstadoDrone.FALLO) {
            alertas.add("FALLO: " + drone.id() + " requiere revision del tecnico de mantenimiento");
        }
    }

    public List<String> alertas() {
        return List.copyOf(alertas);
    }
}
