package monferno.support;

import java.time.LocalDateTime;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.EstadoDrone;
import monferno.model.EstadoMision;
import monferno.model.Mision;
import monferno.model.Prioridad;
import monferno.model.TipoDrone;

public final class DatosMonferno {
    public static final LocalDateTime AHORA = LocalDateTime.of(2026, 10, 7, 12, 0);

    private DatosMonferno() {
    }

    public static Drone drone(String id, TipoDrone tipo, int bateria, int uso) {
        return new Drone(id, tipo, bateria, true, EstadoDrone.DISPONIBLE, uso);
    }

    public static Mision pendiente(int peso) {
        return Mision.pendiente("M-01", "Biblioteca", peso, Prioridad.NORMAL, AHORA);
    }

    public static Mision entregada(String id, Drone drone, LocalDateTime creada, LocalDateTime entregada) {
        return new Mision(id, Optional.of(drone), "Biblioteca", 100, Prioridad.NORMAL,
            EstadoMision.ENTREGADA, creada, Optional.of(entregada));
    }

    public static Mision fallida(String id, Drone drone) {
        return new Mision(id, Optional.of(drone), "Biblioteca", 100, Prioridad.NORMAL,
            EstadoMision.FALLIDA, AHORA, Optional.empty());
    }
}
