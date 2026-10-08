package infernape.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

public record MetricasSede(double tasaExito, OptionalDouble tiempoPromedioEntrega,
                          Optional<String> droneMasUtilizado, double porcentajeUrgentes) {
    public MetricasSede {
        Objects.requireNonNull(tiempoPromedioEntrega);
        Objects.requireNonNull(droneMasUtilizado);
    }
}
