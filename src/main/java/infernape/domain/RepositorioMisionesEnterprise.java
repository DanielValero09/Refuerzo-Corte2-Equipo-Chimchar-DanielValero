package infernape.domain;

import java.util.Optional;

public interface RepositorioMisionesEnterprise {
    void guardar(MisionRegistrada mision);
    Optional<MisionRegistrada> buscarPorId(String id);
}
