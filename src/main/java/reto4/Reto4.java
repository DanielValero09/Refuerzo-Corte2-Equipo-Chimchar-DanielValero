package reto4;

import model.ChargeType;
import model.Drone;
import model.Mission;
import model.MissionState;
import reto4.alert.AlertaConsola;
import reto4.alert.AlertaOperador;
import reto4.report.GeneradorReporte;
import reto4.repository.RepositorioMision;
import reto4.repository.RepositorioMisionMemoria;
import reto4.route.EstrategiaRuta;
import reto4.route.RutaDirecta;
import reto4.route.RutaEvitarObstaculos;

public class Reto4 {

    public static void main(String[] args) {
        Drone drone = new Drone("D-03", "DJI Mini 3", 91, true, "Bloque C");
        Mission pendiente = new Mission(
                "M-04", null, "Bloque C", "Biblioteca", ChargeType.CARPETA,
                MissionState.PENDIENTE, 1, "Entrega prioritaria", "14:00"
        );

        Mission asignada = new AsignadorMision().asignar(pendiente, drone);
        System.out.println("Mision asignada: " + asignada);

        RepositorioMision repositorio = new RepositorioMisionMemoria();
        repositorio.guardar(asignada);
        Mission recuperada = repositorio.buscarPorId(asignada.id()).orElseThrow();
        System.out.println("Mision guardada y recuperada: " + recuperada.id()
                + " - Drone: " + recuperada.drone().id());

        AlertaOperador alerta = new AlertaConsola();
        alerta.enviar("Operador SkyCampus", "Mision " + recuperada.id()
                + " asignada a " + recuperada.drone().id());

        System.out.println(new GeneradorReporte().generar(repositorio.listar()));

        EstrategiaRuta ruta = new RutaDirecta();
        System.out.println("Ruta directa: " + ruta.calcular(recuperada.origen(), recuperada.destino()));
        ruta = new RutaEvitarObstaculos();
        System.out.println("Ruta evitando obstaculos: "
                + ruta.calcular(recuperada.origen(), recuperada.destino()));
    }
}
