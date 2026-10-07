package reto4.repository;

import java.util.List;
import java.util.Optional;
import model.Mission;

public interface RepositorioMision {
    void guardar(Mission mission);

    Optional<Mission> buscarPorId(String id);

    List<Mission> listar();
}
