package reto4;

import model.ChargeType;
import model.Drone;
import model.Mission;
import model.MissionState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsignadorMisionTest {
    @Test
    void asignaDroneEnUnaNuevaMisionConservandoLosOtrosDatos() {
        Mission original = new Mission("M-04", null, "Bloque C", "Biblioteca", ChargeType.CARPETA,
            MissionState.PENDIENTE, 1, "Entrega prioritaria", "14:00");
        Drone drone = new Drone("D-03", "DJI Mini 3", 91, true, "Bloque C");
        Mission asignada = new AsignadorMision().asignar(original, drone);
        assertAll(
            () -> assertNotSame(original, asignada),
            () -> assertNull(original.drone()),
            () -> assertSame(drone, asignada.drone()),
            () -> assertEquals(original.id(), asignada.id()),
            () -> assertEquals(original.origen(), asignada.origen()),
            () -> assertEquals(original.destino(), asignada.destino()),
            () -> assertEquals(original.tipoCarga(), asignada.tipoCarga()),
            () -> assertEquals(original.estado(), asignada.estado()),
            () -> assertEquals(original.prioridad(), asignada.prioridad()),
            () -> assertEquals(original.notas(), asignada.notas()),
            () -> assertEquals(original.horaMaximaEntrega(), asignada.horaMaximaEntrega())
        );
    }

    @Test
    void rechazaMisionODroneNulos() {
        AsignadorMision asignador = new AsignadorMision();
        Mission mission = new Mission("M-04", null, "Bloque A", "Biblioteca", ChargeType.SOBRE, MissionState.PENDIENTE);
        Drone drone = new Drone("D-01", "DJI Mini 3", 85, true, "Bloque A");
        assertAll(
            () -> assertThrows(NullPointerException.class, () -> asignador.asignar(null, drone)),
            () -> assertThrows(NullPointerException.class, () -> asignador.asignar(mission, null))
        );
    }
}
