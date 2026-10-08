package monferno.model;

import java.util.Objects;

public record Drone(String id, TipoDrone tipo, int bateria, boolean disponible,
                    EstadoDrone estado, int misionesCompletadas) {
    public Drone {
        Objects.requireNonNull(id, "El ID es obligatorio");
        Objects.requireNonNull(tipo, "El tipo es obligatorio");
        Objects.requireNonNull(estado, "El estado es obligatorio");
        if (id.isBlank() || bateria < 0 || bateria > 100 || misionesCompletadas < 0) {
            throw new IllegalArgumentException("ID, bateria o uso acumulado invalidos");
        }
    }

    public Drone(String id, TipoDrone tipo, int bateria, boolean disponible, EstadoDrone estado) {
        this(id, tipo, bateria, disponible, estado, 0);
    }
}
