package infernape.domain;

public record EstacionCarga(String id, String nombre, boolean disponible) {
    public EstacionCarga {
        id = Validaciones.texto(id);
        nombre = Validaciones.texto(nombre);
    }
}
