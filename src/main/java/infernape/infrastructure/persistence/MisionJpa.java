package infernape.infrastructure.persistence;

import infernape.domain.*;
import jakarta.persistence.*;

@Entity
@Table(name = "enterprise_misiones")
public class MisionJpa {
    @Id private String id;
    @Enumerated(EnumType.STRING) private Sede origen;
    @Enumerated(EnumType.STRING) private Sede destino;
    private int pesoPaquete;
    @Enumerated(EnumType.STRING) private Prioridad prioridad;
    @Enumerated(EnumType.STRING) private EstadoMision estado;
    private String droneId;
    private String perfil;
    private int capacidad;
    private int pesoMinimo;
    private int bateria;
    protected MisionJpa() { }

    public MisionJpa(MisionRegistrada mision) {
        id = mision.id(); origen = mision.origen(); destino = mision.destino();
        pesoPaquete = mision.pesoPaquete(); prioridad = mision.prioridad(); estado = mision.estado();
        droneId = mision.drone().id(); bateria = mision.drone().bateria();
        perfil = mision.drone().perfil().codigo(); capacidad = mision.drone().perfil().capacidadMaximaGramos();
        pesoMinimo = mision.drone().perfil().pesoMinimoGramos();
    }
    public MisionRegistrada aDominio() {
        var tipo = new PerfilDrone(perfil, capacidad, pesoMinimo);
        var drone = new DroneEnterprise(droneId, origen, tipo, bateria, false);
        return new MisionRegistrada(id, origen, destino, pesoPaquete, prioridad, drone, estado);
    }
}
