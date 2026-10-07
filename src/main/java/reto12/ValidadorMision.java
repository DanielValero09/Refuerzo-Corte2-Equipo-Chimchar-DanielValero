package reto12;

import java.util.Set;
import model.Drone;

public class ValidadorMision {
    private static final Set<String> DESTINOS_VALIDOS = Set.of(
        "Bloque A", "Bloque B", "Bloque C", "Bloque D", "Biblioteca"
    );

    public boolean tieneBateriaSuficiente(Drone drone) {
        return drone.battery() >= 30;
    }

    public void validarDestino(String destino) {
        if (destino == null || !DESTINOS_VALIDOS.contains(destino)) {
            throw new DestinoInvalidoException("Destino inválido: " + destino);
        }
    }

    public boolean droneEstaDisponible(Drone drone) {
        return drone.available();
    }
}
