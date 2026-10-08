package monferno.reto12;

import java.util.List;
import java.util.Optional;
import monferno.model.Drone;
import monferno.model.EstadoDrone;
import monferno.model.EstadoMision;
import monferno.model.Mision;
import monferno.model.Prioridad;
import monferno.model.TipoDrone;
import monferno.reto3.observer.ObservadorDrone;
import monferno.reto3.strategy.EstrategiaAsignacion;
import monferno.support.DatosMonferno;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AsignadorMisionLimitesTest {
    @Mock ApiMeteorologica clima;
    @Mock ObservadorDrone notificador;

    @Test
    void bateriaExactamente30PermiteIniciarVuelo() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        Drone drone = DatosMonferno.drone("D-01", TipoDrone.MINI, 30, 2);
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Drone resultado = asignador.asignar(List.of(drone), mision(200, Prioridad.NORMAL)).orElseThrow();
        // Assert
        assertAll(() -> assertEquals(30, resultado.bateria()), () -> assertFalse(resultado.disponible()));
        verificarInicio(resultado);
    }

    @Test
    void urgenteDe801GramosDescartaExpressYSeleccionaCargoCompatible() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        List<Drone> flota = List.of(DatosMonferno.drone("D-01", TipoDrone.EXPRESS, 95, 0),
            DatosMonferno.drone("D-02", TipoDrone.CARGO, 60, 0));
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Drone resultado = asignador.asignar(flota, mision(801, Prioridad.URGENTE)).orElseThrow();
        // Assert
        assertEquals(TipoDrone.CARGO, resultado.tipo());
        verificarInicio(resultado);
    }

    @Test
    void cargoNoPuedeTransportarPaqueteDe99Gramos() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        Drone cargo = DatosMonferno.drone("D-01", TipoDrone.CARGO, 90, 0);
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Optional<Drone> resultado = asignador.asignar(List.of(cargo), mision(99, Prioridad.NORMAL));
        // Assert
        assertTrue(resultado.isEmpty());
        Mockito.verify(clima).esApto();
        Mockito.verifyNoInteractions(notificador);
    }

    @Test
    void urgenteSinExpressPrefiereMiniCompatibleAntesDeCargoConMasBateria() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        List<Drone> flota = List.of(DatosMonferno.drone("D-01", TipoDrone.CARGO, 95, 0),
            DatosMonferno.drone("D-02", TipoDrone.MINI, 60, 0));
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Drone resultado = asignador.asignar(flota, mision(300, Prioridad.URGENTE)).orElseThrow();
        // Assert
        assertEquals(TipoDrone.MINI, resultado.tipo());
        verificarInicio(resultado);
    }

    @Test
    void empateEntreExpressAptosSeResuelvePorIdAscendente() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        List<Drone> flota = List.of(DatosMonferno.drone("D-09", TipoDrone.EXPRESS, 70, 0),
            DatosMonferno.drone("D-02", TipoDrone.EXPRESS, 70, 9),
            DatosMonferno.drone("D-03", TipoDrone.EXPRESS, 60, 0));
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Drone resultado = asignador.asignar(flota, mision(300, Prioridad.URGENTE)).orElseThrow();
        // Assert
        assertEquals("D-02", resultado.id());
        verificarInicio(resultado);
    }

    @Test
    void prioridadBajoUsaMayorBateriaEnLugarDeRapidez() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        List<Drone> flota = List.of(DatosMonferno.drone("D-01", TipoDrone.MINI, 95, 0),
            DatosMonferno.drone("D-02", TipoDrone.EXPRESS, 70, 0));
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Drone resultado = asignador.asignar(flota, mision(300, Prioridad.BAJO)).orElseThrow();
        // Assert
        assertEquals("D-01", resultado.id());
        verificarInicio(resultado);
    }

    @Test
    void pesoCeroNoConsultaClimaNiNotifica() {
        // Arrange
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        List<Drone> flota = List.of(DatosMonferno.drone("D-01", TipoDrone.MINI, 90, 0));
        // Act
        Optional<Drone> resultado = asignador.asignar(flota, mision(0, Prioridad.NORMAL));
        // Assert
        assertTrue(resultado.isEmpty());
        Mockito.verifyNoInteractions(clima, notificador);
    }

    @Test
    void misionYaEnVueloNoSeVuelveAAsignar() {
        // Arrange
        Drone drone = DatosMonferno.drone("D-01", TipoDrone.MINI, 90, 0);
        Mision enVuelo = new Mision("M-01", Optional.of(drone), "Biblioteca", 300,
            Prioridad.NORMAL, EstadoMision.EN_VUELO, DatosMonferno.AHORA, Optional.empty());
        AsignadorMision asignador = new AsignadorMision(clima, notificador);
        // Act
        Optional<Drone> resultado = asignador.asignar(List.of(drone), enVuelo);
        // Assert
        assertTrue(resultado.isEmpty());
        Mockito.verifyNoInteractions(clima, notificador);
    }

    @Test
    void constructorPermiteInyectarPoliticasSinModificarElAsignador() {
        // Arrange
        Mockito.when(clima.esApto()).thenReturn(true);
        EstrategiaAsignacion normal = Mockito.mock(EstrategiaAsignacion.class);
        EstrategiaAsignacion urgente = Mockito.mock(EstrategiaAsignacion.class);
        Drone drone = DatosMonferno.drone("D-01", TipoDrone.MINI, 90, 0);
        List<Drone> flota = List.of(drone);
        Mision mision = mision(300, Prioridad.NORMAL);
        Mockito.when(normal.seleccionar(flota, mision)).thenReturn(Optional.of(drone));
        AsignadorMision asignador = new AsignadorMision(clima, notificador, normal, urgente);
        // Act
        Drone resultado = asignador.asignar(flota, mision).orElseThrow();
        // Assert
        assertEquals(drone.id(), resultado.id());
        Mockito.verify(normal).seleccionar(flota, mision);
        Mockito.verifyNoInteractions(urgente);
        verificarInicio(resultado);
    }

    private Mision mision(int peso, Prioridad prioridad) {
        return Mision.pendiente("M-01", "Biblioteca", peso, prioridad, DatosMonferno.AHORA);
    }

    private void verificarInicio(Drone drone) {
        assertEquals(EstadoDrone.EN_VUELO, drone.estado());
        Mockito.verify(clima, times(1)).esApto();
        Mockito.verify(notificador, times(1)).onEstadoCambiado(drone, EstadoDrone.EN_VUELO);
        Mockito.verifyNoMoreInteractions(notificador);
    }
}
