package infernape.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

public record MisionEnterprise(String id, Sede sede, Optional<String> droneId,
                              EstadoMision estado, Prioridad prioridad, OptionalDouble tiempoEntregaMinutos) {
    public MisionEnterprise {
        id = Validaciones.texto(id);
        Objects.requireNonNull(sede);
        droneId = Objects.requireNonNull(droneId).map(Validaciones::texto);
        Objects.requireNonNull(estado);
        Objects.requireNonNull(prioridad);
        Objects.requireNonNull(tiempoEntregaMinutos);
        validarEntrega(estado, droneId, tiempoEntregaMinutos);
    }

    private static void validarEntrega(EstadoMision estado, Optional<String> droneId, OptionalDouble minutos) {
        if (estado == EstadoMision.ENTREGADA) {
            if (droneId.isEmpty() || minutos.isEmpty()) {
                throw new IllegalArgumentException("Entrega requiere drone y duracion");
            }
            Validaciones.positivoFinito(minutos.orElseThrow());
        } else if (minutos.isPresent()) {
            throw new IllegalArgumentException("Solo ENTREGADA puede registrar duracion");
        }
    }
}
