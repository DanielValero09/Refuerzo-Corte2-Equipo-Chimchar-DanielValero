package infernape.domain;

import java.util.Objects;
import java.util.Optional;

public record PuntoRuta(String id, Optional<EstacionCarga> estacion) {
    public PuntoRuta {
        id = Validaciones.texto(id);
        Objects.requireNonNull(estacion);
    }

    public static PuntoRuta campus(Sede sede) {
        return new PuntoRuta("CAMPUS:" + sede.name(), Optional.empty());
    }

    public static PuntoRuta carga(EstacionCarga estacion) {
        return new PuntoRuta("CARGA:" + estacion.id(), Optional.of(estacion));
    }

    public boolean mismoLugar(PuntoRuta otro) {
        return id.equals(otro.id);
    }
}
