package model;

public record Mission(
        String id,
        Drone drone,
        String origen,
        String destino,
        ChargeType tipoCarga,
        MissionState estado,
        int prioridad,
        String notas,
        String horaMaximaEntrega
) {
    public Mission(String id, Drone drone, String origen, String destino,
                   ChargeType tipoCarga, MissionState estado) {
        this(id, drone, origen, destino, tipoCarga, estado, 3, "", "");
    }
}
