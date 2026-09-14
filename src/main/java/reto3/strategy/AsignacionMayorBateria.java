package src.main.java.reto3.strategy;

import src.main.java.model.Drone;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AsignacionMayorBateria implements EstrategiaAsignacion {

    @Override
    public Optional<Drone> seleccionar(List<Drone> flota) {

        return flota.stream()
                .filter(Drone::available)
                .filter(drone -> drone.battery() >= 30)
                .max(Comparator.comparingInt(Drone::battery));
    }
}
