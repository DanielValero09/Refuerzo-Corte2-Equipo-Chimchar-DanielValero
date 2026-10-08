# Monferno Reto 3 - Strategy y Observer

Nota de evolución: **Reto 3 exigió las tres estrategias base; Reto 12 extiende el diseño con una política específica para prioridad URGENTE.** EstrategiaUrgenteExpress se incorporó después mediante TDD, en `monferno.reto12`, y no existía durante el cierre del Reto 3. [Evidencia TDD](reto12-tdd.md).

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 03 de Monferno y restricciones del bloque.

## Strategy

`EstrategiaAsignacion` define `Optional<Drone> seleccionar(List<Drone>, Mision)`. El Reto 3 implementó exactamente estas tres estrategias base:

| Implementación | Selección entre aptos | Desempate |
| --- | --- | --- |
| MayorBateriaStrategy | Mayor batería. | ID ascendente. |
| MenorUsoAcumuladoStrategy | Menor misionesCompletadas. | ID ascendente. |
| TipoCompatibleCargaStrategy | Menor capacidad que soporte la carga; puede usar una mayor si falta la preferida. | ID ascendente dentro de la misma capacidad. |

`GestorMisiones` recibe la interfaz por constructor y delega sin instanceof, switch ni nombres de estrategias. `cambiarEstrategia` permite intercambiarla en la misma instancia.

## Validaciones comunes

`CadenaValidacion` compone `ValidadorDrone` mediante allMatch y se detiene en el primer rechazo:

ValidadorBateria → ValidadorDisponibilidad → ValidadorCarga.

- Batería >=30%.
- disponible = true y estado = DISPONIBLE.
- Peso no negativo, garantizado por Mision, y no superior a la capacidad del tipo.
- CARGO no puede utilizarse para un paquete menor de 100 g, incluso como alternativa.

| Peso | Tipo preferido |
| --- | --- |
| 0–500 g | MINI |
| 501–800 g | EXPRESS |
| 801–2000 g | CARGO |
| >2000 g | Ningún candidato: Optional.empty(). |

Las tres estrategias respetan estas validaciones. Un nuevo validador puede componerse mediante el constructor de CadenaValidacion e inyectarse en la estrategia sin editar GestorMisiones. Durante el cierre del Reto 3 las prioridades se conservaban en la misión sin una política adicional de urgencia; esta se incorporó posteriormente en el Reto 12.

## Observer

`ObservadorDrone` define `onEstadoCambiado(Drone, EstadoDrone)`. GestorFlota depende solo de esa interfaz y mantiene la flota actualizada por ID.

| Suscriptor | Resultado observable |
| --- | --- |
| PanelOperador | Conserva el último estado recibido por ID. |
| SistemaLog | Registra todos los cambios en orden. |
| AlertaTecnico | Registra una alerta con ID y necesidad de revisión solo al entrar en FALLO. |

Al cambiar un estado se crea un nuevo Drone, se actualiza la flota y se notifica ese objeto actualizado; se conserva batería, tipo y uso. Solo DISPONIBLE habilita disponible = true. El record no conoce observadores. Repetir el mismo estado no genera un cambio falso; suscribir dos veces no duplica avisos y desuscribir evita avisos posteriores. Los resultados se mantienen en memoria: no se integra una API externa de alertas.

## Pruebas

- `EstrategiasAsignacionTest`: 22 casos, incluidos filtros comunes para las tres estrategias, 99/100 g en CARGO, límites 500/501/800/801/2000/2001, fallback, empate y ausencia de candidatos.
- `GestorFlotaObserverTest`: seis pruebas. La prueba `cuartoObservadorLambdaSeNotificaSinModificarGestorFlota` suscribe los tres observadores y un cuarto mediante lambda; verifica el drone actualizado y el nuevo estado.
- `GestorMisionesStrategyTest`: misma instancia con las tres políticas, implementación local y validador adicional.

Todos los casos usan AAA y comprueban resultados observables.
