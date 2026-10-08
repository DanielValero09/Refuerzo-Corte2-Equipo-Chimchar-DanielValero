package infernape.domain;

public record PerfilDrone(String codigo, int capacidadMaximaGramos, int pesoMinimoGramos) {
    public static final PerfilDrone MINI = new PerfilDrone("MINI", 500, 0);
    public static final PerfilDrone EXPRESS = new PerfilDrone("EXPRESS", 800, 0);
    public static final PerfilDrone CARGO = new PerfilDrone("CARGO", 2000, 100);

    public PerfilDrone {
        codigo = Validaciones.texto(codigo);
        if (capacidadMaximaGramos <= 0 || pesoMinimoGramos < 0 || pesoMinimoGramos > capacidadMaximaGramos) {
            throw new IllegalArgumentException("Limites de perfil invalidos");
        }
    }

    public boolean soporta(int pesoGramos) {
        return pesoGramos >= pesoMinimoGramos && pesoGramos <= capacidadMaximaGramos;
    }
}
