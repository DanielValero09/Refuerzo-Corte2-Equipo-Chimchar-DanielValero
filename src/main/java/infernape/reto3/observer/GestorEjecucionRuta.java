package infernape.reto3.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import infernape.domain.DroneEnterprise;
import infernape.domain.EtapaRuta;

public final class GestorEjecucionRuta {
    private final List<ObservadorEtapa> observadores = new ArrayList<>();

    public GestorEjecucionRuta(List<ObservadorEtapa> iniciales) {
        List.copyOf(iniciales).forEach(this::suscribir);
    }

    public void suscribir(ObservadorEtapa observador) {
        Objects.requireNonNull(observador);
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void desuscribir(ObservadorEtapa observador) { observadores.remove(observador); }

    public void publicar(EtapaRuta etapa, DroneEnterprise drone, EstadoEtapa estado) {
        EventoEtapa evento = new EventoEtapa(etapa, drone, estado);
        List.copyOf(observadores).forEach(observador -> observador.onEtapa(evento));
    }
}
