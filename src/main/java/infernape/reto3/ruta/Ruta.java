package infernape.reto3.ruta;

import java.util.List;
import infernape.domain.EtapaRuta;

public interface Ruta {
    double distanciaKm();
    List<EtapaRuta> etapas();

    default long numeroRecargas() {
        return etapas().stream().filter(etapa -> etapa.destino().estacion().isPresent()).count();
    }

    default String clave() {
        return etapas().stream().map(etapa -> etapa.origen().id() + ">" + etapa.destino().id())
            .collect(java.util.stream.Collectors.joining("|"));
    }
}
