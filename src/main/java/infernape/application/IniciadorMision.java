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
    private final ObservadorAsignacion observadorPersistencia;

    public IniciadorMision(AsignadorMisionEnterprise asignador, AutorizadorRutaInterSede autorizador,
                          ServicioClima clima, RepositorioEstadoSede sedes,
                          RepositorioMisionesEnterprise misiones, Map<Sede, ConfiguracionOperacionSede> configuraciones) {
        this(asignador, autorizador, clima, sedes, misiones, configuraciones, (solicitud, drone) -> { });
    }
    public IniciadorMision(AsignadorMisionEnterprise asignador, AutorizadorRutaInterSede autorizador,
                          ServicioClima clima, RepositorioEstadoSede sedes,
                          RepositorioMisionesEnterprise misiones, Map<Sede, ConfiguracionOperacionSede> configuraciones,
                          ObservadorAsignacion observadorPersistencia) {
        this.asignador = Objects.requireNonNull(asignador);
        this.autorizador = Objects.requireNonNull(autorizador);
        this.clima = Objects.requireNonNull(clima);
        this.sedes = Objects.requireNonNull(sedes);
        this.misiones = Objects.requireNonNull(misiones);
        this.configuraciones = Map.copyOf(configuraciones);
        this.observadorPersistencia = Objects.requireNonNull(observadorPersistencia);
    }
    public MisionRegistrada iniciar(SolicitudInicioMision inicio) {
        var solicitud = inicio.asignacion();
        validarOperacion(solicitud);
        validarAutorizacion(inicio);
        var drone = asignador.asignar(solicitud).orElseThrow(() -> new ReglaOperacionException(CodigoRechazo.NO_DRONES_APTOS));
        return registrar(solicitud, drone);
    }
    private void validarOperacion(SolicitudAsignacion solicitud) {
        if (solicitud.pesoPaqueteGramos() > 2000) throw new ReglaOperacionException(CodigoRechazo.PESO_EXCESIVO);
        if (misiones.buscarPorId(solicitud.id()).isPresent()) throw new ReglaOperacionException(CodigoRechazo.MISION_DUPLICADA);
        if (!sedes.activa(solicitud.origen()) || !sedes.activa(solicitud.destino())) throw new ReglaOperacionException(CodigoRechazo.SEDE_INACTIVA);
        if (!clima.condicionesAptas(solicitud.origen(), solicitud.destino())) throw new ReglaOperacionException(CodigoRechazo.CLIMA_ADVERSO);
    }
    private void validarAutorizacion(SolicitudInicioMision inicio) {
        var solicitud = inicio.asignacion();
        var config = java.util.Optional.ofNullable(configuraciones.get(solicitud.origen()))
            .orElseThrow(() -> new ReglaOperacionException(CodigoRechazo.CONFIGURACION_NO_DISPONIBLE));
        if (!autorizador.autorizar(solicitud, config, inicio.distanciaPlanificadaKm(), inicio.alturaMetros())) {
            throw new ReglaOperacionException(CodigoRechazo.AEROCIVIL_RECHAZA);
        }
    }
    private MisionRegistrada registrar(SolicitudAsignacion solicitud, DroneEnterprise drone) {
        var resultado = new MisionRegistrada(solicitud.id(), solicitud.origen(), solicitud.destino(), solicitud.pesoPaqueteGramos(), solicitud.prioridad(), drone, EstadoMision.EN_VUELO);
        misiones.guardar(resultado);
        observadorPersistencia.onAsignada(solicitud, drone);
        return resultado;
    }
}
