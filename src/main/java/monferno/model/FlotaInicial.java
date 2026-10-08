package monferno.model;

import java.util.List;
import java.util.stream.IntStream;

public final class FlotaInicial {
    private FlotaInicial() {
    }

    public static List<Drone> crear() {
        TipoDrone[] tipos = TipoDrone.values();
        return IntStream.rangeClosed(1, 20)
            .mapToObj(numero -> new Drone("D-%02d".formatted(numero), tipos[(numero - 1) % tipos.length],
                60 + numero % 5 * 7, true, EstadoDrone.DISPONIBLE, numero % 6))
            .toList();
    }
}
