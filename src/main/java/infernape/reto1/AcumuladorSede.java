package infernape.reto1;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import infernape.domain.EstadoMision;
import infernape.domain.MetricasSede;
import infernape.domain.MisionEnterprise;
import infernape.domain.Prioridad;

final class AcumuladorSede {
    private long total;
    private long entregadas;
    private long urgentes;
    private double minutos;
    private final Map<String, Long> uso = new HashMap<>();

    void aceptar(MisionEnterprise mision) {
        total++;
        urgentes += mision.prioridad() == Prioridad.URGENTE ? 1 : 0;
        mision.droneId().ifPresent(id -> uso.merge(id, 1L, Long::sum));
        if (mision.estado() == EstadoMision.ENTREGADA) {
            entregadas++;
            minutos += mision.tiempoEntregaMinutos().orElseThrow();
        }
    }

    AcumuladorSede combinar(AcumuladorSede otro) {
        total += otro.total;
        entregadas += otro.entregadas;
        urgentes += otro.urgentes;
        minutos += otro.minutos;
        otro.uso.forEach((id, cantidad) -> uso.merge(id, cantidad, Long::sum));
        return this;
    }

    MetricasSede resultado() {
        OptionalDouble promedio = entregadas == 0 ? OptionalDouble.empty() : OptionalDouble.of(minutos / entregadas);
        Optional<String> ganador = uso.entrySet().stream()
            .max(Comparator.<Map.Entry<String, Long>>comparingLong(Map.Entry::getValue)
                .thenComparing(Map.Entry::getKey, Comparator.reverseOrder())).map(Map.Entry::getKey);
        return new MetricasSede(100.0 * entregadas / total, promedio, ganador, 100.0 * urgentes / total);
    }
}
