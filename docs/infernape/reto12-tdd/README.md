# Reto 12 - TDD e Integración REST

## Red

Las pruebas se escribieron antes de crear IniciadorMision, EnterpriseApi y la persistencia. La configuración de Spring Boot 3.4.13 ya había compilado y pasado las 186 pruebas anteriores; este fallo corresponde a clases de producción aún ausentes, no a dependencias rotas.

Comando ejecutado el 8 de octubre de 2026:

```text
mvn -B -ntp -Dtest=IniciadorMisionTest,MisionesEndpointTest test
```

Fragmento real:

```text
[ERROR] COMPILATION ERROR :
[ERROR] symbol: class IniciadorMision
[ERROR] symbol: class EnterpriseApi
[ERROR] symbol: class MisionesJpaRepository
[INFO] BUILD FAILURE
[INFO] Finished at: 2026-10-08T16:24:42-05:00
```

Código de salida: 1. El commit RED contiene nueve pruebas unitarias del orquestador y doce pruebas del endpoint, sin su implementación. Commit RED: `920c7e8c35a688b360704bbf99db7fdd9deb7433`.


## Green

Commit: `243ec5a8f61bb3adec089761007c8e4ed7f309a6` — implementación del endpoint, orquestador, puertos y JPA.

```text
mvn -B -ntp -Dtest=IniciadorMisionTest,MisionesEndpointTest test
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 19.70 s -- in infernape.api.MisionesEndpointTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.439 s -- in infernape.application.IniciadorMisionTest
[INFO] Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[INFO] Finished at: 2026-10-08T17:02:54-05:00
```

## Refactor

Commit: `60f00ba176cfce48bda386e429be33ac9614e224`. Se separaron validarOperacion, validarAutorizacion y registrar para que iniciar exprese la secuencia de negocio sin repetir algoritmos. Se añadieron pruebas útiles de política, invariantes y persistencia/aislamiento H2.

```text
mvn -B -ntp clean verify
[INFO] Tests run: 225, Failures: 0, Errors: 0, Skipped: 0
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
```

Después se agregó el manejo centralizado HTTP 503 de una dependencia local indisponible y su prueba específica sin filtrar el detalle técnico.

## Arquitectura y dependencias

