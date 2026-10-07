package reto4.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import model.Mission;

public class RepositorioMisionMemoria implements RepositorioMision {

    private final Map<String, Mission> misiones = new LinkedHashMap<>();

    @Override
    public void guardar(Mission mission) {
        Objects.requireNonNull(mission, "La mision es obligatoria");
        Objects.requireNonNull(mission.id(), "El ID de la mision es obligatorio");
        misiones.put(mission.id(), mission);
    }

    @Override
    public Optional<Mission> buscarPorId(String id) {
        return Optional.ofNullable(misiones.get(id));
    }

    @Override
    public List<Mission> listar() {
        return List.copyOf(misiones.values());
    }
}
