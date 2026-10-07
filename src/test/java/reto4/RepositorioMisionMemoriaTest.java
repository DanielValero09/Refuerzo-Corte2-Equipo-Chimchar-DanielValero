package reto4;

import java.util.List;
import model.ChargeType;
import model.Mission;
import model.MissionState;
import org.junit.jupiter.api.Test;
import reto4.repository.RepositorioMision;
import reto4.repository.RepositorioMisionMemoria;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioMisionMemoriaTest {
    @Test
    void guardaRecuperaYActualizaUnaMisionPorId() {
        RepositorioMision repositorio = new RepositorioMisionMemoria();
        Mission pendiente = mision("M-01", MissionState.PENDIENTE);
        Mission entregada = mision("M-01", MissionState.ENTREGADA);
        assertTrue(repositorio.buscarPorId("M-01").isEmpty());
        repositorio.guardar(pendiente);
        assertEquals(pendiente, repositorio.buscarPorId("M-01").orElseThrow());
        repositorio.guardar(entregada);
        assertEquals(entregada, repositorio.buscarPorId("M-01").orElseThrow());
        assertEquals(List.of(entregada), repositorio.listar());
    }

    @Test
    void listarConservaOrdenYDevuelveUnaCopiaInmutable() {
        RepositorioMision repositorio = new RepositorioMisionMemoria();
        Mission primera = mision("M-01", MissionState.PENDIENTE);
        Mission segunda = mision("M-02", MissionState.EN_VUELO);
        repositorio.guardar(primera);
        List<Mission> copia = repositorio.listar();
        repositorio.guardar(segunda);
        assertEquals(List.of(primera), copia);
        assertEquals(List.of(primera, segunda), repositorio.listar());
        assertThrows(UnsupportedOperationException.class, () -> copia.add(segunda));
    }

    @Test
    void rechazaMisionNulaEIdNulo() {
        RepositorioMision repositorio = new RepositorioMisionMemoria();
        assertAll(
            () -> assertThrows(NullPointerException.class, () -> repositorio.guardar(null)),
            () -> assertThrows(NullPointerException.class, () -> repositorio.guardar(mision(null, MissionState.PENDIENTE))),
            () -> assertTrue(repositorio.listar().isEmpty())
        );
    }

    private Mission mision(String id, MissionState estado) {
        return new Mission(id, null, "Bloque A", "Biblioteca", ChargeType.SOBRE, estado);
    }
}
