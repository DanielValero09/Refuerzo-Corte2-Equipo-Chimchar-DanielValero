package reto4;

import java.util.Objects;
import model.Drone;
import model.Mission;

public class AsignadorMision {

    public Mission asignar(Mission mission, Drone drone) {
        Objects.requireNonNull(mission, "La mision es obligatoria");
        Objects.requireNonNull(drone, "El drone es obligatorio");

        return new Mission(
                mission.id(), drone, mission.origen(), mission.destino(),
                mission.tipoCarga(), mission.estado(), mission.prioridad(),
                mission.notas(), mission.horaMaximaEntrega()
        );
    }
}
