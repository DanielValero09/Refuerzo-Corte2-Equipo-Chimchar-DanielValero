package infernape.enterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import infernape.application.ServicioResumenRed;
import infernape.domain.*;
import org.junit.jupiter.api.Test;
import static infernape.support.DatosEnterprise.*;
import static org.junit.jupiter.api.Assertions.*;

class ResumenRedTest {
    @Test
    void resumeLasCuatroSedesFlotaMisionesActivasYAlertasExplicitas() {
        // Arrange
        var flota = List.of(drone("D-01", Sede.ECI, 85, true), drone("D-02", Sede.UNAL, 80, false));
        var misiones = List.of(activa("M-01", EstadoMision.PENDIENTE), activa("M-02", EstadoMision.EN_VUELO),
            entregada("M-03", Sede.ECI, "D-01", Prioridad.NORMAL, 12), fallida("M-04", Sede.ECI, "D-01", Prioridad.NORMAL));
        // Act
        var resumen = new ServicioResumenRed().generar(flota, misiones, List.of(new AlertaOperativa(Sede.UNAL, "Estacion cerrada")));
        // Assert
        assertEquals(4, resumen.sedes().size());
        assertEquals(new ResumenSede(1, 1, 2, 0), resumen.sedes().get(Sede.ECI));
        assertEquals(new ResumenSede(1, 0, 0, 1), resumen.sedes().get(Sede.UNAL));
        assertEquals(EstadoGeneralRed.CON_ALERTAS, resumen.estado());
    }

    @Test
    void redVaciaMuestraCuatroSedesSinActividad() {
        // Arrange
        var servicio = new ServicioResumenRed();
        // Act
        var resumen = servicio.generar(List.of(), List.of(), List.of());
        // Assert
        assertEquals(4, resumen.sedes().size());
        assertTrue(resumen.sedes().values().stream().allMatch(s -> s.equals(new ResumenSede(0, 0, 0, 0))));
        assertEquals(EstadoGeneralRed.SIN_ACTIVIDAD, resumen.estado());
    }

    @Test
    void indisponibilidadNoSeInventaComoFalloOAlerta() {
        // Arrange
        var flota = List.of(drone("D-02", Sede.UNAL, 80, false));
        // Act
        var resumen = new ServicioResumenRed().generar(flota, List.of(), List.of());
        // Assert
        assertEquals(EstadoGeneralRed.OPERATIVA, resumen.estado());
        assertEquals(0, resumen.sedes().get(Sede.UNAL).alertas());
    }

    @Test
    void resumenEsSnapshotInmutableDeLosDatosRecibidos() {
        // Arrange
        var flota = new ArrayList<>(List.of(drone("D-01", Sede.ECI, 85, true)));
        // Act
        var resumen = new ServicioResumenRed().generar(flota, List.of(), List.of());
        flota.clear();
        // Assert
        assertEquals(1, resumen.sedes().get(Sede.ECI).drones());
        assertThrows(UnsupportedOperationException.class, () -> resumen.sedes().clear());
    }

    private static MisionEnterprise activa(String id, EstadoMision estado) {
        return new MisionEnterprise(id, Sede.ECI, Optional.of("D-01"), estado, Prioridad.NORMAL, OptionalDouble.empty());
    }
}
