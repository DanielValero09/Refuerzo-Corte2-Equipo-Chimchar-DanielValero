package infernape.application;

import java.time.Duration;
import java.util.Objects;
import infernape.domain.*;

public final class AutorizadorRutaInterSede {
    public static final Duration PLAZO_VERIFICACION = Duration.ofSeconds(2);
    private final ServicioAerocivil aerocivil;

    public AutorizadorRutaInterSede(ServicioAerocivil aerocivil) {
        this.aerocivil = Objects.requireNonNull(aerocivil);
    }

    public boolean autorizar(SolicitudAsignacion solicitud, ConfiguracionOperacionSede configuracion,
                              double distanciaDesdeSedeKm, int alturaMetros) {
        if (solicitud.origen() == solicitud.destino() || solicitud.origen() != configuracion.sede()
            || !configuracion.permite(distanciaDesdeSedeKm, alturaMetros)) {
            return false;
        }
        return aerocivil.verificar(solicitud.origen(), solicitud.destino(), PLAZO_VERIFICACION)
            .filter(CondicionesEspacioAereo::autorizado)
            .filter(condiciones -> alturaMetros <= condiciones.alturaMaximaMetros()).isPresent();
    }
}
