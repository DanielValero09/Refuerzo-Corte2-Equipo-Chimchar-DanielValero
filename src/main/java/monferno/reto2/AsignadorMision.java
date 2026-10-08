package monferno.reto2;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import monferno.model.Drone;
import monferno.model.Mision;
import monferno.reto3.GestorMisiones;

public class AsignadorMision {
    private final GestorMisiones gestor;
    private final Consumer<Mision> notificador;

    public AsignadorMision(GestorMisiones gestor, Consumer<Mision> notificador) {
        this.gestor = Objects.requireNonNull(gestor);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public Optional<Mision> asignar(List<Drone> flota, Mision mision) {
        return gestor.asignar(flota, mision).map(mision::conDrone);
    }

    private Mision notificar(Mision asignada) {
        notificador.accept(asignada);
        return asignada;
    }
}
