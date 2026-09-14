package src.main.java.reto3.builder;

import src.main.java.model.ChargeType;
import src.main.java.model.Drone;
import src.main.java.model.Mission;
import src.main.java.model.MissionState;

public class MissionBuilder {

    private String id;
    private Drone drone;
    private String origen;
    private String destino;
    private ChargeType tipoCarga;

    private String notas = "";
    private int prioridad = 3;
    private String horaMaximaEntrega = "";

    public MissionBuilder id(String id) {
        this.id = id;
        return this;
    }

    public MissionBuilder drone(Drone drone) {
        this.drone = drone;
        return this;
    }

    public MissionBuilder origen(String origen) {
        this.origen = origen;
        return this;
    }

    public MissionBuilder destino(String destino) {
        this.destino = destino;
        return this;
    }

    public MissionBuilder tipoCarga(ChargeType tipoCarga) {
        this.tipoCarga = tipoCarga;
        return this;
    }

    public MissionBuilder notas(String notas) {
        this.notas = notas;
        return this;
    }

    public MissionBuilder prioridad(int prioridad) {
        this.prioridad = prioridad;
        return this;
    }

    public MissionBuilder horaMaximaEntrega(String horaMaximaEntrega) {
        this.horaMaximaEntrega = horaMaximaEntrega;
        return this;
    }

    public Mission build() {

        if (drone == null || origen == null || destino == null) {
            throw new IllegalStateException(
                    "Drone, origen y destino son obligatorios"
            );
        }

        System.out.println("Notas: " + notas);
        System.out.println("Prioridad: " + prioridad);
        System.out.println("Hora máxima: " + horaMaximaEntrega);

        return new Mission(
                id,
                drone,
                origen,
                destino,
                tipoCarga,
                MissionState.PENDIENTE
        );
    }
}
