package reto4.route;

public class RutaDirecta implements EstrategiaRuta {

    @Override
    public String calcular(String origen, String destino) {
        return origen + " -> " + destino;
    }
}
