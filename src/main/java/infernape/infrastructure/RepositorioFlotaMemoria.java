package infernape.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import infernape.domain.DroneEnterprise;
import infernape.domain.RepositorioFlota;
import infernape.domain.Sede;

public final class RepositorioFlotaMemoria implements RepositorioFlota {
    private final List<DroneEnterprise> drones;

    public RepositorioFlotaMemoria(List<DroneEnterprise> drones) {
        this.drones = List.copyOf(drones);
        if (this.drones.stream().map(DroneEnterprise::id).collect(Collectors.toSet()).size() != this.drones.size()) {
            throw new IllegalArgumentException("IDs duplicados en flota");
        }
    }

    @Override public List<DroneEnterprise> findDisponibles(Sede sede) {
        return drones.stream().filter(drone -> drone.sede() == sede && drone.disponible()).toList();
    }
}
