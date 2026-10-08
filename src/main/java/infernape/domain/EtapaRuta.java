package infernape.domain;

import java.util.Objects;

public record EtapaRuta(PuntoRuta origen, PuntoRuta destino, double distanciaKm) {
    public EtapaRuta {
        Objects.requireNonNull(origen);
        Objects.requireNonNull(destino);
        Validaciones.positivoFinito(distanciaKm);
        if (origen.mismoLugar(destino)) {
            throw new IllegalArgumentException("Una etapa necesita puntos distintos");
        }
    }
}
