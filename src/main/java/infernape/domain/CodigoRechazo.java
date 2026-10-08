package infernape.domain;

public enum CodigoRechazo {
    CLIMA_ADVERSO("Condiciones meteorologicas adversas: no se inicia el vuelo."),
    NO_DRONES_APTOS("No hay drones aptos disponibles para este paquete."),
    PESO_EXCESIVO("El paquete supera el maximo de 2000 gramos."),
    AEROCIVIL_RECHAZA("La ruta no tiene autorizacion regulatoria verificable."),
    SEDE_INACTIVA("Una de las sedes no esta activa para operar."),
    CONFIGURACION_NO_DISPONIBLE("No hay configuracion operativa autorizada para la sede."),
    MISION_DUPLICADA("Ya existe una mision con ese ID.");

    private final String mensaje;
    CodigoRechazo(String mensaje) { this.mensaje = mensaje; }
    public String mensaje() { return mensaje; }
}
