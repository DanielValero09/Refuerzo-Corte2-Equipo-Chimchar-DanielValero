package infernape.domain;

public record LimitesOperacionRegulada(double radioMaximoKm, int alturaMaximaMetros) {
    public static final int MAXIMO_URBANO_METROS = 120;

    public LimitesOperacionRegulada {
        Validaciones.positivoFinito(radioMaximoKm);
        if (alturaMaximaMetros < 1 || alturaMaximaMetros > MAXIMO_URBANO_METROS) {
            throw new IllegalArgumentException("Altura regulatoria fuera de 1-120 metros");
        }
    }
}
