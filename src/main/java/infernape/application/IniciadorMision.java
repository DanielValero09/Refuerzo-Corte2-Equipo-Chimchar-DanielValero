package infernape.application;

import infernape.domain.*;
import java.util.Map;
import java.util.Objects;

public final class IniciadorMision {
    private final AsignadorMisionEnterprise asignador;
    private final AutorizadorRutaInterSede autorizador;
    private final ServicioClima clima;
    private final RepositorioEstadoSede sedes;
    private final RepositorioMisionesEnterprise misiones;
    private final Map<Sede, ConfiguracionOperacionSede> configuraciones;

    public IniciadorMision(AsignadorMisionEnterprise asignador, AutorizadorRutaInterSede autorizador,
                          ServicioClima clima, RepositorioEstadoSede sedes,
                          RepositorioMisionesEnterprise misiones, Map<Sede, ConfiguracionOperacionSede> configuraciones) {
        this.asignador = Objects.requireNonNull(asignador);
        this.autorizador = Objects.requireNonNull(autorizador);
        this.clima = Objects.requireNonNull(clima);
        this.sedes = Objects.requireNonNull(sedes);
        this.misiones = Objects.requireNonNull(misiones);
        this.configuraciones = Map.copyOf(configuraciones);
    }
    public MisionRegistrada iniciar(SolicitudInicioMision inicio) {
        var solicitud = inicio.asignacion();
        if (solicitud.pesoPaqueteGramos() > 2000) throw new ReglaOperacionException(CodigoRechazo.PESO_EXCESIVO);
        if (misiones.buscarPorId(solicitud.id()).isPresent()) throw new ReglaOperacionException(CodigoRechazo.MISION_DUPLICADA);
        if (!sedes.activa(solicitud.origen()) || !sedes.activa(solicitud.destino())) throw new ReglaOperacionException(CodigoRechazo.SEDE_INACTIVA);
        if (!clima.condicionesAptas(solicitud.origen(), solicitud.destino())) throw new ReglaOperacionException(CodigoRechazo.CLIMA_ADVERSO);
        var config = java.util.Optional.ofNullable(configuraciones.get(solicitud.origen()))
            .orElseThrow(() -> new ReglaOperacionException(CodigoRechazo.CONFIGURACION_NO_DISPONIBLE));
        if (!autorizador.autorizar(solicitud, config, inicio.distanciaPlanificadaKm(), inicio.alturaMetros())) {
            throw new ReglaOperacionException(CodigoRechazo.AEROCIVIL_RECHAZA);
        }
        var drone = asignador.asignar(solicitud).orElseThrow(() -> new ReglaOperacionException(CodigoRechazo.NO_DRONES_APTOS));
        var resultado = new MisionRegistrada(solicitud.id(), solicitud.origen(), solicitud.destino(), solicitud.pesoPaqueteGramos(), solicitud.prioridad(), drone, EstadoMision.EN_VUELO);
        misiones.guardar(resultado);
        return resultado;
    }
}
