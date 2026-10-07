package reto3;

import java.util.List;
import model.Drone;
import org.junit.jupiter.api.Test;
import reto3.strategy.AsignacionMayorBateria;
import reto3.strategy.AsignadorDrone;

import static org.junit.jupiter.api.Assertions.*;

class AsignacionMayorBateriaTest {
    @Test
    void seleccionaMayorBateriaDisponibleYDescartaLosNoAptos() {
        Drone elegido = new Drone("D-03", "DJI Mini 3", 91, true, "Bloque C");
        List<Drone> flota = List.of(
            new Drone("D-02", "DJI Mini 3", 99, false, "Biblioteca"),
            new Drone("D-04", "DJI Mini 3", 18, true, "Bloque B"),
            new Drone("D-01", "DJI Mini 3", 85, true, "Bloque A"), elegido
        );
        assertEquals(elegido, new AsignacionMayorBateria().seleccionar(flota).orElseThrow());
    }

    @Test
    void admiteElLimite30YDevuelveVacioSiNoHayAptos() {
        AsignacionMayorBateria estrategia = new AsignacionMayorBateria();
        Drone limite = new Drone("D-01", "DJI Mini 3", 30, true, "Bloque A");
        Drone insuficiente = new Drone("D-04", "DJI Mini 3", 29, true, "Bloque B");
        assertAll(
            () -> assertEquals(limite, estrategia.seleccionar(List.of(limite)).orElseThrow()),
            () -> assertTrue(estrategia.seleccionar(List.of(insuficiente)).isEmpty()),
            () -> assertTrue(estrategia.seleccionar(List.of()).isEmpty())
        );
    }

    @Test
    void asignadorPermiteIntercambiarLaEstrategia() {
        Drone primero = new Drone("D-01", "DJI Mini 3", 85, true, "Bloque A");
        Drone mayor = new Drone("D-03", "DJI Mini 3", 91, true, "Bloque C");
        List<Drone> flota = List.of(primero, mayor);
        AsignadorDrone asignador = new AsignadorDrone(new AsignacionMayorBateria());
        assertEquals(mayor, asignador.asignar(flota).orElseThrow());
        asignador.setEstrategia(drones -> drones.stream().findFirst());
        assertEquals(primero, asignador.asignar(flota).orElseThrow());
    }
}
