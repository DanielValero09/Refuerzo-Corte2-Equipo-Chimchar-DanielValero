package reto4.route;

public class RutaEvitarObstaculos implements EstrategiaRuta {

    @Override
    public String calcular(String origen, String destino) {
        return origen + " -> corredor seguro (evita obstaculos y edificios altos) -> " + destino;
    }
}
