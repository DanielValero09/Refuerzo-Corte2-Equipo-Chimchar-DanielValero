package src.main.java.reto3.chain;

import src.main.java.model.Mission;

public class ValidadorCarga extends Validador {

    @Override
    public boolean validar(Mission mission) {

        if (mission.tipoCarga() == null) {
            System.out.println("Misión rechazada: tipo de carga inválido");
            return false;
        }

        return validarSiguiente(mission);
    }
}
