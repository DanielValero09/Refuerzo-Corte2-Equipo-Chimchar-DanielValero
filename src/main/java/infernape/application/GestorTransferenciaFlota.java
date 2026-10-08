package infernape.application;

import java.util.Objects;
import java.util.Optional;
import infernape.domain.DroneEnterprise;
import infernape.domain.Sede;

public final class GestorTransferenciaFlota {
    public Optional<DroneEnterprise> transferir(DroneEnterprise drone, Sede destino) {
        Objects.requireNonNull(drone);
        Objects.requireNonNull(destino);
        if (!drone.disponible() || drone.sede() == destino) return Optional.empty();
        return Optional.of(new DroneEnterprise(drone.id(), destino, drone.perfil(), drone.bateria(), true));
    }
}
