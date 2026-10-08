package infernape.support;

import java.util.Optional;
import java.util.OptionalDouble;
import infernape.domain.*;
import infernape.reto3.ruta.*;

public final class DatosEnterprise {
    private DatosEnterprise() { }
    public static final PuntoRuta ECI = PuntoRuta.campus(Sede.ECI);
    public static final PuntoRuta UNAL = PuntoRuta.campus(Sede.UNAL);
    public static final PuntoRuta UNIANDES = PuntoRuta.campus(Sede.UNIANDES);
    public static final PuntoRuta CARGA = PuntoRuta.carga(new EstacionCarga("C-116", "Estacion calle 116", true));

    public static MisionEnterprise entregada(String id, Sede sede, String drone, Prioridad prioridad, double tiempo) {
        return new MisionEnterprise(id, sede, Optional.of(drone), EstadoMision.ENTREGADA, prioridad, OptionalDouble.of(tiempo));
    }

    public static MisionEnterprise fallida(String id, Sede sede, String drone, Prioridad prioridad) {
        return new MisionEnterprise(id, sede, Optional.of(drone), EstadoMision.FALLIDA, prioridad, OptionalDouble.empty());
    }

    public static Ruta simple(PuntoRuta origen, PuntoRuta destino, double km) {
        return new RutaSimple(new EtapaRuta(origen, destino, km));
    }

    public static Ruta viaCarga() {
        return new RutaCompuesta(java.util.List.of(simple(ECI, CARGA, 3), simple(CARGA, UNAL, 3)));
    }

    public static DroneEnterprise drone(String id, Sede sede, int bateria, boolean disponible) {
        return new DroneEnterprise(id, sede, PerfilDrone.MINI, bateria, disponible);
    }

    public static SolicitudAsignacion solicitud() {
        return new SolicitudAsignacion("M-E-01", Sede.ECI, Sede.UNAL, 200, Prioridad.NORMAL);
    }
}
