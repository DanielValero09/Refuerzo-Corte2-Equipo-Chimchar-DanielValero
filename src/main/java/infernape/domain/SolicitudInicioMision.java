package infernape.domain;

import java.util.Objects;

/** Distancia radial y altura propuestas por el simulador, no geografia real. */
public record SolicitudInicioMision(SolicitudAsignacion asignacion, double distanciaPlanificadaKm, int alturaMetros) {
    public SolicitudInicioMision {
        Objects.requireNonNull(asignacion);
        Validaciones.positivoFinito(distanciaPlanificadaKm);
        if (alturaMetros <= 0 || asignacion.origen() == asignacion.destino()) {
            throw new IllegalArgumentException("La solicitud inter-sede requiere altura positiva y sedes distintas");
        }
    }
}
