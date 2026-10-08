package infernape.domain;

import java.util.Objects;

public record MisionRegistrada(String id, Sede origen, Sede destino, int pesoPaquete,
                               Prioridad prioridad, DroneEnterprise drone, EstadoMision estado) {
    public MisionRegistrada {
        id = Validaciones.texto(id);
        Objects.requireNonNull(origen);
        Objects.requireNonNull(destino);
        Objects.requireNonNull(prioridad);
        Objects.requireNonNull(drone);
        Objects.requireNonNull(estado);
        if (pesoPaquete < 1) throw new IllegalArgumentException("Peso debe ser positivo");
    }
}
