package infernape.application;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import infernape.domain.*;

public final class ServicioResumenRed {
    public ResumenRed generar(List<DroneEnterprise> drones, List<MisionEnterprise> misiones, List<AlertaOperativa> alertas) {
        Map<Sede, Long> flota = contar(drones, DroneEnterprise::sede);
        Map<Sede, Long> libres = contar(drones.stream().filter(DroneEnterprise::disponible).toList(), DroneEnterprise::sede);
        Map<Sede, Long> activas = contar(misiones.stream().filter(this::activa).toList(), MisionEnterprise::sede);
        Map<Sede, Long> avisos = contar(alertas, AlertaOperativa::sede);
        return new ResumenRed(Arrays.stream(Sede.values()).collect(Collectors.toMap(Function.identity(),
            sede -> new ResumenSede(flota.getOrDefault(sede, 0L), libres.getOrDefault(sede, 0L),
                activas.getOrDefault(sede, 0L), avisos.getOrDefault(sede, 0L)))));
    }

    private boolean activa(MisionEnterprise mision) {
        return mision.estado() == EstadoMision.PENDIENTE || mision.estado() == EstadoMision.EN_VUELO;
    }

    private <T> Map<Sede, Long> contar(List<T> datos, Function<T, Sede> sede) {
        return datos.stream().collect(Collectors.groupingBy(sede, Collectors.counting()));
    }
}
