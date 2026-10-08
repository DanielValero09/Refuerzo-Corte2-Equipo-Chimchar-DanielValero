package monferno.model;

public enum TipoDrone {
    MINI(500),
    CARGO(2000),
    EXPRESS(800);

    private final int capacidadGramos;

    TipoDrone(int capacidadGramos) {
        this.capacidadGramos = capacidadGramos;
    }

    public int capacidadGramos() {
        return capacidadGramos;
    }
}
