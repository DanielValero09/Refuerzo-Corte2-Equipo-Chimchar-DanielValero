# Evolución Monferno — SkyCampus v2

## Contexto

SkyCampus v2 contempla 20 drones, selección automática según carga y política intercambiable, peso del paquete, prioridad y notificaciones de cambio de estado. Agrega al Técnico de mantenimiento para los fallos. Fuente principal: `DOSW_Equipo_Chimchar_fixed.html`, contexto y apartados 01–12 de Monferno.

| Tipo | Capacidad máxima | Característica oficial |
| --- | --- | --- |
| MINI | 500 g | Ágil |
| CARGO | 2000 g | Lento |
| EXPRESS | 800 g | Rápido, batería limitada |

Prioridades: URGENTE, NORMAL y BAJO. Estados de drone: DISPONIBLE, EN_VUELO, ATERRIZANDO, FALLO, EN_CARGA y MANTENIMIENTO. Estados de misión: PENDIENTE, EN_VUELO, ENTREGADA y FALLIDA.

`FlotaInicial.crear()` proporciona una lista inmutable de 20 drones con IDs únicos: siete MINI, siete CARGO y seis EXPRESS. Son datos representativos reproducibles, no telemetría real. Las pruebas usan subconjuntos pequeños cuando basta para comprobar una regla.

## Modelo propio y compatibilidad

Los packages `monferno.*` extienden el mismo proyecto Maven. No se reemplazaron `model.Drone`, `model.Mission` ni los retos Chimchar; sus 34 pruebas permanecen intactas y pasan.

- `Drone`: ID, tipo, batería, disponible, estado y `misionesCompletadas` (mínimo dato necesario para menor uso acumulado); constructor de cinco argumentos aplica uso cero.
- `Mision`: ID, drone opcional, destino, peso, prioridad, estado, `creadaEn` y entrega opcional `entregadaEn`.
- `Optional<Drone>` representa una misión todavía sin asignación, evitando null.
- `creadaEn` permite evaluar pendientes de más de diez minutos. `entregadaEn` distingue creación y entrega para contar correctamente «completadas hoy», incluso si la misión nació ayer; no agrega telemetría del drone.
- Una ENTREGADA exige drone e instante de entrega no anterior a su creación. `conDrone` crea una nueva misión sin alterar los otros datos.

La capacidad deriva de `TipoDrone` y no se duplica como un dato arbitrario de cada drone. Disponibilidad y estado se comprueban conjuntamente antes de seleccionar; GestorFlota actualiza ambos al cambiar el estado.

## Índice Monferno

- [Reto 1 - Streams](reto1-streams.md)
- [Reto 2 - GitFlow y conflicto real](reto2-gitflow.md)
- [Reto 3 - Strategy y Observer](reto3-patrones.md)
- [Reto 4 - SOLID](reto4-solid.md)
- [Reto 5 - Contexto C4 y comparación](reto5-c4/README.md)
- [Reto 6 - RF, RNF y MoSCoW](reto6-requerimientos.md)
- [Reto 7 - SC-07, plantilla DOSW](reto7-dosw.md)
- [Reto 8 - Identidad y UX](reto8-ux/README.md)
- [Reto 9 - Planificación real en Jira](reto9-jira/README.md)
- [Reto 10 - Casos de uso y herencia](reto10-casos-uso/README.md)
- [Reto 11 - Mocks con IA y Nielsen](reto11-mocks/README.md)
- [Reto 12 - TDD y Mockito](reto12-tdd.md)

## Validación histórica del bloque 1/4

```powershell
mvn clean test
rg -n "\b(for|while|do)\b" src/main/java/monferno/reto1
git diff --check
git branch --show-current
```

El conjunto final del bloque 1/4 ejecutó 85 casos JUnit: 34 Chimchar y 51 Monferno, incluidos los casos parametrizados de límites y estrategias. Todas las pruebas nuevas muestran Arrange, Act y Assert. Java release 17; sin Mockito ni dependencias externas nuevas.

Validación final real de `mvn clean test`, terminada el 2026-10-07 a las 23:10:58 (UTC−05:00):

