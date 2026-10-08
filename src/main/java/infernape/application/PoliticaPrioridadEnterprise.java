package infernape.application;

import infernape.domain.*;
import java.util.*;

public final class PoliticaPrioridadEnterprise implements EstrategiaAsignacionEnterprise {
    @Override public Optional<DroneEnterprise> seleccionar(List<DroneEnterprise> flota, SolicitudAsignacion solicitud) {
        var orden = Comparator.comparing((DroneEnterprise drone) ->
            solicitud.prioridad() == Prioridad.URGENTE && drone.perfil().equals(PerfilDrone.EXPRESS))
            .thenComparingInt(DroneEnterprise::bateria)
            .thenComparing(DroneEnterprise::id, Comparator.reverseOrder());
        return flota.stream().filter(drone -> drone.sede() == solicitud.origen())
            .filter(drone -> drone.aptoPara(solicitud.pesoPaqueteGramos())).max(orden);
    }
}
