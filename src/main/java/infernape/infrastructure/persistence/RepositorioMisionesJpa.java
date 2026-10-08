package infernape.infrastructure.persistence;

import infernape.domain.*;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public class RepositorioMisionesJpa implements RepositorioMisionesEnterprise {
    private final MisionesJpaRepository tabla;
    public RepositorioMisionesJpa(MisionesJpaRepository tabla) { this.tabla = tabla; }
    @Override @Transactional public void guardar(MisionRegistrada mision) {
        tabla.saveAndFlush(new MisionJpa(mision));
    }
    @Override @Transactional(readOnly = true) public Optional<MisionRegistrada> buscarPorId(String id) {
        return tabla.findById(id).map(MisionJpa::aDominio);
    }
}
