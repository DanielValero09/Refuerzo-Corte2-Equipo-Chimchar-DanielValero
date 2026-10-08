package monferno.reto1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import monferno.model.Drone;
import monferno.model.EstadoMision;
import monferno.model.Mision;
import monferno.model.Prioridad;
import monferno.model.TipoDrone;

public class EstadisticasMisiones {
    public Map<TipoDrone, Long> completadasHoyPorTipo(List<Mision> misiones, LocalDate hoy) {
        return misiones.stream()
            .filter(mision -> mision.estado() == EstadoMision.ENTREGADA)
            .filter(mision -> mision.entregadaEn().orElseThrow().toLocalDate().equals(hoy))
            .collect(Collectors.groupingBy(mision -> mision.drone().orElseThrow().tipo(), Collectors.counting()));
    }

    public Optional<Drone> droneConMasCompletadas(List<Mision> misiones) {
        Map<String, Long> conteos = misiones.stream()
            .filter(mision -> mision.estado() == EstadoMision.ENTREGADA)
            .map(mision -> mision.drone().orElseThrow())
            .collect(Collectors.groupingBy(Drone::id, Collectors.counting()));
        return conteos.entrySet().stream()
            .max(Comparator.<Map.Entry<String, Long>>comparingLong(Map.Entry::getValue)
                .thenComparing(Map.Entry::getKey, Comparator.reverseOrder()))
            .flatMap(ganador -> misiones.stream()
                .filter(mision -> mision.estado() == EstadoMision.ENTREGADA)
                .map(mision -> mision.drone().orElseThrow())
                .filter(drone -> drone.id().equals(ganador.getKey()))
                .findFirst());
    }

    public double porcentajeFallidas(List<Mision> misiones) {
        if (misiones.isEmpty()) {
            return 0.0;
        }
        long fallidas = misiones.stream().filter(mision -> mision.estado() == EstadoMision.FALLIDA).count();
        return 100.0 * fallidas / misiones.size();
    }

    public boolean existeUrgentePendienteAntigua(List<Mision> misiones, LocalDateTime ahora) {
        return misiones.stream().anyMatch(mision -> mision.prioridad() == Prioridad.URGENTE
            && mision.estado() == EstadoMision.PENDIENTE
            && mision.creadaEn().isBefore(ahora.minusMinutes(10)));
    }
}
