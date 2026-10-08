package infernape.domain;

import java.util.Objects;

public record SolicitudAsignacion(String id, Sede origen, Sede destino, int pesoPaqueteGramos, Prioridad prioridad) {
    public SolicitudAsignacion {
        id = Validaciones.texto(id);
        Objects.requireNonNull(origen);
        Objects.requireNonNull(destino);
        Objects.requireNonNull(prioridad);
        if (pesoPaqueteGramos < 1) {
            throw new IllegalArgumentException("Peso debe ser positivo");
        }
    }
}
