package monferno.reto3;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.Mision;
import monferno.reto3.strategy.EstrategiaAsignacion;

public class GestorMisiones {
    private EstrategiaAsignacion estrategia;

    public GestorMisiones(EstrategiaAsignacion estrategia) {
        cambiarEstrategia(estrategia);
    }

    public void cambiarEstrategia(EstrategiaAsignacion estrategia) {
        this.estrategia = Objects.requireNonNull(estrategia);
    }

    public Optional<Drone> asignar(List<Drone> drones, Mision mision) {
        return estrategia.seleccionar(drones, mision);
    }
}
