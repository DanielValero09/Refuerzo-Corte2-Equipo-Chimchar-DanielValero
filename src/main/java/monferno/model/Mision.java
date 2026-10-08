package monferno.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public record Mision(String id, Optional<Drone> drone, String destino, int pesoPaqueteGramos,
                     Prioridad prioridad, EstadoMision estado, LocalDateTime creadaEn,
                     Optional<LocalDateTime> entregadaEn) {
    public Mision {
        Objects.requireNonNull(id, "El ID es obligatorio");
        Objects.requireNonNull(drone, "Use Optional para la asignacion");
        Objects.requireNonNull(destino, "El destino es obligatorio");
        Objects.requireNonNull(prioridad, "La prioridad es obligatoria");
        Objects.requireNonNull(estado, "El estado es obligatorio");
        Objects.requireNonNull(creadaEn, "La creacion es obligatoria");
        Objects.requireNonNull(entregadaEn, "Use Optional para la entrega");
        if (id.isBlank() || destino.isBlank() || pesoPaqueteGramos < 0) {
            throw new IllegalArgumentException("ID, destino o peso invalidos");
        }
        if (estado == EstadoMision.ENTREGADA && (drone.isEmpty() || entregadaEn.isEmpty())) {
            throw new IllegalArgumentException("Una entrega requiere drone e instante de entrega");
        }
        if (entregadaEn.isPresent() && (estado != EstadoMision.ENTREGADA || entregadaEn.orElseThrow().isBefore(creadaEn))) {
            throw new IllegalArgumentException("El instante de entrega no corresponde al estado o creacion");
        }
    }

    public static Mision pendiente(String id, String destino, int peso, Prioridad prioridad, LocalDateTime creadaEn) {
        return new Mision(id, Optional.empty(), destino, peso, prioridad,
            EstadoMision.PENDIENTE, creadaEn, Optional.empty());
    }

    public Mision conDrone(Drone asignado) {
        return new Mision(id, Optional.of(asignado), destino, pesoPaqueteGramos,
            prioridad, estado, creadaEn, entregadaEn);
    }
}
