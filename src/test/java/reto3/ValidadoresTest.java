package reto3;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import model.ChargeType;
import model.Drone;
import model.Mission;
import model.MissionState;
import org.junit.jupiter.api.Test;
import reto3.chain.Validador;
import reto3.chain.ValidadorBateria;
import reto3.chain.ValidadorCarga;
import reto3.chain.ValidadorDestino;

import static org.junit.jupiter.api.Assertions.*;

class ValidadoresTest {
    @Test
    void cadenaAceptaSobreYCarpeta() {
        assertAll(
            () -> assertTrue(cadena().validar(mision(30, "Biblioteca", ChargeType.SOBRE))),
            () -> assertTrue(cadena().validar(mision(91, "Bloque C", ChargeType.CARPETA)))
        );
    }

    @Test
    void cadenaRechazaLibroYNull() {
        assertAll(
            () -> assertFalse(cadena().validar(mision(91, "Biblioteca", ChargeType.LIBRO))),
            () -> assertFalse(cadena().validar(mision(91, "Biblioteca", null)))
        );
    }

    @Test
    void bateriaRechaza29YAcepta30() {
        Validador bateria = new ValidadorBateria();
        assertAll(
            () -> assertFalse(bateria.validar(mision(29, "Biblioteca", ChargeType.SOBRE))),
            () -> assertTrue(bateria.validar(mision(30, "Biblioteca", ChargeType.SOBRE)))
        );
    }

    @Test
    void destinoAceptaSoloLosCincoLugaresDelMvp() {
        Validador destino = new ValidadorDestino();
        for (String lugar : List.of("Bloque A", "Bloque B", "Bloque C", "Bloque D", "Biblioteca")) {
            assertTrue(destino.validar(mision(91, lugar, ChargeType.SOBRE)), lugar);
        }
        assertFalse(destino.validar(mision(91, "Edificio Inexistente", ChargeType.SOBRE)));
    }

    @Test
    void bateriaInvalidaDetieneLaCadenaAntesDelSiguiente() {
        AtomicBoolean siguienteEjecutado = new AtomicBoolean();
        Validador bateria = new ValidadorBateria();
        bateria.setSiguiente(new Validador() {
            @Override
            public boolean validar(Mission mission) {
                siguienteEjecutado.set(true);
                return true;
            }
        });
        assertFalse(bateria.validar(mision(29, "Biblioteca", ChargeType.SOBRE)));
        assertFalse(siguienteEjecutado.get());
        assertTrue(bateria.validar(mision(30, "Biblioteca", ChargeType.SOBRE)));
        assertTrue(siguienteEjecutado.get());
    }

    private Validador cadena() {
        Validador bateria = new ValidadorBateria();
        bateria.setSiguiente(new ValidadorDestino()).setSiguiente(new ValidadorCarga());
        return bateria;
    }

    private Mission mision(int bateria, String destino, ChargeType carga) {
        Drone drone = new Drone("D-03", "DJI Mini 3", bateria, true, "Bloque C");
        return new Mission("M-01", drone, "Bloque C", destino, carga, MissionState.PENDIENTE);
    }
}
