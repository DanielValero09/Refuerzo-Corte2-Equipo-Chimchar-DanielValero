package monferno.reto3;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import monferno.model.Drone;
import monferno.model.EstadoDrone;
import monferno.model.TipoDrone;
import monferno.reto3.observer.AlertaTecnico;
import monferno.reto3.observer.ObservadorDrone;
import monferno.reto3.observer.PanelOperador;
import monferno.reto3.observer.SistemaLog;
import org.junit.jupiter.api.Test;

import static monferno.support.DatosMonferno.drone;
import static org.junit.jupiter.api.Assertions.*;

class GestorFlotaObserverTest {
    private final Drone inicial = drone("D-01", TipoDrone.MINI, 85, 7);

    @Test
    void actualizaElDroneYNotificaPanelLogYTecnicoSegunElEstado() {
        // Arrange
        GestorFlota gestor = new GestorFlota(List.of(inicial));
        PanelOperador panel = new PanelOperador();
        SistemaLog log = new SistemaLog();
        AlertaTecnico tecnico = new AlertaTecnico();
        gestor.suscribir(panel);
        gestor.suscribir(log);
        gestor.suscribir(tecnico);
        // Act
        List<Drone> cambios = List.of(EstadoDrone.EN_VUELO, EstadoDrone.ATERRIZANDO,
            EstadoDrone.DISPONIBLE, EstadoDrone.FALLO).stream().map(estado -> gestor.cambiarEstado(inicial.id(), estado)).toList();
        // Assert
        assertAll(
            () -> assertEquals(EstadoDrone.FALLO, panel.estadoDe(inicial.id()).orElseThrow()),
            () -> assertEquals(List.of("D-01 -> EN_VUELO", "D-01 -> ATERRIZANDO", "D-01 -> DISPONIBLE", "D-01 -> FALLO"), log.registros()),
            () -> assertEquals(List.of("FALLO: D-01 requiere revision del tecnico de mantenimiento"), tecnico.alertas()),
            () -> assertEquals(List.of(false, false, true, false), cambios.stream().map(Drone::disponible).toList()),
            () -> assertTrue(cambios.stream().allMatch(drone -> drone.bateria() == 85 && drone.misionesCompletadas() == 7)),
            () -> assertEquals(EstadoDrone.DISPONIBLE, inicial.estado()),
            () -> assertEquals(cambios.get(3), gestor.buscar(inicial.id()).orElseThrow())
        );
    }

    @Test
    void cuartoObservadorLambdaSeNotificaSinModificarGestorFlota() {
        // Arrange
        GestorFlota gestor = new GestorFlota(List.of(inicial));
        gestor.suscribir(new PanelOperador());
        gestor.suscribir(new SistemaLog());
        gestor.suscribir(new AlertaTecnico());
        AtomicReference<Drone> recibido = new AtomicReference<>();
        AtomicReference<EstadoDrone> estadoRecibido = new AtomicReference<>();
        gestor.suscribir((drone, estado) -> { recibido.set(drone); estadoRecibido.set(estado); });
        // Act
        Drone actualizado = gestor.cambiarEstado(inicial.id(), EstadoDrone.EN_VUELO);
        // Assert
        assertAll(() -> assertSame(actualizado, recibido.get()),
            () -> assertEquals(EstadoDrone.EN_VUELO, estadoRecibido.get()), () -> assertFalse(actualizado.disponible()));
    }

    @Test
    void tecnicoNoProduceAlertaParaUnCambioNormal() {
        // Arrange
        GestorFlota gestor = new GestorFlota(List.of(inicial));
        AlertaTecnico tecnico = new AlertaTecnico();
        gestor.suscribir(tecnico);
        // Act
        gestor.cambiarEstado(inicial.id(), EstadoDrone.EN_VUELO);
        // Assert
        assertTrue(tecnico.alertas().isEmpty());
    }

    @Test
    void desuscribirEvitaLaNotificacionYSuscribirDosVecesNoLaDuplica() {
        // Arrange
        GestorFlota gestor = new GestorFlota(List.of(inicial));
        AtomicInteger llamadas = new AtomicInteger();
        ObservadorDrone observador = (drone, estado) -> llamadas.incrementAndGet();
        gestor.suscribir(observador);
        gestor.suscribir(observador);
        // Act
        gestor.cambiarEstado(inicial.id(), EstadoDrone.EN_VUELO);
        int antesDeDesuscribir = llamadas.get();
        gestor.desuscribir(observador);
        gestor.cambiarEstado(inicial.id(), EstadoDrone.ATERRIZANDO);
        // Assert
        assertAll(() -> assertEquals(1, antesDeDesuscribir), () -> assertEquals(1, llamadas.get()));
    }

    @Test
    void mismoEstadoNoEmiteUnCambioInexistente() {
        // Arrange
        GestorFlota gestor = new GestorFlota(List.of(inicial));
        SistemaLog log = new SistemaLog();
        gestor.suscribir(log);
        // Act
        Drone resultado = gestor.cambiarEstado(inicial.id(), EstadoDrone.DISPONIBLE);
        // Assert
        assertAll(() -> assertSame(inicial, resultado), () -> assertTrue(log.registros().isEmpty()));
    }

    @Test
    void rechazaIdInexistenteYFlotaConIdsDuplicados() {
        // Arrange
        GestorFlota gestor = new GestorFlota(List.of(inicial));
        // Act
        Runnable desconocido = () -> gestor.cambiarEstado("D-99", EstadoDrone.FALLO);
        Runnable duplicado = () -> new GestorFlota(List.of(inicial, inicial));
        // Assert
        assertAll(() -> assertThrows(IllegalArgumentException.class, desconocido::run),
            () -> assertThrows(IllegalArgumentException.class, duplicado::run), () -> assertEquals(List.of(inicial), gestor.listar()));
    }
}
