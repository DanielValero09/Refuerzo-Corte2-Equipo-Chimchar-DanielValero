package reto3;

import model.ChargeType;
import model.Drone;
import model.Mission;
import model.MissionState;
import org.junit.jupiter.api.Test;
import reto3.builder.MissionBuilder;

import static org.junit.jupiter.api.Assertions.*;

class MissionBuilderTest {
    private final Drone drone = new Drone("D-03", "DJI Mini 3", 91, true, "Bloque C");

    @Test
    void conservaTodosLosDatosConfigurados() {
        Mission mission = base().notas("Entrega prioritaria").prioridad(1).horaMaximaEntrega("14:00").build();
        assertAll(
            () -> assertEquals("M-01", mission.id()),
            () -> assertSame(drone, mission.drone()),
            () -> assertEquals("Bloque C", mission.origen()),
            () -> assertEquals("Biblioteca", mission.destino()),
            () -> assertEquals(ChargeType.CARPETA, mission.tipoCarga()),
            () -> assertEquals(MissionState.PENDIENTE, mission.estado()),
            () -> assertEquals(1, mission.prioridad()),
            () -> assertEquals("Entrega prioritaria", mission.notas()),
            () -> assertEquals("14:00", mission.horaMaximaEntrega())
        );
    }

    @Test
    void aplicaLosDefaultsOpcionales() {
        Mission mission = base().build();
        assertAll(
            () -> assertEquals(3, mission.prioridad()),
            () -> assertEquals("", mission.notas()),
            () -> assertEquals("", mission.horaMaximaEntrega()),
            () -> assertEquals(MissionState.PENDIENTE, mission.estado())
        );
    }

    @Test
    void rechazaCadaCampoObligatorioAusente() {
        assertAll(
            () -> assertThrows(IllegalStateException.class, () -> base().drone(null).build()),
            () -> assertThrows(IllegalStateException.class, () -> base().origen(null).build()),
            () -> assertThrows(IllegalStateException.class, () -> base().destino(null).build())
        );
    }

    @Test
    void reutilizarBuilderNoModificaLaMisionYaConstruida() {
        MissionBuilder builder = base();
        Mission primera = builder.build();
        Mission segunda = builder.prioridad(1).notas("Nueva entrega").build();
        assertAll(
            () -> assertEquals(3, primera.prioridad()),
            () -> assertEquals("", primera.notas()),
            () -> assertEquals(1, segunda.prioridad()),
            () -> assertEquals("Nueva entrega", segunda.notas())
        );
    }

    private MissionBuilder base() {
        return new MissionBuilder().id("M-01").drone(drone).origen("Bloque C")
            .destino("Biblioteca").tipoCarga(ChargeType.CARPETA);
    }
}
