package infernape.infrastructure;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import infernape.domain.*;

public record ServicioAerocivilSimulado(Duration demora, Optional<CondicionesEspacioAereo> respuesta)
    implements ServicioAerocivil {
    public ServicioAerocivilSimulado {
        Objects.requireNonNull(demora);
        Objects.requireNonNull(respuesta);
        if (demora.isNegative()) throw new IllegalArgumentException("Demora negativa");
    }

    @Override public Optional<CondicionesEspacioAereo> verificar(Sede origen, Sede destino, Duration plazo) {
        Objects.requireNonNull(origen);
        Objects.requireNonNull(destino);
        Objects.requireNonNull(plazo);
        if (plazo.isNegative() || plazo.isZero()) throw new IllegalArgumentException("Plazo debe ser positivo");
        return demora.compareTo(plazo) > 0 ? Optional.empty() : respuesta;
    }
}
