package infernape.reto3.factory;

import infernape.domain.PerfilDrone;
import infernape.domain.DroneEnterprise;
import infernape.domain.Sede;

public final class CreadorExpress extends CreadorDroneEtapa {
    @Override protected DroneEnterprise crearDrone(String id, Sede sede, int bateria) {
        return new DroneEnterprise(id, sede, PerfilDrone.EXPRESS, bateria, true);
    }
}