- Dominio: SolicitudInicioMision, MisionRegistrada y puertos; solo JDK, sin Spring/JPA/HTTP/H2.
- Aplicación: IniciadorMision inyecta AsignadorMisionEnterprise, AutorizadorRutaInterSede, clima, estado de sedes, repositorio y configuración. PoliticaPrioridadEnterprise implementa la estrategia y mantiene URGENTE/EXPRESS, capacidad, batería y desempate por ID.
- Infraestructura: MisionJpa, MisionesJpaRepository y RepositorioMisionesJpa almacenan realmente en H2; la entidad no contamina el dominio.
- API: DTO tipado/validado, Controller delgado y errores centralizados. EnterpriseApi importa exclusivamente estas piezas.
- Spring Boot 3.4.13 / Spring Framework 6.2.15; H2 gestionado por el BOM. Se conserva Java release 17, JUnit 5.11.4, Mockito 5.15.2, Surefire 3.5.2 y JaCoCo 0.8.11. [Compatibilidad oficial](https://docs.spring.io/spring-boot/3.4/system-requirements.html).
- Tests Spring utilizan `@MockitoBean` de Spring Framework, sin `@MockBean` deprecado. No se agregaron exclusiones de cobertura ni se cambió el check 80% LINE / 70% BRANCH.

## Contrato REST

`POST /api/v3/misiones`:

```json
{"id":"ME-100","origen":"ECI","destino":"UNAL","pesoPaquete":300,"prioridad":"NORMAL","distanciaPlanificadaKm":6,"alturaMetros":100}
```

La distancia es el alcance radial máximo planificado desde la sede de origen, propuesto por el simulador; no una distancia geográfica real ni una suma de etapas. La altura es propuesta y no puede superar los límites locales ni los autorizados. Estos campos son obligatorios: no se inventan silenciosamente cuando faltan. Las sedes deben ser distintas; peso entero positivo, prioridad del enum e ID no vacío de hasta 80 caracteres.

Éxito: 201, ID solicitado, estado EN_VUELO y droneAsignado con ID, tipo y batería procedentes de la selección real. JSON de ejemplo con los datos de test:

```json
{"id":"ME-100","estado":"EN_VUELO","droneAsignado":{"id":"DE-01","tipo":"MINI","bateria":95}}
```

| HTTP | Caso | Código |
|---|---|---|
| 400 | JSON malformado, enum/campo obligatorio inválido o invariantes estructurales | DATOS_INVALIDOS |
| 409 | ID previamente registrado | MISION_DUPLICADA |
| 422 | Clima adverso | CLIMA_ADVERSO |
| 422 | Sin candidatos aptos | NO_DRONES_APTOS |
| 422 | Peso >2000 g | PESO_EXCESIVO |
| 422 | Autorización denegada, ausente o fuera de límites | AEROCIVIL_RECHAZA |
| 422 | Origen o destino inactivo | SEDE_INACTIVA |
| 422 | Configuración local ausente | CONFIGURACION_NO_DISPONIBLE |
| 503 | Dependencia local indisponible o fallo de acceso a datos | DEPENDENCIA_NO_DISPONIBLE |

El error contiene solo codigo y mensaje público, sin stack trace o credenciales. La autorización Aerocivil válida se exige antes de iniciar toda solicitud inter-sede; la falta de verificación bloquea por defecto.

## Tres capas de pruebas

| Capa / archivo | Pruebas nuevas | Qué verifica |
|---|---:|---|
| IniciadorMisionTest (unitaria Mockito) | 9 | Selección delegada, peso, clima, autorización, sedes, prioridad, persistencia y duplicados. |
| PoliticaPrioridadEnterpriseTest (unitaria) | 6 | Batería, EXPRESS urgente, capacidad, CARGO mínimo, sede, disponibilidad y desempate. |
| SolicitudInicioMisionTest (dominio) | 8 | Distancia finita positiva, altura, sedes distintas y peso persistible. |
| PersistenciaMisionIntegrationTest (@SpringBootTest/H2) | 4 | Inserción JDBC real, recuperación por ID, rechazo sin registro, mapeo e aislamiento. |
| MisionesEndpointTest (MockMvc E2E simulado) | 13 | Endpoint hasta H2, cinco alternos, prioridad, JSON inválido, duplicados, límites y 503 seguro. |

Total nuevo: 40. Total final: 226 = 34 Chimchar + 70 Monferno + 82 Infernape anteriores + 40 nuevas. Todas las pruebas nuevas usan AAA; Mockito verifica interacciones relevantes con verify, never y times(1). Las pruebas anteriores de asignación siguen verificando la notificación real del puerto.

Las pruebas H2 consultan `select count(*) from enterprise_misiones` mediante JdbcTemplate y verifican `DatabaseProductName = H2` y URL `jdbc:h2:mem:`. Cada prueba limpia su tabla antes de ejecutarse; los dos contextos usan nombres de BD diferentes. No se llama H2 a una lista simulada.

## Cinco flujos alternos

Clima adverso, sin drones aptos, paquete >2000 g, Aerocivil rechaza y sede inactiva tienen cada uno una prueba específica MockMvc y una prueba unitaria. Todos producen rechazo, cero registros exitosos y ninguna notificación de inicio. Autorización vacía y altura superior a la permitida también bloquean. Un mock puede cambiar el estado de cualquier sede; no existe una regla fija que bloquee UNAL.

## Métricas finales reales

Extraídas de target/site/jacoco/jacoco.xml, sin exclusiones añadidas:

| Ámbito / contador | Covered | Missed | Cobertura |
|---|---:|---:|---:|
| GLOBAL LINE | 747 | 12 | 98.4190% |
| GLOBAL BRANCH | 254 | 22 | 92.0290% |
| INFERNAPE LINE | 378 | 8 | 97.9275% |
| INFERNAPE BRANCH | 155 | 15 | 91.1765% |

Infernape suma los contadores de todos los paquetes cuyo nombre comienza por `infernape/`; no promedia porcentajes. Reporte regenerable: target/site/jacoco/index.html. target/ no se versiona.

## Validación final

```text
mvn -B -ntp clean verify
[INFO] Tests run: 226, Failures: 0, Errors: 0, Skipped: 0
[INFO] --- jacoco:0.8.11:check (check) @ chimchar ---
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
[INFO] Finished at: 2026-10-08T17:54:01-05:00
```

[Extracto real de ejecución](evidencia-maven.txt).

## Límites de la simulación

E2E SIMULADO: no drones físicos, Aerocivil real, HTTP externo ni BD de producción. Los adaptadores por defecto son fail-safe: clima no apto, autorización vacía, sedes inactivas y flota vacía. Los contextos de test reemplazan esos puertos y usan H2 real en memoria. El doble chequeo local de clima conserva la protección del asignador anterior; no implica dos llamadas remotas reales.

El endpoint prueba inicio/asignación regulada y registro; no despliega todo el planificador multi-etapa ni demuestra recuperación transaccional de drones físicos, reserva concurrente de flota o telemetría. La notificación es un puerto local de test; el prototipo interactivo es independiente. La configuración propuesta de 10 km/120 m es académica, no permiso legal real.

No se inició Reto 13, no se cambiaron sus umbrales y no se ejecutó SonarQube.
