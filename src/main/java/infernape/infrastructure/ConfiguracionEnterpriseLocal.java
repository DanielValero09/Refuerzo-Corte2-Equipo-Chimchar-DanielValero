package infernape.infrastructure;

import infernape.application.*;
import infernape.domain.*;
import infernape.infrastructure.persistence.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.context.annotation.*;

@Configuration(proxyBeanMethods = false)
public class ConfiguracionEnterpriseLocal {
    @Bean public ServicioClima clima() { return new ServicioClimaConfigurable(false); }
    @Bean public ServicioAerocivil aerocivil() { return new ServicioAerocivilSimulado(Duration.ZERO, Optional.empty()); }
    @Bean public RepositorioFlota flota() { return new RepositorioFlotaMemoria(List.of()); }
    @Bean public RepositorioEstadoSede sedes() { return sede -> false; }
    @Bean public ObservadorAsignacion observador() { return (solicitud, drone) -> { }; }
    @Bean public EstrategiaAsignacionEnterprise estrategia() { return new PoliticaPrioridadEnterprise(); }
    @Bean public RepositorioMisionesEnterprise misiones(MisionesJpaRepository tabla) { return new RepositorioMisionesJpa(tabla); }
    @Bean public AsignadorMisionEnterprise asignador(RepositorioFlota flota, ServicioClima clima,
        EstrategiaAsignacionEnterprise estrategia) {
        // El flujo REST emite el evento desde IniciadorMision, después de guardar.
        return new AsignadorMisionEnterprise(flota, clima, estrategia, (solicitud, drone) -> { });
    }
    @Bean public AutorizadorRutaInterSede autorizador(ServicioAerocivil aerocivil) { return new AutorizadorRutaInterSede(aerocivil); }
    @Bean public IniciadorMision iniciador(AsignadorMisionEnterprise asignador, AutorizadorRutaInterSede autorizador,
        ServicioClima clima, RepositorioEstadoSede sedes, RepositorioMisionesEnterprise misiones,
        ObservadorAsignacion observador) {
        var configs = Arrays.stream(Sede.values()).collect(Collectors.toMap(sede -> sede,
            sede -> new ConfiguracionOperacionSede(sede, 10, new LimitesOperacionRegulada(10, 120))));
        return new IniciadorMision(asignador, autorizador, clima, sedes, misiones, configs, observador);
    }
}
