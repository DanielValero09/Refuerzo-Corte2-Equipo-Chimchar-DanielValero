package reto4.report;

import java.util.List;
import java.util.stream.Collectors;
import model.Mission;

public class GeneradorReporte {

    public String generar(List<Mission> misiones) {
        return "Reporte de misiones\nID | Destino | Estado\n"
                + misiones.stream()
                .map(mission -> mission.id() + " | " + mission.destino() + " | " + mission.estado())
                .collect(Collectors.joining("\n"));
    }
}
