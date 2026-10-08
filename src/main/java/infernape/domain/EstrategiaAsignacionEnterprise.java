package infernape.domain;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface EstrategiaAsignacionEnterprise {
    Optional<DroneEnterprise> seleccionar(List<DroneEnterprise> drones, SolicitudAsignacion solicitud);
}
