package infernape.reto3.observer;

import java.util.ArrayList;
import java.util.List;

public final class RegistroEventos implements ObservadorEtapa {
    private final List<EventoEtapa> eventos = new ArrayList<>();

    @Override public void onEtapa(EventoEtapa evento) { eventos.add(evento); }
    public List<EventoEtapa> eventos() { return List.copyOf(eventos); }
}
