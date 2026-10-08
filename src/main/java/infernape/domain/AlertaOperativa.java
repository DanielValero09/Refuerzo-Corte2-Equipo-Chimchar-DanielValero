package infernape.domain;

import java.util.Objects;

public record AlertaOperativa(Sede sede, String mensaje) {
    public AlertaOperativa {
        Objects.requireNonNull(sede);
        mensaje = Validaciones.texto(mensaje);
    }
}
