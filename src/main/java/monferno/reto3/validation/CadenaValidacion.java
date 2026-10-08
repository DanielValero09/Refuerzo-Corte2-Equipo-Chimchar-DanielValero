package monferno.reto3.validation;

import java.util.List;
import monferno.model.Drone;
import monferno.model.Mision;

public class CadenaValidacion implements ValidadorDrone {
    private final List<ValidadorDrone> validadores;

    public CadenaValidacion() {
        this(List.of(new ValidadorBateria(), new ValidadorDisponibilidad(), new ValidadorCarga()));
    }

    public CadenaValidacion(List<ValidadorDrone> validadores) {
        this.validadores = List.copyOf(validadores);
    }

    @Override
    public boolean validar(Drone drone, Mision mision) {
        return validadores.stream().allMatch(validador -> validador.validar(drone, mision));
    }
}
