package src.main.java.reto3.strategy;

import src.main.java.model.Drone;

import java.util.List;
import java.util.Optional;

public interface EstrategiaAsignacion {

    Optional<Drone> seleccionar(List<Drone> flota);
}
