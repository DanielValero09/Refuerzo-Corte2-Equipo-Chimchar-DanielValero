package infernape.reto3.observer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MonitorRed implements ObservadorEtapa {
    private final Map<String, EventoEtapa> ultimo = new HashMap<>();

    @Override public void onEtapa(EventoEtapa evento) { ultimo.put(evento.drone().id(), evento); }
    public Optional<EventoEtapa> ultimoEvento(String droneId) { return Optional.ofNullable(ultimo.get(droneId)); }
}