```text
[INFO] Compiling 50 source files with javac [debug release 17] to target\classes
[INFO] Compiling 19 source files with javac [debug release 17] to target\test-classes
[INFO] Tests run: 85, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

| Clase de pruebas Monferno | Casos ejecutados |
| --- | ---: |
| ModeloMonfernoTest | 5 |
| EstadisticasMisionesTest | 12 |
| AsignadorMisionTest | 3 |
| EstrategiasAsignacionTest | 22 |
| GestorFlotaObserverTest | 6 |
| GestorMisionesStrategyTest | 3 |
| **Total Monferno** | **51** |

Los XML reales de Surefire confirman los conteos y cero fallos/errores/omitidas. La auditoría adicional comprobó 31 enlaces relativos válidos, packages acordes a sus carpetas, cero ciclos imperativos en Reto 1, tres implementaciones de Strategy y cero archivos generados versionados. Los seis heads protegidos conservan sus hashes previos. `git diff --check` no encontró errores de formato; Git únicamente avisó de su conversión habitual LF/CRLF en Windows.

## Bloque 2/4 — Retos 5–8

Se documentó el contexto v2 con cuatro actores, tres sistemas externos y 14 flujos etiquetados; cuatro RF y cuatro RNF con ocho prioridades MoSCoW; SC-07 con siete pasos, tres alternos y siete reglas; y componentes UX basados en la identidad Chimchar. La prioridad de rapidez URGENTE, los límites de rendimiento y las integraciones externas son especificaciones por implementar/verificar, no resultados operativos del prototipo.

El único cambio de producción fue añadir EN_CARGA y MANTENIMIENTO a EstadoDrone Monferno. `EstadosOperativosTest` agrega cinco casos ejecutados con AAA: conserva los valores previos, bloquea ambos estados en las tres estrategias y comprueba notificación, conservación de datos y retorno a DISPONIBLE. No se modificó producción ni pruebas Chimchar ni las 85 pruebas existentes.

Validación real de `mvn clean test`, terminada el 2026-10-08 a las 00:06:12 (UTC−05:00):

```text
[INFO] Compiling 50 source files with javac [debug release 17] to target\classes
[INFO] Compiling 20 source files with javac [debug release 17] to target\test-classes
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0 -- in monferno.model.EstadosOperativosTest
[INFO] Tests run: 90, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Total al cierre del bloque 2/4: **90 pruebas = 34 Chimchar + 56 Monferno**. Los XML Surefire permiten verificar los conteos. Se validaron mediante ElementTree el Draw.io y cinco SVG; los cinco SVG se renderizaron con Chrome headless y se revisaron visualmente, con texto, flechas y controles legibles, sin contenido cortado. Las capturas de revisión son temporales y no se añaden como nuevos entregables al repositorio.

La auditoría comprobó 57 enlaces relativos válidos y cero rotos, coincidencia de los 14 flujos Draw.io/SVG, cuatro RF, cuatro RNF, ocho prioridades MoSCoW, siete pasos de SC-07, tres alternos y siete reglas. Las nueve ramas existentes distintas de evolution/monferno conservaron sus heads; todos los archivos de las 85 pruebas previas permanecieron sin cambios. `git diff --check` no encontró errores de formato.

Durante el bloque 2/4 no se inició Reto 9 ni se creó tag v2.0.0. No se agregaron entonces Mockito, integración meteorológica Java, cambios JaCoCo ni análisis SonarQube.

## Bloque 3/4 — Retos 9–12

La API autenticada de Jira permitió verificar SCRUM-13, las cinco historias SCRUM-14–18 y el Sprint 1 — SkyCampus v2 (ID 3, board 1, estado future). Se documentaron sus diez criterios Gherkin existentes, DoD y capacidad de 19/20 points, con un point restante. No se crearon ni modificaron issues. La captura visual real queda pendiente porque el navegador autenticado no está disponible; los criterios de calidad del DoD final todavía deben verificarse en su bloque correspondiente.

Los casos de uso muestran cuatro actores humanos, generalización Técnico → Operador, quince casos, tres include y dos extend con condiciones. Los seis mocks estáticos reutilizan la identidad del Reto 8: panel agrupado para veinte drones, formulario, confirmación y errores por falta de candidatos, clima adverso y peso mayor de 2000 g. Se conservan el prompt completo y nueve heurísticas de Nielsen vinculadas a elementos concretos.

TDD conserva cuatro commits consecutivos: configuración de Mockito 5.15.2, cinco pruebas antes de producción (Red real por clases ausentes), implementación Green y Refactor con inyección de políticas y transición de vuelo separada. NORMAL/BAJO aplican mayor batería; URGENTE prefiere EXPRESS compatible, con fallback MINI y CARGO. La política de urgencia se añadió en el Reto 12; el Reto 3 conserva sus tres estrategias base.

Total actual: **104 pruebas = 34 Chimchar + 70 Monferno**. Se preservan las 90 pruebas anteriores, las nueve de ValidadorMisionTest y los cinco escenarios iniciales de AsignadorMisionTest; otras nueve pruebas verifican límites y delegación. Todos los nuevos casos usan AAA y verifican resultados e interacciones Mockito relevantes.

ApiMeteorologica es únicamente un puerto local simulado mediante Mockito. No se implementaron HTTP, tiempo de espera de red, autorización real de ruta ni registro de reparación. La asignación devuelve un nuevo Drone EN_VUELO/no disponible y notifica una vez; no muta la flota recibida. Los tiempos de ruta/ETA de los mocks son ilustrativos.

No se iniciaron los Retos 13 o 14 Monferno, no se cambió la configuración JaCoCo heredada ni se ejecutó SonarQube. No se creó tag v2.0.0. SonarQube Chimchar y su captura real de Jira siguen pendientes, sin intervención en este bloque.

Validación final real de `mvn clean test`, terminada el 2026-10-08 a las 00:41:57 (UTC−05:00):

```text
[INFO] Compiling 53 source files with javac [debug release 17] to target\classes
[INFO] Compiling 22 source files with javac [debug release 17] to target\test-classes
[INFO] Tests run: 104, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

La auditoría confirmó 70 archivos Java previos intactos, nueve heads protegidos sin cambios y la secuencia real setup → Red → Green → Refactor. Se validaron XML de un Draw.io y siete SVG; los siete SVG se renderizaron con Chrome headless y se revisaron visualmente, con contenido legible y sin recortes. Se comprobaron 85 enlaces relativos sin enlaces rotos, correspondencia package/ruta y ausencia de target/out/.idea versionados. `git diff --check` pasó; solo hubo avisos habituales LF/CRLF en Windows. Las capturas de revisión permanecen temporales y no sustituyen la captura real pendiente de Jira.
