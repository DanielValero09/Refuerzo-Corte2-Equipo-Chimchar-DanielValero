package reto4.alert;

public class AlertaConsola implements AlertaOperador {

    @Override
    public void enviar(String operador, String mensaje) {
        System.out.println("Alerta para " + operador + ": " + mensaje);
    }
}
