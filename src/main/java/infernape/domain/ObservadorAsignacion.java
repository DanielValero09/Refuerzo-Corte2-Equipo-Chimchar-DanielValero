package infernape.domain;

@FunctionalInterface
public interface ObservadorAsignacion {
    void onAsignada(SolicitudAsignacion solicitud, DroneEnterprise drone);
}
