package src.main.java.model;

public record Mission(
        String id,
        Drone drone,
        String origen,
        String destino,
        ChargeType tipoCarga,
        MissionState estado
) {
}
