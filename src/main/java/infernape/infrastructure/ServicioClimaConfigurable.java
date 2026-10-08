package infernape.infrastructure;

import infernape.domain.Sede;
import infernape.domain.ServicioClima;

public record ServicioClimaConfigurable(boolean apto) implements ServicioClima {
    @Override public boolean condicionesAptas(Sede origen, Sede destino) {
        java.util.Objects.requireNonNull(origen);
        java.util.Objects.requireNonNull(destino);
        return apto;
    }
}
