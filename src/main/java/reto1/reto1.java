package src.main.java.reto1;
import src.main.java.model.Drone;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class reto1 {

    public static void main(String[] args) {

        List<Drone> flota = List.of(
                new Drone("D-01", "DJI Mini 3", 85, true, "Bloque A"),
                new Drone("D-02", "DJI Mini 3", 42, false, "Biblioteca"),
                new Drone("D-03", "DJI Mini 3", 91, true, "Bloque C"),
                new Drone("D-04", "DJI Mini 3", 18, true, "Bloque B"),
                new Drone("D-05", "DJI Mini 3", 67, true, "Bloque D")
        );

        // CONSULTA 1
        List<String> dronesDisponibles = flota.stream()
                .filter(drone -> drone.available())
                .filter(drone -> drone.battery() >= 50)
                .sorted(Comparator.comparingInt(Drone::battery).reversed())
                .map(Drone::id)
                .collect(Collectors.toList());

        System.out.println("Consulta 1: " + dronesDisponibles);


        // CONSULTA 2
        boolean droneDisponibleBloqueC = flota.stream()
                .anyMatch(drone ->
                        drone.available()
                                && drone.ubication().equals("Bloque C")
                );

        System.out.println("Consulta 2: " + droneDisponibleBloqueC);


        // CONSULTA 3
        long dronesBateriaCritica = flota.stream()
                .filter(drone -> drone.battery() < 20)
                .count();

        System.out.println("Consulta 3: " + dronesBateriaCritica);


        // CONSULTA 4
        List<String> bateriasDrones = flota.stream()
                .map(drone -> drone.id() + ": " + drone.battery() + "%")
                .collect(Collectors.toList());

        System.out.println("Consulta 4: " + bateriasDrones);
    }
}
