package infernape.domain;

import java.util.List;

public interface RepositorioFlota {
    List<DroneEnterprise> findDisponibles(Sede sede);
}
