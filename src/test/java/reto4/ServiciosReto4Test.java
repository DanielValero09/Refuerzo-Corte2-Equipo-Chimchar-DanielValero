package reto4;

import java.util.List;
import model.ChargeType;
import model.Mission;
import model.MissionState;
import org.junit.jupiter.api.Test;
import reto4.alert.AlertaConsola;
import reto4.alert.AlertaOperador;
import reto4.report.GeneradorReporte;
import reto4.route.EstrategiaRuta;
import reto4.route.RutaDirecta;
import reto4.route.RutaEvitarObstaculos;
import support.ConsoleCapture;

import static org.junit.jupiter.api.Assertions.*;

class ServiciosReto4Test {
    @Test
    void reporteIncluyeIdDestinoYEstadoDeCadaMision() {
        List<Mission> misiones = List.of(
            new Mission("M-01", null, "Bloque A", "Biblioteca", ChargeType.SOBRE, MissionState.PENDIENTE),
            new Mission("M-02", null, "Biblioteca", "Bloque D", ChargeType.CARPETA, MissionState.FALLIDA)
        );
        String reporte = new GeneradorReporte().generar(misiones);
        assertEquals("Reporte de misiones\nID | Destino | Estado\nM-01 | Biblioteca | PENDIENTE\nM-02 | Bloque D | FALLIDA", reporte);
    }

    @Test
    void reporteSinMisionesConservaLaCabecera() {
        assertEquals("Reporte de misiones\nID | Destino | Estado\n", new GeneradorReporte().generar(List.of()));
    }

    @Test
    void estrategiasConservanOrigenYDestinoConRecorridosDiferentes() {
        EstrategiaRuta ruta = new RutaDirecta();
        assertEquals("Bloque A -> Biblioteca", ruta.calcular("Bloque A", "Biblioteca"));
        ruta = new RutaEvitarObstaculos();
        assertEquals("Bloque A -> corredor seguro (evita obstaculos y edificios altos) -> Biblioteca", ruta.calcular("Bloque A", "Biblioteca"));
    }

    @Test
    void alertaInformaOperadorYMensaje() {
        AlertaOperador alerta = new AlertaConsola();
        String salida = ConsoleCapture.ejecutar(() -> alerta.enviar("Operador SkyCampus", "Mision M-01 asignada"));
        assertEquals("Alerta para Operador SkyCampus: Mision M-01 asignada" + System.lineSeparator(), salida);
    }
}
