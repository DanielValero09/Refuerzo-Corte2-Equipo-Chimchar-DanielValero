package infernape.domain;

import java.time.Duration;
import java.util.Optional;

public interface ServicioAerocivil {
    // El adaptador devuelve vacio si no puede verificar dentro del plazo.
    Optional<CondicionesEspacioAereo> verificar(Sede origen, Sede destino, Duration plazo);
}
