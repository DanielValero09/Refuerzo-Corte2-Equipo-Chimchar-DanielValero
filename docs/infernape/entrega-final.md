# Estado final de Infernape — auditoría de entrega

Auditoría del 8 de octubre de 2026 sobre base 428a3c6, exclusivamente evolution/infernape. Fuente académica: DOSW_Equipo_Chimchar_fixed.html. **No se declara 14/14 ni cierre total**: quedan evidencias humanas/visuales y Sonar pendiente por decisión del usuario.

## Checklist de los 14 retos

| Reto | Estado | Evidencia | Observación |
|---|---|---|---|
| 1 | COMPLETO | [Analytics](reto1-analytics.md), [AnalyticsRedTest](../../src/test/java/infernape/reto1/AnalyticsRedTest.java) | Una pasada de misiones, cuatro métricas, Optional por sede vacía, pruebas parametrizadas y desempate por ID. |
| 2 | COMPLETO | [Release/hotfix/grafo/tags](reto2-gitflow/README.md) | Historia académica real preservada; main/develop reales intactos. |
| 3 | COMPLETO | [Cuatro patrones y diagramas](reto3-patrones/README.md) | Composite, Strategy, Observer, Factory Method; escenario combinado probado. |
| 4 | COMPLETO | [Capas y SOLID](reto4-solid/README.md), [Mockito](../../src/test/java/infernape/application/AsignadorMisionEnterpriseTest.java) | Dominio JDK sin Spring/JPA; inyección por constructor y puertos. |
| 5 | COMPLETO | [C4 niveles 1/2](reto5-c4/README.md) | Diagramas de contexto/arquitectura objetivo; no se afirma despliegue de todos los contenedores. |
| 6 | COMPLETO | [8 RF, 4 RNF y trazabilidad](reto6-trazabilidad.md) | Cada RF tiene CU/HU/prueba real. RNF-08 y RNF-10 son objetivos medibles documentados, todavía no benchmarks ejecutados; RNF-11 usa demora simulada, no red. |
| 7 | COMPLETO | [DOSW SC-15](reto7-dosw-sc15.md) | Contrato tipado, seis pasos, tres alternos y cinco reglas; no demuestra ejecución física completa. |
| 8 | COMPLETO | [Tokens/componentes multi-sede](reto8-ux/README.md) | Cuatro temas oficiales, estructuras reutilizables y contraste medido; no certificación total WCAG. |
| 9 | COMPLETO — captura Jira pendiente | [Roadmap real](reto9-jira/README.md) | Tres sprints futuros y 45 SP informados por el usuario; captura auténtica pendiente. No se afirma nueva consulta de Jira. |
| 10 | COMPLETO | [CU UML](reto10-casos-uso/README.md) | Cinco paquetes, dos herencias, tres include y dos extend condicionados; CU-E01–08 estables. |
| 11 | PARCIAL — falta prueba con compañero | [Prototipo navegable](reto11-prototipo/README.md), [plantilla manual](reto11-prototipo/prueba-usabilidad.md) | 26 verificaciones Chrome repetidas; la observación auténtica de un compañero permanece pendiente. |
| 12 | COMPLETO | [TDD, H2 y MockMvc](reto12-tdd/README.md) | Tres capas de pruebas reales, cinco alternos y commits RED/GREEN/REFACTOR verificables. Dos regresiones nuevas protegen la persistencia antes de notificar. |
| 13 | PARCIAL — JACOCO COMPLETO / SONAR PENDIENTE | [Métricas, captura y gate local](reto13-jacoco/README.md) | Global LINE 98.4273%, BRANCH 92.0290%; Sonar no ejecutado. |
| 14 | PENDIENTE — SONARQUBE | [Pendiente técnico](reto14-sonarqube/README.md) | Sin análisis, bugs/deuda/duplicación/gate ni capturas Sonar inventados. |

## Integración REST y límites

POST /api/v3/misiones recorre DTO/API → IniciadorMision → puertos, asignador/autorizador y persistencia JPA/H2 local. Unitarias usan Mockito; integración consulta H2 por JDBC; MockMvc es E2E SIMULADO. No hay drones físicos, BD productiva ni llamadas HTTP externas en la suite.

| Aspecto auditado | Evidencia y resultado |
|---|---|
| Autorización antes de iniciar | IniciadorMision valida límites/permiso antes de asignar. Ausencia, rechazo o altura fuera de permiso bloquean. |
| Clima y sedes | Clima adverso y origen/destino inactivo impiden registro y notificación; el estado se configura mediante un puerto. |
| Sin drones / peso | Candidatos aptos por sede, batería/capacidad/disponibilidad; peso >2000 g rechazado antes de selección. |
| Duplicados | ID existente devuelve 409 sin segunda notificación; comprobación secuencial y PK en BD. No se prueba reserva concurrente/idempotencia distribuida. |
| HTTP | 201 éxito, 400 datos estructurales, 409 duplicado secuencial, 422 regla incumplida, 503 dependencia simulada/almacenamiento no disponible, sin stack trace público. |
| H2 real | PersistenciaMisionIntegrationTest verifica producto H2, URL jdbc:h2:mem, inserción, recuperación/mapeo y aislamiento. |
| Notificaciones | Asignador autónomo conserva su contrato anterior. El cableado REST delega la emisión efectiva a Iniciador tras guardar. |

