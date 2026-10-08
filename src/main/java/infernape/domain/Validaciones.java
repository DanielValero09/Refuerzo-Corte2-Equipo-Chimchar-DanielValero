package infernape.domain;

import java.util.Objects;

public final class Validaciones {
    private Validaciones() { }

    public static String texto(String valor) {
        String limpio = Objects.requireNonNull(valor).strip();
        if (limpio.isEmpty()) {
            throw new IllegalArgumentException("Texto obligatorio");
        }
        return limpio;
    }

    public static void positivoFinito(double valor) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException("Valor positivo y finito requerido");
        }
    }
}
