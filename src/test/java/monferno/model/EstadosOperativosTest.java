package monferno.model;

import java.util.List;
import java.util.Set;
import monferno.reto3.GestorFlota;
import monferno.reto3.observer.AlertaTecnico;
import monferno.reto3.observer.PanelOperador;
import monferno.reto3.observer.SistemaLog;
import monferno.reto3.strategy.EstrategiaAsignacion;
import monferno.reto3.strategy.MayorBateriaStrategy;
import monferno.reto3.strategy.MenorUsoAcumuladoStrategy;
import monferno.reto3.strategy.TipoCompatibleCargaStrategy;
import monferno.support.DatosMonferno;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstadosOperativosTest {
    @Test
    void modeloConservaLosEstadosAnterioresYAdmiteCargaYMantenimiento() {
        // Arrange
        Set<EstadoDrone> esperados = Set.of(EstadoDrone.DISPONIBLE, EstadoDrone.EN_VUELO,
            EstadoDrone.ATERRIZANDO, EstadoDrone.FALLO, EstadoDrone.EN_CARGA, EstadoDrone.MANTENIMIENTO);
        // Act
        Set<EstadoDrone> actuales = Set.of(EstadoDrone.values());
        // Assert
        assertEquals(esperados, actuales);
    }

    @ParameterizedTest
    @EnumSource(value = EstadoDrone.class, names = {"EN_CARGA", "MANTENIMIENTO"})
    void estadoNoOperativoBloqueaLasTresEstrategiasInclusoConDisponibleTrue(EstadoDrone estado) {
        // Arrange
        Drone noApto = new Drone("D-01", TipoDrone.MINI, 95, true, estado, 0);
        Drone apto = DatosMonferno.drone("D-02", TipoDrone.MINI, 60, 5);
        List<EstrategiaAsignacion> estrategias = List.of(new MayorBateriaStrategy(),
            new MenorUsoAcumuladoStrategy(), new TipoCompatibleCargaStrategy());
        Mision mision = DatosMonferno.pendiente(300);
        // Act
        List<Drone> elegidos = estrategias.stream()
            .map(estrategia -> estrategia.seleccionar(List.of(noApto, apto), mision).orElseThrow()).toList();
        // Assert
        assertEquals(List.of(apto, apto, apto), elegidos);
    }

    @ParameterizedTest
    @EnumSource(value = EstadoDrone.class, names = {"EN_CARGA", "MANTENIMIENTO"})
    void cambioNotificaConservaDatosYPermiteAsignarSoloAlVolverADisponible(EstadoDrone estado) {
        // Arrange
        Drone inicial = DatosMonferno.drone("D-01", TipoDrone.MINI, 85, 7);
        GestorFlota gestor = new GestorFlota(List.of(inicial));
        PanelOperador panel = new PanelOperador();
        SistemaLog log = new SistemaLog();
        AlertaTecnico tecnico = new AlertaTecnico();
        gestor.suscribir(panel);
        gestor.suscribir(log);
        gestor.suscribir(tecnico);
        MayorBateriaStrategy estrategia = new MayorBateriaStrategy();
        Mision mision = DatosMonferno.pendiente(300);
        // Act
        Drone bloqueado = gestor.cambiarEstado(inicial.id(), estado);
        EstadoDrone estadoPanel = panel.estadoDe(inicial.id()).orElseThrow();
        boolean asignacionBloqueada = estrategia.seleccionar(gestor.listar(), mision).isEmpty();
        Drone restaurado = gestor.cambiarEstado(inicial.id(), EstadoDrone.DISPONIBLE);
        Drone seleccionado = estrategia.seleccionar(gestor.listar(), mision).orElseThrow();
        // Assert
        assertAll(() -> assertFalse(bloqueado.disponible()), () -> assertTrue(asignacionBloqueada),
            () -> assertEquals(estado, estadoPanel), () -> assertEquals(85, bloqueado.bateria()),
            () -> assertEquals(7, bloqueado.misionesCompletadas()), () -> assertTrue(restaurado.disponible()),
            () -> assertEquals(restaurado, seleccionado), () -> assertTrue(tecnico.alertas().isEmpty()),
            () -> assertEquals(List.of("D-01 -> " + estado, "D-01 -> DISPONIBLE"), log.registros()),
            () -> assertEquals(EstadoDrone.DISPONIBLE, panel.estadoDe(inicial.id()).orElseThrow()));
    }
}
