package infernape.api;

import infernape.infrastructure.ConfiguracionEnterpriseLocal;
import infernape.infrastructure.persistence.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = MisionJpa.class)
@EnableJpaRepositories(basePackageClasses = MisionesJpaRepository.class)
@Import({ConfiguracionEnterpriseLocal.class, MisionesController.class, ErroresApi.class})
public class EnterpriseApi { }
