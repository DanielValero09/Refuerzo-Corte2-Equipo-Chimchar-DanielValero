package src.main.java.reto3.chain;

import src.main.java.model.Mission;

import java.util.List;

public class ValidadorDestino extends Validador {

    private final List<String> destinosValidos = List.of(
            "Bloque A",
            "Bloque B",
            "Bloque C",
            "Bloque D",
            "Biblioteca"
    );

    @Override
    public boolean validar(Mission mission) {

        if (!destinosValidos.contains(mission.destino())) {
            System.out.println("Misión rechazada: destino inválido");
            return false;
        }

        return validarSiguiente(mission);
    }
}
