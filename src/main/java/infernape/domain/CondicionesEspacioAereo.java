package infernape.domain;

import java.util.Objects;
import java.util.Optional;

public record CondicionesEspacioAereo(boolean autorizado, int alturaMaximaMetros, Optional<String> observacion) {
    public CondicionesEspacioAereo {
        observacion = Objects.requireNonNull(observacion).map(Validaciones::texto);
        if (alturaMaximaMetros < 0 || alturaMaximaMetros > LimitesOperacionRegulada.MAXIMO_URBANO_METROS
            || (autorizado && alturaMaximaMetros == 0)) {
            throw new IllegalArgumentException("Limite aereo invalido");
        }
    }
}
