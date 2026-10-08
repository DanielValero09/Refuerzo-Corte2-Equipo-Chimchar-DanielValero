# Reto 6 - Matriz de trazabilidad Enterprise

Fuente: HTML oficial Infernape 06 y requisitos precisados por el usuario. Los códigos Jira son los **reales suministrados por el usuario** para la épica **SCRUM-19 — SkyCampus Enterprise — Red multi-sede**. No se crearon issues ni duplicados y no se afirma una nueva verificación de Jira por API en este bloque.

## Ocho requerimientos funcionales

| Código / nombre | Actor, acción y resultado observable | MoSCoW | CU estable | HU real | Prueba existente y verificable |
| --- | --- | --- | --- | --- | --- |
| RF-11 — Consultar analytics por sede | Superadministrador consulta tasa de éxito, promedio de entregas, drone más utilizado y porcentaje urgentes; obtiene métricas por sede u ausencia de actividad. | MUST | CU-E01 — Consultar analytics por sede | SCRUM-20 | [AnalyticsRedTest](../../src/test/java/infernape/reto1/AnalyticsRedTest.java), escenarios parametrizados y promedio solo ENTREGADAS |
| RF-12 — Configurar radio máximo de vuelo por sede | Coordinador ajusta el radio de la sede; obtiene una configuración inmutable válida que nunca supera restricciones obligatorias. | MUST | CU-E02 — Configurar radio máximo de vuelo | SCRUM-21 | [ConfiguracionOperacionSedeTest](../../src/test/java/infernape/enterprise/ConfiguracionOperacionSedeTest.java), ajuste válido y rechazo del radio no autorizado |
| RF-13 — Planificar ruta multi-etapa inter-sede | Operador solicita origen, etapas, estación y destino; recibe una ruta continua seleccionada mediante política. | MUST | CU-E03 — Planificar ruta multi-etapa | SCRUM-22 | [RutasYStrategyTest](../../src/test/java/infernape/reto3/RutasYStrategyTest.java), Composite anidado, continuidad y selección por Strategy |
| RF-14 — Validar autorización de Aerocivil | Operador solicita autorización antes del inicio inter-sede; recibe autorización válida o bloqueo por rechazo/verificación ausente. | MUST | CU-E04 — Autorizar ruta con Aerocivil | SCRUM-23 | [AutorizacionAerocivilTest](../../src/test/java/infernape/enterprise/AutorizacionAerocivilTest.java), permiso/rechazo/ausencia/límites y timeout local |
| RF-15 — Gestionar estaciones de carga intermedias | Coordinador considera el estado de estaciones; una estación no disponible invalida la alternativa que la necesita. | MUST | CU-E05 — Gestionar estación de carga | SCRUM-24 | [PlanificadorRutaHotfixTest](../../src/test/java/infernape/reto3/PlanificadorRutaHotfixTest.java), estación cerrada y ausencia de ruta operable |
| RF-16 — Transferir drones entre sedes | Coordinador transfiere un drone disponible a otra sede; recibe un nuevo drone con adscripción cambiada, sin mutar el original. | SHOULD | CU-E06 — Transferir drone entre sedes | SCRUM-25 | [GestorTransferenciaFlotaTest](../../src/test/java/infernape/enterprise/GestorTransferenciaFlotaTest.java), transferencia válida, ocupado y origen=destino |
| RF-17 — Asignar drone desde la flota compartida | Operador obtiene candidatos del RepositorioFlota para la sede origen y delega elección en EstrategiaAsignacionEnterprise; recibe candidato válido y notificación. | MUST | CU-E07 — Asignar drone de flota compartida | SCRUM-26 | [AsignadorMisionEnterpriseTest](../../src/test/java/infernape/application/AsignadorMisionEnterpriseTest.java), consulta de sede, Strategy inyectada y notificación |
| RF-18 — Visualizar estado global de la red | Superadministrador consulta cuatro sedes, drones, misiones activas y alertas; recibe un resumen inmutable y estado general. | SHOULD | CU-E08 — Ver dashboard Enterprise | SCRUM-27 | [ResumenRedTest](../../src/test/java/infernape/enterprise/ResumenRedTest.java), red completa/vacía, alertas explícitas y snapshot |

MUST protege el núcleo operativo y regulatorio. SHOULD permite incorporar transferencia y vista agregada después del núcleo, conservando pruebas útiles desde este bloque. Los ocho códigos CU y sus nombres quedan reservados para Reto 10; aquí no se dibuja todavía el CU completo.

