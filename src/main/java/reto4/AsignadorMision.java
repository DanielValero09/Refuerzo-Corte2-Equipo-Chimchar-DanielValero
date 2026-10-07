package src.main.java.reto4;

import java.util.Objects;
import src.main.java.model.Drone;
import src.main.java.model.Mission;

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
