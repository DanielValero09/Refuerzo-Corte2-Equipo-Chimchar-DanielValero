package infernape.domain;

@FunctionalInterface
public interface RepositorioEstadoSede {
    boolean activa(Sede sede);
}
