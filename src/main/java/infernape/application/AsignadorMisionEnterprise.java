package infernape.application;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import infernape.domain.DroneEnterprise;
import infernape.domain.EstrategiaAsignacionEnterprise;
import infernape.domain.ObservadorAsignacion;
import infernape.domain.RepositorioFlota;
import infernape.domain.ServicioClima;
import infernape.domain.SolicitudAsignacion;

public final class AsignadorMisionEnterprise {
    private final RepositorioFlota repositorio;
    private final ServicioClima clima;
    private final EstrategiaAsignacionEnterprise estrategia;
    private final ObservadorAsignacion observador;

    public AsignadorMisionEnterprise(RepositorioFlota repositorio, ServicioClima clima,
                                    EstrategiaAsignacionEnterprise estrategia, ObservadorAsignacion observador) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.clima = Objects.requireNonNull(clima);
        this.estrategia = Objects.requireNonNull(estrategia);
        this.observador = Objects.requireNonNull(observador);
    }

    public Optional<DroneEnterprise> asignar(SolicitudAsignacion solicitud) {
        if (!clima.condicionesAptas(solicitud.origen(), solicitud.destino())) {
            return Optional.empty();
        }
        List<DroneEnterprise> flota = List.copyOf(repositorio.findDisponibles(solicitud.origen()));
        return estrategia.seleccionar(flota, solicitud)
            .filter(drone -> flota.contains(drone) && drone.sede() == solicitud.origen())
            .filter(drone -> drone.aptoPara(solicitud.pesoPaqueteGramos()))
            .map(drone -> notificar(solicitud, drone.enMision()));
    }

    private DroneEnterprise notificar(SolicitudAsignacion solicitud, DroneEnterprise drone) {
        observador.onAsignada(solicitud, drone);
        return drone;
    }
}
