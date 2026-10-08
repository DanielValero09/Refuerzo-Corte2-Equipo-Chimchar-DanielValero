package infernape.reto3;

import java.util.ArrayList;
import java.util.List;
import infernape.domain.*;
import infernape.reto3.factory.*;
import infernape.reto3.observer.*;
import infernape.reto3.ruta.*;
import infernape.reto3.strategy.*;
import org.junit.jupiter.api.Test;
import static infernape.support.DatosEnterprise.*;
import static org.junit.jupiter.api.Assertions.*;

class ObserverYCombinacionTest {
    @Test
    void estrategiaCompositeFactoryYObserverSeCombinanSinGodObject() {
        // Arrange
        MonitorRed monitor = new MonitorRed();
        RegistroEventos registro = new RegistroEventos();
        GestorEjecucionRuta ejecucion = new GestorEjecucionRuta(List.of(monitor, registro));
        PlanificadorRuta planificador = new PlanificadorRuta(new MenorDistanciaStrategy());
        // Act
        Ruta ruta = planificador.planificar(List.of(simple(ECI, UNAL, 10), viaCarga())).orElseThrow();
        EtapaRuta etapa = ruta.etapas().get(0);
        DroneEnterprise drone = new CreadorExpress().crear("D-01", Sede.ECI, 80, 300, etapa).orElseThrow();
        ejecucion.publicar(etapa, drone, EstadoEtapa.INICIADA);
        // Assert
        assertEquals(2, ruta.etapas().size());
        assertEquals(6, ruta.distanciaKm());
        assertEquals(PerfilDrone.EXPRESS, drone.perfil());
        assertEquals(List.of(monitor.ultimoEvento("D-01").orElseThrow()), registro.eventos());
    }

    @Test
    void tercerObservadorLambdaSeAgregaSinModificarElGestor() {
        // Arrange
        MonitorRed monitor = new MonitorRed();
        RegistroEventos registro = new RegistroEventos();
        List<EventoEtapa> tercero = new ArrayList<>();
        GestorEjecucionRuta gestor = new GestorEjecucionRuta(List.of(monitor, registro));
        gestor.suscribir(tercero::add);
        // Act
        gestor.publicar(viaCarga().etapas().get(0), drone("D", Sede.ECI, 90, true), EstadoEtapa.EN_CARGA);
        // Assert
        assertEquals(registro.eventos(), tercero);
        assertEquals(EstadoEtapa.EN_CARGA, monitor.ultimoEvento("D").orElseThrow().estado());
        assertTrue(monitor.ultimoEvento("INEXISTENTE").isEmpty());
    }

    @Test
    void duplicadoYDesuscripcionNoProducenAvisosAdicionales() {
        // Arrange
        RegistroEventos registro = new RegistroEventos();
        GestorEjecucionRuta gestor = new GestorEjecucionRuta(List.of(registro));
        gestor.suscribir(registro);
        EtapaRuta etapa = viaCarga().etapas().get(0);
        DroneEnterprise drone = drone("D", Sede.ECI, 90, true);
        // Act
        gestor.publicar(etapa, drone, EstadoEtapa.COMPLETADA);
        gestor.desuscribir(registro);
        gestor.publicar(etapa, drone, EstadoEtapa.FALLIDA);
        // Assert
        assertEquals(List.of(new EventoEtapa(etapa, drone, EstadoEtapa.COMPLETADA)), registro.eventos());
        assertThrows(UnsupportedOperationException.class, () -> registro.eventos().clear());
    }
}
