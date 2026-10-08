package infernape.domain;

@FunctionalInterface
public interface ServicioClima {
    boolean condicionesAptas(Sede origen, Sede destino);
}
