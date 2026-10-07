package reto3.chain;

import model.Mission;

public class ValidadorBateria extends Validador {

    @Override
    public boolean validar(Mission mission) {

        if (mission.drone().battery() < 30) {
            System.out.println("Misión rechazada: batería insuficiente");
            return false;
        }

        return validarSiguiente(mission);
    }
}
