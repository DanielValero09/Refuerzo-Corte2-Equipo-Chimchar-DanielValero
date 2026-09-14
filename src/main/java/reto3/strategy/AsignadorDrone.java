package src.main.java.reto3.strategy;

import src.main.java.model.Drone;

import java.util.List;
import java.util.Optional;

public class AsignadorDrone {

    private EstrategiaAsignacion estrategia;

    public AsignadorDrone(EstrategiaAsignacion estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(EstrategiaAsignacion estrategia) {
        this.estrategia = estrategia;
    }

    public Optional<Drone> asignar(List<Drone> flota) {
        return estrategia.seleccionar(flota);
    }
}