package infernape.enterprise;

import infernape.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionOperacionSedeTest {
    @ParameterizedTest
    @CsvSource({"4,120,true", "4,121,false", "4.1,80,false", "3,0,false", "NaN,30,false", "0,30,false"})
    void respetaRadioLocalYAlturaRegulatoria(double distancia, int altura, boolean esperado) {
        // Arrange
        var configuracion = new ConfiguracionOperacionSede(Sede.ECI, 4, new LimitesOperacionRegulada(5, 120));
        // Act
        boolean permitido = configuracion.permite(distancia, altura);
        // Assert
        assertEquals(esperado, permitido);
    }

    @Test
    void coordinadorPuedeAjustarRadioSinCambiarRestricciones() {
        // Arrange
        var original = new ConfiguracionOperacionSede(Sede.ECI, 3, new LimitesOperacionRegulada(5, 100));
        // Act
        var nueva = original.conRadioMaximo(4);
        // Assert
        assertTrue(nueva.permite(4, 100));
        assertFalse(original.permite(4, 100));
        assertEquals(original.limites(), nueva.limites());
    }

    @Test
    void configuracionLocalNuncaAmpliaElRadioAutorizado() {
        // Arrange
        var configuracion = new ConfiguracionOperacionSede(Sede.UNAL, 3, new LimitesOperacionRegulada(4, 120));
        // Act
        var intento = (org.junit.jupiter.api.function.Executable) () -> configuracion.conRadioMaximo(5);
        // Assert
        assertThrows(IllegalArgumentException.class, intento);
    }

    @Test
    void restriccionMasEstricaDeAlturaPrevaleceSobreElMaximoUrbano() {
        // Arrange
        var configuracion = new ConfiguracionOperacionSede(Sede.EAFIT, 4, new LimitesOperacionRegulada(5, 80));
        // Act
        boolean permitido = configuracion.permite(3, 81);
        // Assert
        assertFalse(permitido);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 121})
    void noSePuedeDesactivarNiSuperarElLimiteDeAltura(int altura) {
        // Arrange
        var intento = (org.junit.jupiter.api.function.Executable) () -> new LimitesOperacionRegulada(5, altura);
        // Act
        var error = assertThrows(IllegalArgumentException.class, intento);
        // Assert
        assertTrue(error.getMessage().contains("Altura"));
    }
}
