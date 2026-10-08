package infernape.api;

import infernape.domain.*;
import jakarta.validation.constraints.*;

public record MisionRequest(@NotBlank @Size(max=80) String id, @NotNull Sede origen, @NotNull Sede destino,
    @Min(1) int pesoPaquete, @NotNull Prioridad prioridad,
    @Positive double distanciaPlanificadaKm, @Min(1) @Max(500) int alturaMetros) {
    public SolicitudInicioMision aDominio() {
        return new SolicitudInicioMision(new SolicitudAsignacion(id, origen, destino, pesoPaquete, prioridad), distanciaPlanificadaKm, alturaMetros);
    }
}
