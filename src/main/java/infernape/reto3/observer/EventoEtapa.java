package infernape.reto3.observer;

import java.util.Objects;
import infernape.domain.DroneEnterprise;
import infernape.domain.EtapaRuta;

public record EventoEtapa(EtapaRuta etapa, DroneEnterprise drone, EstadoEtapa estado) {
    public EventoEtapa {
        Objects.requireNonNull(etapa);
        Objects.requireNonNull(drone);
        Objects.requireNonNull(estado);
    }
}
