package infernape.domain;

import java.util.Objects;

public final class ReglaOperacionException extends RuntimeException {
    private final CodigoRechazo codigo;
    public ReglaOperacionException(CodigoRechazo codigo) {
        super(Objects.requireNonNull(codigo).mensaje());
        this.codigo = codigo;
    }
    public CodigoRechazo codigo() { return codigo; }
}
