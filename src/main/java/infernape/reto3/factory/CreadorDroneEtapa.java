package infernape.reto3.factory;

import java.util.Optional;
import infernape.domain.DroneEnterprise;
import infernape.domain.EtapaRuta;
import infernape.domain.Sede;

public abstract class CreadorDroneEtapa {
    protected abstract DroneEnterprise crearDrone(String id, Sede sede, int bateria);

    public Optional<DroneEnterprise> crear(String id, Sede sede, int bateria, int pesoGramos, EtapaRuta etapa) {
        java.util.Objects.requireNonNull(etapa);
        DroneEnterprise candidato = crearDrone(id, sede, bateria);
        return candidato.aptoPara(pesoGramos) ? Optional.of(candidato) : Optional.empty();
    }
}
