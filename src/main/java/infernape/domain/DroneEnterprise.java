package infernape.domain;

import java.util.Objects;

public record DroneEnterprise(String id, Sede sede, PerfilDrone perfil, int bateria, boolean disponible) {
    public DroneEnterprise {
        id = Validaciones.texto(id);
        Objects.requireNonNull(sede);
        Objects.requireNonNull(perfil);
        if (bateria < 0 || bateria > 100) {
            throw new IllegalArgumentException("Bateria fuera de 0-100");
        }
    }

    public boolean aptoPara(int pesoGramos) {
        return disponible && bateria >= 30 && perfil.soporta(pesoGramos);
    }

    public DroneEnterprise enMision() {
        return new DroneEnterprise(id, sede, perfil, bateria, false);
    }
}
