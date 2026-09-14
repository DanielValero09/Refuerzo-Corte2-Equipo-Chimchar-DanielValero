package src.main.java.reto3.chain;

import src.main.java.model.Mission;

public abstract class Validador {

    protected Validador siguiente;

    public Validador setSiguiente(Validador siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public abstract boolean validar(Mission mission);

    protected boolean validarSiguiente(Mission mission) {

        if (siguiente == null) {
            return true;
        }

        return siguiente.validar(mission);
    }
}
