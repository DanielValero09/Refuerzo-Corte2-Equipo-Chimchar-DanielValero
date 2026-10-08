package infernape.reto3.ruta;

import java.util.List;
import java.util.stream.IntStream;
import infernape.domain.EtapaRuta;

public record RutaCompuesta(List<Ruta> partes) implements Ruta {
    public RutaCompuesta {
        partes = List.copyOf(partes);
        if (partes.isEmpty()) {
            throw new IllegalArgumentException("Ruta compuesta requiere partes");
        }
        List<EtapaRuta> etapas = partes.stream().flatMap(ruta -> ruta.etapas().stream()).toList();
        boolean continua = IntStream.range(1, etapas.size())
            .allMatch(i -> etapas.get(i - 1).destino().mismoLugar(etapas.get(i).origen()));
        if (!continua) {
            throw new IllegalArgumentException("Etapas desconectadas");
        }
    }

    @Override public double distanciaKm() {
        return partes.stream().mapToDouble(Ruta::distanciaKm).sum();
    }

    @Override public List<EtapaRuta> etapas() {
        return partes.stream().flatMap(ruta -> ruta.etapas().stream()).toList();
    }
}
