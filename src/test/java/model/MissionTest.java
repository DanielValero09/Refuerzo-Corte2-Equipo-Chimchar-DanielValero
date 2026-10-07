package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MissionTest {
    @Test
    void constructorCompatibleConservaDatosYAplicaDefaults() {
        Drone drone = new Drone("D-01", "DJI Mini 3", 85, true, "Bloque A");
        Mission mission = new Mission("M-01", drone, "Bloque A", "Biblioteca", ChargeType.SOBRE, MissionState.PENDIENTE);
        assertAll(
            () -> assertEquals("M-01", mission.id()),
            () -> assertSame(drone, mission.drone()),
            () -> assertEquals("Bloque A", mission.origen()),
            () -> assertEquals("Biblioteca", mission.destino()),
            () -> assertEquals(ChargeType.SOBRE, mission.tipoCarga()),
            () -> assertEquals(MissionState.PENDIENTE, mission.estado()),
            () -> assertEquals(3, mission.prioridad()),
            () -> assertEquals("", mission.notas()),
            () -> assertEquals("", mission.horaMaximaEntrega())
        );
    }
}
