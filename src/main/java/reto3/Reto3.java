package src.main.java.reto3;

import src.main.java.model.ChargeType;
import src.main.java.model.Drone;
import src.main.java.model.Mission;

import src.main.java.reto3.builder.MissionBuilder;

import src.main.java.reto3.chain.Validador;
import src.main.java.reto3.chain.ValidadorBateria;
import src.main.java.reto3.chain.ValidadorDestino;
import src.main.java.reto3.chain.ValidadorCarga;

import src.main.java.reto3.strategy.AsignadorDrone;
import src.main.java.reto3.strategy.AsignacionMayorBateria;

import java.util.List;

public class Reto3 {

    public static void main(String[] args) {

        // =========================================================
        // PROBLEMA 1 - BUILDER
        // =========================================================

        System.out.println("===== PROBLEMA 1 - BUILDER =====");

        Drone drone = new Drone(
                "D-03",
                "DJI Mini 3",
                91,
                true,
                "Bloque C"
        );

        Mission mission = new MissionBuilder()
                .id("M-01")
                .drone(drone)
                .origen("Bloque C")
                .destino("Biblioteca")
                .tipoCarga(ChargeType.SOBRE)
                .notas("Entrega prioritaria")
                .prioridad(1)
                .horaMaximaEntrega("14:00")
                .build();

        System.out.println("Misión creada:");
        System.out.println(mission);


        // =========================================================
        // PROBLEMA 2 - CHAIN OF RESPONSIBILITY
        // =========================================================

        System.out.println("\n===== PROBLEMA 2 - CHAIN OF RESPONSIBILITY =====");

        Validador validadorBateria = new ValidadorBateria();
        Validador validadorDestino = new ValidadorDestino();
        Validador validadorCarga = new ValidadorCarga();

        validadorBateria
                .setSiguiente(validadorDestino)
                .setSiguiente(validadorCarga);

        boolean missionValida = validadorBateria.validar(mission);

        System.out.println("¿La misión pasó todas las validaciones?: "
                + missionValida);


        // =========================================================
        // PROBLEMA 3 - STRATEGY
        // =========================================================

        System.out.println("\n===== PROBLEMA 3 - STRATEGY =====");

        List<Drone> flota = List.of(
                new Drone(
                        "D-01",
                        "DJI Mini 3",
                        85,
                        true,
                        "Bloque A"
                ),

                new Drone(
                        "D-02",
                        "DJI Mini 3",
                        42,
                        false,
                        "Biblioteca"
                ),

                new Drone(
                        "D-03",
                        "DJI Mini 3",
                        91,
                        true,
                        "Bloque C"
                ),

                new Drone(
                        "D-04",
                        "DJI Mini 3",
                        18,
                        true,
                        "Bloque B"
                ),

                new Drone(
                        "D-05",
                        "DJI Mini 3",
                        67,
                        true,
                        "Bloque D"
                )
        );

        AsignadorDrone asignador = new AsignadorDrone(
                new AsignacionMayorBateria()
        );

        asignador.asignar(flota)
                .ifPresentOrElse(

                        droneSeleccionado ->
                                System.out.println(
                                        "Drone seleccionado: "
                                                + droneSeleccionado.id()
                                                + " - Batería: "
                                                + droneSeleccionado.battery()
                                                + "%"
                                ),

                        () ->
                                System.out.println(
                                        "No hay drones disponibles para la misión."
                                )
                );
    }
}