package infernape.reto3.ruta;

import java.util.List;
import java.util.Objects;
import infernape.domain.EtapaRuta;

public record RutaSimple(EtapaRuta etapa) implements Ruta {
    public RutaSimple {
        Objects.requireNonNull(etapa);
    }

    @Override public double distanciaKm() { return etapa.distanciaKm(); }
    @Override public List<EtapaRuta> etapas() { return List.of(etapa); }
}
