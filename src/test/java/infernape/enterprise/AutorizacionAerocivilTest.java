package infernape.enterprise;

import java.time.Duration;
import java.util.Optional;
import infernape.application.AutorizadorRutaInterSede;
import infernape.domain.*;
import infernape.infrastructure.ServicioAerocivilSimulado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static infernape.support.DatosEnterprise.solicitud;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutorizacionAerocivilTest {
    @Mock ServicioAerocivil aerocivil;

    @Test
    void aceptaAutorizacionRespetandoLosLimitesYElPuertoInyectado() {
        // Arrange
        when(aerocivil.verificar(Sede.ECI, Sede.UNAL, Duration.ofSeconds(2))).thenReturn(condiciones(true, 120));
        // Act
        boolean autorizada = new AutorizadorRutaInterSede(aerocivil).autorizar(solicitud(), configuracion(), 3, 120);
        // Assert
        assertTrue(autorizada);
        verify(aerocivil).verificar(Sede.ECI, Sede.UNAL, Duration.ofSeconds(2));
    }

    @Test
    void rechazoDeAerocivilImpideAutorizacion() {
        // Arrange
        when(aerocivil.verificar(Sede.ECI, Sede.UNAL, Duration.ofSeconds(2))).thenReturn(condiciones(false, 0));
        // Act
        boolean autorizada = new AutorizadorRutaInterSede(aerocivil).autorizar(solicitud(), configuracion(), 3, 80);
        // Assert
        assertFalse(autorizada);
    }

    @Test
    void verificacionNoDisponibleBloqueaEnLugarDeAsumirPermiso() {
        // Arrange
        when(aerocivil.verificar(Sede.ECI, Sede.UNAL, Duration.ofSeconds(2))).thenReturn(Optional.empty());
        // Act
        boolean autorizada = new AutorizadorRutaInterSede(aerocivil).autorizar(solicitud(), configuracion(), 3, 80);
        // Assert
        assertFalse(autorizada);
        verify(aerocivil).verificar(Sede.ECI, Sede.UNAL, Duration.ofSeconds(2));
    }

    @Test
    void permisoConAlturaMenorTambienLimitaLaOperacion() {
        // Arrange
        when(aerocivil.verificar(Sede.ECI, Sede.UNAL, Duration.ofSeconds(2))).thenReturn(condiciones(true, 80));
        // Act
        boolean autorizada = new AutorizadorRutaInterSede(aerocivil).autorizar(solicitud(), configuracion(), 3, 81);
        // Assert
        assertFalse(autorizada);
    }

    @Test
    void radioOAlturaLocalInvalidosNoPuedenAutorizarse() {
        // Arrange
        var autorizador = new AutorizadorRutaInterSede(aerocivil);
        // Act
        boolean altura = autorizador.autorizar(solicitud(), configuracion(), 3, 121);
        boolean radio = autorizador.autorizar(solicitud(), configuracion(), 6, 80);
        // Assert
        assertFalse(altura);
        assertFalse(radio);
        verifyNoInteractions(aerocivil);
    }

    @Test
    void configuracionDeOtraSedeNoSeUsaParaAutorizar() {
        // Arrange
        var otra = new ConfiguracionOperacionSede(Sede.UNAL, 4, new LimitesOperacionRegulada(5, 120));
        // Act
        boolean autorizada = new AutorizadorRutaInterSede(aerocivil).autorizar(solicitud(), otra, 3, 80);
        // Assert
        assertFalse(autorizada);
        verifyNoInteractions(aerocivil);
    }

    @Test
    void servicioInterSedeNoConfundeUnaRutaEnLaMismaSede() {
        // Arrange
        var local = new SolicitudAsignacion("M-LOCAL", Sede.ECI, Sede.ECI, 200, Prioridad.NORMAL);
        // Act
        boolean autorizada = new AutorizadorRutaInterSede(aerocivil).autorizar(local, configuracion(), 3, 80);
        // Assert
        assertFalse(autorizada);
        verifyNoInteractions(aerocivil);
    }

    @ParameterizedTest
    @CsvSource({"1000,true", "2000,true", "2001,false"})
    void timeoutLocalDeDosSegundosEsFailSafeSinEsperarNiUsarRed(long demoraMs, boolean esperado) {
        // Arrange
        var simulado = new ServicioAerocivilSimulado(Duration.ofMillis(demoraMs), condiciones(true, 120));
        // Act
        boolean autorizada = new AutorizadorRutaInterSede(simulado).autorizar(solicitud(), configuracion(), 3, 80);
        // Assert
        assertEquals(esperado, autorizada);
    }

    private static Optional<CondicionesEspacioAereo> condiciones(boolean autorizado, int altura) {
        return Optional.of(new CondicionesEspacioAereo(autorizado, altura, Optional.of("Escenario local")));
    }

    private static ConfiguracionOperacionSede configuracion() {
        return new ConfiguracionOperacionSede(Sede.ECI, 4, new LimitesOperacionRegulada(5, 120));
    }
}
