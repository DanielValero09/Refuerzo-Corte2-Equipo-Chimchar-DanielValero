package reto3.chain;

import model.ChargeType;
import model.Mission;

public class ValidadorCarga extends Validador {

    @Override
    public boolean validar(Mission mission) {

        // El MVP Chimchar representa la capacidad mediante tipos de carga permitidos.
        if (mission.tipoCarga() != ChargeType.SOBRE
                && mission.tipoCarga() != ChargeType.CARPETA) {
            System.out.println("Misión rechazada: tipo de carga inválido");
            return false;
        }

        return validarSiguiente(mission);
    }
}
