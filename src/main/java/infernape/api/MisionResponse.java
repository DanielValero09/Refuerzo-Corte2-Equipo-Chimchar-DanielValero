package infernape.api;

import infernape.domain.*;

public record MisionResponse(String id, EstadoMision estado, DroneAsignado droneAsignado) {
    public record DroneAsignado(String id, String tipo, int bateria) { }
    public static MisionResponse desde(MisionRegistrada mision) {
        var drone = mision.drone();
        return new MisionResponse(mision.id(), mision.estado(), new DroneAsignado(drone.id(), drone.perfil().codigo(), drone.bateria()));
    }
}
