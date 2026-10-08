package infernape.reto3.observer;

@FunctionalInterface
public interface ObservadorEtapa {
    void onEtapa(EventoEtapa evento);
}