## Cuatro requerimientos no funcionales

RNF-09 está explícitamente definido por el material académico. Los otros tres concretan el contexto Enterprise mediante objetivos medibles; no son métricas ya obtenidas ni afirmaciones de normativa vigente fuera del ejercicio.

| Código / nombre | Condición medible | MoSCoW | Verificación y estado |
| --- | --- | --- | --- |
| RNF-08 — Rendimiento de analytics | Calcular métricas de las cuatro sedes en menos de 1 segundo en el escenario local de hasta 100 drones. | SHOULD | Verificación futura con JUnit assertTimeout; aún no se declara un benchmark de 100 drones. |
| RNF-09 — Restricción Aerocivil | Respetar espacio aéreo regulado; ningún drone supera 120 m en zona urbana, según el enunciado. Una restricción menor también prevalece. | MUST | ConfiguracionOperacionSedeTest verifica 120 permitido, 121 rechazado y restricción de 80 m; AutorizacionAerocivilTest verifica límites del permiso. |
| RNF-10 — Rendimiento de planificación | Selección de rutas candidatas en menos de 1 segundo en el escenario local de pruebas. | SHOULD | Verificación futura con JUnit assertTimeout; no se presenta como medición ya realizada. |
| RNF-11 — Fail-safe regulatorio | Si no puede verificarse la autorización/restricción, no autorizar el inicio inter-sede. Plazo local documentado: 2 segundos. | MUST | AutorizacionAerocivilTest usa Mockito y simulación de demoras 1000/2000/2001 ms; 2001 bloquea. No hay HTTP ni espera real. |

## Tensión RF-12 / RNF-09

El Coordinador configura parámetros locales, pero las restricciones regulatorias tienen prioridad. Radio horizontal y altura vertical son parámetros distintos: el radio local no supera el radio autorizado del snapshot regulatorio, y la altura respeta el menor límite aplicable, nunca más de 120 m urbanos en el ejercicio. No existe una opción para desactivar RNF-09.

ConfiguracionOperacionSede conserva LimitesOperacionRegulada; conRadioMaximo valida cada cambio. AutorizadorRutaInterSede también aplica la altura del permiso devuelto por el puerto. Si no hay permiso verificable, bloquea incluso con una configuración local válida.

## Tensión RF-13 / RF-15

Una ruta más corta deja de ser candidata si requiere una estación cerrada. **Operabilidad y seguridad prevalecen sobre optimización.** El hotfix real ya aplica ese filtro antes de Strategy: [evidencia, pruebas y hashes](reto2-gitflow/README.md).

## Implementación mínima y límites

- Configuración: records inmutables, límites subordinados y ajuste de radio; sin UI ni almacenamiento real.
- Aerocivil: puerto de dominio y AutorizadorRutaInterSede con constructor injection. ServicioAerocivilSimulado compara una demora declarada con el plazo de 2 segundos y devuelve Optional.empty fuera de plazo; no mide ni espera una red real. Un futuro adaptador debe imponer su timeout real y traducir la imposibilidad de verificar a ausencia de permiso.
- Transferencia: operación lógica de adscripción, disponible y origen distinto de destino. No implica vuelo físico, consumo de batería ni persistencia automática.
- Resumen: cuenta drones/disponibles por sede, misiones PENDIENTE o EN_VUELO y alertas aportadas explícitamente; indisponible no se inventa como FALLO. Incluye las cuatro sedes aunque estén vacías.
- Flota compartida: la consulta existente está segmentada por sede origen y valida pertenencia; este bloque no inventa selección automática de candidatos de otra sede.

La autorización regulatoria es una guardia independiente y testeable para el inicio inter-sede; no se afirma una orquestación completa de vuelo que ya conecte todos los servicios. Se conservan íntegros los 157 tests y la producción anterior. Todas las referencias de la matriz apuntan a pruebas reales; no queda RF sin CU, HU o prueba.

## Validación real del bloque

`mvn clean verify`, 2026-10-08T15:54:27-05:00: 186 pruebas (157 previas + 29 nuevas), Failures 0, Errors 0, Skipped 0 y BUILD SUCCESS. Check JaCoCo 80% LINE / 70% BRANCH aprobado sin cambiar pom.xml. [Salida real](reto6-validacion.txt).
