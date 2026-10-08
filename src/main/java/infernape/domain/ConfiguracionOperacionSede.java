package infernape.domain;

import java.util.Objects;

public record ConfiguracionOperacionSede(Sede sede, double radioMaximoKm, LimitesOperacionRegulada limites) {
    public ConfiguracionOperacionSede {
        Objects.requireNonNull(sede);
        Objects.requireNonNull(limites);
        Validaciones.positivoFinito(radioMaximoKm);
        if (radioMaximoKm > limites.radioMaximoKm()) {
            throw new IllegalArgumentException("Configuracion local excede radio autorizado");
        }
    }

    public ConfiguracionOperacionSede conRadioMaximo(double radioKm) {
        return new ConfiguracionOperacionSede(sede, radioKm, limites);
    }

    public boolean permite(double distanciaDesdeSedeKm, int alturaMetros) {
        return Double.isFinite(distanciaDesdeSedeKm) && distanciaDesdeSedeKm > 0
            && distanciaDesdeSedeKm <= radioMaximoKm
            && alturaMetros > 0 && alturaMetros <= limites.alturaMaximaMetros();
    }
}
