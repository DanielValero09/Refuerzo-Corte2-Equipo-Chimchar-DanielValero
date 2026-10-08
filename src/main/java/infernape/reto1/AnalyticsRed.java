package infernape.reto1;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import infernape.domain.MetricasSede;
import infernape.domain.MisionEnterprise;
import infernape.domain.Sede;

public final class AnalyticsRed {
    public Map<Sede, Optional<MetricasSede>> calcular(List<MisionEnterprise> misiones) {
        Collector<MisionEnterprise, AcumuladorSede, MetricasSede> porSede =
            Collector.of(AcumuladorSede::new, AcumuladorSede::aceptar,
                AcumuladorSede::combinar, AcumuladorSede::resultado);
        Map<Sede, MetricasSede> agregadas = misiones.stream().collect(Collectors.groupingBy(
            MisionEnterprise::sede, () -> new EnumMap<>(Sede.class), porSede));
        Map<Sede, Optional<MetricasSede>> resultado = new EnumMap<>(Sede.class);
        Arrays.stream(Sede.values()).forEach(sede -> resultado.put(sede, Optional.ofNullable(agregadas.get(sede))));
        return Map.copyOf(resultado);
    }
}
