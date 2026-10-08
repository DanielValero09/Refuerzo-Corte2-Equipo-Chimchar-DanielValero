package monferno.reto12;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.EstadoDrone;
import monferno.model.EstadoMision;
import monferno.model.Mision;
import monferno.model.Prioridad;
import monferno.reto3.observer.ObservadorDrone;
import monferno.reto3.strategy.EstrategiaAsignacion;
import monferno.reto3.strategy.MayorBateriaStrategy;

public class AsignadorMision {
    private final ApiMeteorologica clima;
    private final ObservadorDrone notificador;
    private final EstrategiaAsignacion normal;
    private final EstrategiaAsignacion urgente;

    public AsignadorMision(ApiMeteorologica clima, ObservadorDrone notificador) {
        this(clima, notificador, new MayorBateriaStrategy(), new EstrategiaUrgenteExpress());
    }

    public AsignadorMision(ApiMeteorologica clima, ObservadorDrone notificador,
                           EstrategiaAsignacion normal, EstrategiaAsignacion urgente) {
        this.clima = Objects.requireNonNull(clima);
        this.notificador = Objects.requireNonNull(notificador);
        this.normal = Objects.requireNonNull(normal);
        this.urgente = Objects.requireNonNull(urgente);
    }

    public Optional<Drone> asignar(List<Drone> flota, Mision mision) {
        if (!solicitudAsignable(mision) || !clima.esApto()) {
            return Optional.empty();
        }
        EstrategiaAsignacion estrategia = mision.prioridad() == Prioridad.URGENTE ? urgente : normal;
        return estrategia.seleccionar(flota, mision).map(this::iniciarVuelo);
    }

    private boolean solicitudAsignable(Mision mision) {
        return mision.pesoPaqueteGramos() >= 1 && mision.pesoPaqueteGramos() <= 2000
            && mision.estado() == EstadoMision.PENDIENTE;
    }

    private Drone iniciarVuelo(Drone seleccionado) {
        Drone enVuelo = new Drone(seleccionado.id(), seleccionado.tipo(), seleccionado.bateria(),
            false, EstadoDrone.EN_VUELO, seleccionado.misionesCompletadas());
        notificador.onEstadoCambiado(enVuelo, EstadoDrone.EN_VUELO);
        return enVuelo;
    }
}
