package infernape.application;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import infernape.domain.DroneEnterprise;
import infernape.domain.EstrategiaAsignacionEnterprise;
import infernape.domain.SolicitudAsignacion;

public final class AsignacionMayorBateria implements EstrategiaAsignacionEnterprise {
    @Override public Optional<DroneEnterprise> seleccionar(List<DroneEnterprise> drones, SolicitudAsignacion solicitud) {
        return drones.stream().filter(drone -> drone.sede() == solicitud.origen())
            .filter(drone -> drone.aptoPara(solicitud.pesoPaqueteGramos()))
            .min(Comparator.comparingInt(DroneEnterprise::bateria).reversed().thenComparing(DroneEnterprise::id));
    }
}
