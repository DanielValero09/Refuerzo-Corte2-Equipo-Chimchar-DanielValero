package infernape.domain;

import java.util.Arrays;
import java.util.Map;

public record ResumenRed(Map<Sede, ResumenSede> sedes) {
    public ResumenRed {
        sedes = Map.copyOf(sedes);
        if (!sedes.keySet().containsAll(Arrays.asList(Sede.values()))) {
            throw new IllegalArgumentException("Resumen requiere las cuatro sedes");
        }
    }

    public EstadoGeneralRed estado() {
        if (sedes.values().stream().anyMatch(s -> s.estado() == EstadoGeneralRed.CON_ALERTAS)) {
            return EstadoGeneralRed.CON_ALERTAS;
        }
        return sedes.values().stream().allMatch(s -> s.estado() == EstadoGeneralRed.SIN_ACTIVIDAD)
            ? EstadoGeneralRed.SIN_ACTIVIDAD : EstadoGeneralRed.OPERATIVA;
    }
}
