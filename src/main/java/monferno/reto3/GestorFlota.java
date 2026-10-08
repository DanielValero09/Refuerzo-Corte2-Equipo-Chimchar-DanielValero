package monferno.reto3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import monferno.model.Drone;
import monferno.model.EstadoDrone;
import monferno.reto3.observer.ObservadorDrone;

public class GestorFlota {
    private final Map<String, Drone> drones;
    private final List<ObservadorDrone> observadores = new ArrayList<>();

    public GestorFlota(List<Drone> flota) {
        drones = flota.stream().collect(Collectors.toMap(Drone::id, drone -> drone,
            (primero, segundo) -> { throw new IllegalArgumentException("ID de drone duplicado"); }, LinkedHashMap::new));
    }

    public void suscribir(ObservadorDrone observador) {
        Objects.requireNonNull(observador);
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void desuscribir(ObservadorDrone observador) {
        observadores.remove(observador);
    }

    public Optional<Drone> buscar(String id) {
        return Optional.ofNullable(drones.get(id));
    }

    public List<Drone> listar() {
        return List.copyOf(drones.values());
    }

    public Drone cambiarEstado(String id, EstadoDrone nuevoEstado) {
        Objects.requireNonNull(nuevoEstado);
        Drone anterior = buscar(id).orElseThrow(() -> new IllegalArgumentException("Drone inexistente: " + id));
        if (anterior.estado() == nuevoEstado) {
            return anterior;
        }
        Drone actualizado = new Drone(anterior.id(), anterior.tipo(), anterior.bateria(),
            nuevoEstado == EstadoDrone.DISPONIBLE, nuevoEstado, anterior.misionesCompletadas());
        drones.put(id, actualizado);
        List.copyOf(observadores).forEach(observador -> observador.onEstadoCambiado(actualizado, nuevoEstado));
        return actualizado;
    }
}
