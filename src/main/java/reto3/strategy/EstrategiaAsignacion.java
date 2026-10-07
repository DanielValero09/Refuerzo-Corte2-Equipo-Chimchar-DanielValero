package reto3.strategy;

import model.Drone;

import java.util.List;
import java.util.Optional;

public interface EstrategiaAsignacion {

    Optional<Drone> seleccionar(List<Drone> flota);
}
