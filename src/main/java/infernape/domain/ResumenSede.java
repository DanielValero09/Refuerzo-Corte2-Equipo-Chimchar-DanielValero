package infernape.domain;

public record ResumenSede(long drones, long disponibles, long misionesActivas, long alertas) {
    public ResumenSede {
        if (drones < 0 || disponibles < 0 || disponibles > drones || misionesActivas < 0 || alertas < 0) {
            throw new IllegalArgumentException("Contadores de sede inconsistentes");
        }
    }

    public EstadoGeneralRed estado() {
        if (alertas > 0) return EstadoGeneralRed.CON_ALERTAS;
        return drones == 0 && misionesActivas == 0 ? EstadoGeneralRed.SIN_ACTIVIDAD : EstadoGeneralRed.OPERATIVA;
    }
}