### Riesgo reproducido y corrección mínima

Antes, AsignadorMisionEnterprise notificaba durante la selección y después Iniciador intentaba guardar. Al hacer que el puerto de almacenamiento lanzara DataAccessResourceFailureException, el evento ya había salido. Dos pruebas nuevas escritas primero fallaron realmente: NeverWantedButInvoked y VerificationInOrderFailure; [extractos reales RED/GREEN](reto13-jacoco/evidencia-maven.txt).

- Commit de pruebas primero: `ce3a6f88456cc5fcbfecdd8cad0b83061f07e368`.
- Corrección: `8d54e4cc29db6b82aca8e757c507d87cbbcb4845`; solo IniciadorMision y ConfiguracionEnterpriseLocal cambian en producción.
- La configuración REST usa un observador sin efectos en el asignador; el iniciador recibe el observador real por constructor y lo invoca después de guardar. Se conserva el constructor previo para compatibilidad. La garantía de orden documentada aplica al cableado REST, no a cualquier construcción manual del asignador antiguo.
- [NotificacionPersistenciaTest](../../src/test/java/infernape/api/NotificacionPersistenciaTest.java) verifica respuesta 503 sin evento cuando falla el almacenamiento y éxito 201 con guardar antes del evento exactamente una vez. Las 226 pruebas previas no se editaron.

**Limitaciones transaccionales pendientes, fuera del alcance académico:** persistir y publicar un evento externo no son una transacción atómica. Si el proceso cae o el observador falla después del commit, puede quedar misión persistida sin evento o respuesta de error aunque el registro exista. Harían falta outbox, reintentos/idempotencia y reserva concurrente de flota para entrega garantizada. No se implementan ni se anuncian como resueltos. Una carrera de dos solicitudes con el mismo ID tampoco está certificada por el test secuencial; el mapeo HTTP de esa carrera no se demuestra. El repositorio de flota actual devuelve snapshots, sin reserva física/persistente.

## Prototipo y evidencia humana

`node docs/infernape/reto11-prototipo/pruebas-navegador.cjs` se repitió en Chrome headless real el 2026-10-08T23:25:54.835Z: **26 aprobadas / 0 fallos**. [JSON auténtico de esta ejecución](reto13-jacoco/evidencia-navegador.json). Cuatro sedes, tokens ECI/UNAL, navegación/vuelta, confirmación/eventos locales, seis errores y recuperación, teclado y viewport móvil 390 px pasan. Datos claramente simulados; no telemetría real de 100 drones ni conexión al endpoint desde este mock.

La prueba real con compañero sigue PENDIENTE. [Plantilla preparada](reto11-prototipo/prueba-usabilidad.md); no se inventan personas, tiempos, preguntas ni resultados humanos.

## Jira y planificación

Épica real SCRUM-19, ocho HU SCRUM-20–27. S1 Conectividad ID4: 19 SP (21,23,25,26); S2 Rutas ID5: 13 SP (22,24); S3 Analytics ID6: 13 SP (20,27). Total **45 SP**; capacidad estimada 20 SP por sprint, no velocidad histórica. Sprints futuros; completados no medidos. La información real proviene del usuario y no se altera Jira en este bloque. Captura auténtica pendiente; no existe PNG de Jira fabricado.

## Calidad e integridad

Maven: 228 pruebas = 34 Chimchar + 70 Monferno + 124 Infernape; failures/errors/skipped 0, BUILD SUCCESS y check BUNDLE LINE ≥85% / BRANCH ≥75% aprobado. [Métricas reales](reto13-jacoco/metricas-finales.json), [PNG auténtico JaCoCo](reto13-jacoco/reporte-jacoco-infernape.png), [extractos Maven](reto13-jacoco/evidencia-maven.txt).

[Auditoría automatizada de repositorio](reto13-jacoco/auditoria-repositorio.json) registra enlaces Markdown, XML de todos los SVG/Draw.io, paquetes/rutas, commits citados y refs protegidas. No hay secretos detectados por los patrones revisados, generados versionados, packages src.main.java ni imports prohibidos en dominio. Los artefactos target/ son locales e ignorados. No se cambian las fuentes/pruebas Chimchar/Monferno, ni los tags anotados v3.0.0/v3.0.1. git diff --check pasa. La búsqueda de patrones no constituye un análisis Sonar ni una auditoría de seguridad exhaustiva.

## Pendientes reales de entrega

- SonarQube Chimchar, Monferno e Infernape: decisión del usuario de no ejecutarlo; requerirá autorización futura para análisis y evidencias.
- Capturas auténticas Jira de las evoluciones; los proyectos/planificaciones reales no se presentan como inexistentes.
- Observación auténtica del prototipo por un compañero y registro de hallazgos.

Los benchmarks RNF-08/10 y la robustez distribuida descrita arriba son límites técnicos de alcance documentados, no resultados obtenidos ni requisitos humanos inventados. No se crea PR, merge o tag final.
