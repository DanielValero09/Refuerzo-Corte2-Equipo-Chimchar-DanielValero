# Refuerzo-Corte2-Equipo-Chimchar-DanielValero

## Reto 1 - Streams & Lambdas

En este reto se trabajó con una lista de drones del sistema SkyCampus utilizando **Streams de Java**, sin emplear ciclos `for`.

Se realizaron las siguientes consultas:

1. Obtener los IDs de los drones disponibles con batería mayor o igual al 50%, ordenados de mayor a menor nivel de batería.
2. Verificar si existe algún drone disponible ubicado en el Bloque C.
3. Contar cuántos drones tienen batería crítica, definida como menor al 20%.
4. Generar una lista con el ID y el porcentaje de batería de todos los drones.

Para resolver las consultas se utilizaron operaciones de Streams como:

- `filter()`
- `sorted()`
- `map()`
- `anyMatch()`
- `count()`
- `collect()`

### Resultado obtenido

```text
Consulta 1: [D-03, D-01, D-05]
Consulta 2: true
Consulta 3: 1
Consulta 4: [D-01: 85%, D-02: 42%, D-03: 91%, D-04: 18%, D-05: 67%]
```


## Reto 2 - GitHub y GitFlow

Para este reto se utilizó GitFlow para organizar el desarrollo del proyecto SkyCampus.

Se trabajó con las siguientes ramas:

- `main`: rama principal del proyecto.
- `develop`: rama de integración del desarrollo.
- `feature/reto1-chimchar`: rama utilizada para implementar las consultas con Streams del Reto 1.
- `feature/Valero-modelo-flota`: rama utilizada para documentar el flujo GitFlow del Reto 2.

El Reto 1 fue integrado desde `feature/reto1-chimchar` a `develop` mediante el Pull Request #2 (commit `a2c42e8`). La documentación del Reto 2 fue integrada desde `feature/Valero-modelo-flota` a `develop` mediante el Pull Request #4 (commit `85c4e63`).

### Evidencias

- Repositorio: https://github.com/DanielValero09/Refuerzo-Corte2-Equipo-Chimchar-DanielValero.git
- Rama base del Pull Request: `develop`
- Rama de trabajo: `feature/Valero-modelo-flota`

### Historial de Git

Se verificó la estructura de ramas y commits mediante:

```bash
git log --oneline --graph --all --decorate
```

## Reto 3 - Patrones de Diseño

Se implementaron Builder para conservar los datos de la misión, Chain of Responsibility para validar batería, destino y carga, y Strategy para asignar el drone disponible con mayor batería (mínimo 30%).

Detalles y alcance del MVP: [Reto 3 - Patrones de Diseño](docs/chimchar/reto3-patrones.md).

## Reto 4 - Principios SOLID

Se separaron asignación, almacenamiento, alertas, reportes y rutas (SRP), se hicieron extensibles las rutas mediante `EstrategiaRuta` (OCP) y se usan las abstracciones `RepositorioMision` y `AlertaOperador` (DIP). Los servicios pequeños mejoran ISP; el fragmento original no presenta una violación directa de LSP porque no muestra una jerarquía de herencia.

Detalles del rediseño: [Reto 4 - Principios SOLID](docs/chimchar/reto4-solid.md).

## Reto 5 - Diagrama de Contexto C4

[Alcance, actores y flujos del contexto](docs/chimchar/reto5-c4/README.md) · [Diagrama SVG](docs/chimchar/reto5-c4/contexto-chimchar.svg).

## Reto 6 - RF, RNF y MoSCoW

[Tres RF, tres RNF medibles y sus prioridades](docs/chimchar/reto6-requerimientos.md).

## Reto 7 - Plantilla DOSW

[SC-01: Registrar misión de reparto de documento](docs/chimchar/reto7-dosw.md).

## Reto 8 - Manual de Identidad y UX/UI

[Manual de identidad y decisiones UX](docs/chimchar/reto8-ux/manual-identidad.md) · [Panel de flota SVG](docs/chimchar/reto8-ux/panel-flota.svg).

## Reto 9 - Agilismo y Jira

[Épica, feature, historias, subtareas y criterios](docs/chimchar/reto9-jira.md).

Evidencia pendiente: captura real de Jira.

## Reto 10 - Diagramas de Casos de Uso

[Actores, casos de uso e interpretación UML](docs/chimchar/reto10-casos-uso/README.md) · [Diagrama SVG](docs/chimchar/reto10-casos-uso/casos-uso-chimchar.svg).

## Reto 11 - Mocks con IA

Tres estados con la identidad del Reto 8: [Normal](docs/chimchar/reto11-mocks/panel-normal.svg), [FALLO](docs/chimchar/reto11-mocks/panel-fallo.svg) y [Sin drones disponibles](docs/chimchar/reto11-mocks/panel-vacio.svg).

[Prompt utilizado](docs/chimchar/reto11-mocks/prompt-utilizado.md) · [Heurísticas de Nielsen](docs/chimchar/reto11-mocks/heuristicas-nielsen.md) · [Alcance de los mocks](docs/chimchar/reto11-mocks/README.md).

## Reto 12 - TDD

Red → Green → Refactor con JUnit 5, exactamente 9 pruebas y patrón AAA para `ValidadorMision`. Las pruebas y la evidencia RED se guardaron en un commit anterior a la implementación.

[Evidencia real y casos verificados](docs/chimchar/reto12-tdd.md).

## Reto 13 - JaCoCo

[Configuración, pruebas y métricas reales](docs/chimchar/reto13-jacoco.md) · [Captura real del reporte](docs/chimchar/reto13-jacoco/reporte-jacoco.png).

Cobertura LINE: `ValidadorMision` 100%; global 97,81%. El proyecto ejecuta 34 pruebas, incluidas las nueve originales de `ValidadorMisionTest`, sin exclusiones artificiales.

## Reto 14 - SonarQube

[Intento real, error de autenticación y pasos pendientes](docs/chimchar/reto14-sonarqube/README.md).

Análisis SonarQube pendiente por limitación del entorno. El contenedor existente está activo, pero el scanner recibió HTTP 401 por falta de autenticación válida; no se publican métricas ni capturas inventadas.

## Compilación y ejecución

Se requiere un JDK 17 o superior y Maven. La compilación usa Java 17 y admite records. Los packages corresponden a las carpetas relativas a `src/main/java`: `model`, `reto1`, `reto3`, `reto4`, `reto12`, `monferno` e `infernape`, con sus subpackages. Se conservan las 104 pruebas de las evoluciones previas: 34 Chimchar y 70 Monferno; Enterprise agrega sus pruebas en packages propios. El conteo actual y la validación se registran en el índice Infernape.

```bash
mvn clean test
java -cp target/classes reto1.reto1
java -cp target/classes reto3.Reto3
java -cp target/classes reto4.Reto4
```

Para regenerar el reporte de cobertura: `mvn clean test jacoco:report`.

## Estado de entrega Chimchar

[Auditoría final de los Retos 1–14](docs/chimchar/entrega-final.md).

Evidencia Jira pendiente: captura real. SonarQube pendiente de autenticación válida para completar análisis, correcciones y evidencias.

# Evolución Monferno — SkyCampus v2

[Contexto v2, modelo y alcance del bloque](docs/monferno/README.md).

Monferno extiende el proyecto con packages propios, 20 drones, tres tipos, peso, prioridad, selección automática y notificaciones de estado. Las métricas y evidencias de las secciones Chimchar corresponden a su cierre en `2c7fe37`; su código y sus 34 pruebas permanecen intactos.

## Monferno Reto 1 - Streams

[Cuatro consultas, fechas y desempates deterministas](docs/monferno/reto1-streams.md).

## Monferno Reto 2 - GitFlow

[Ramas temporales, conflicto real, resolución y grafo verificable](docs/monferno/reto2-gitflow.md).

## Monferno Reto 3 - Strategy y Observer

[Tres estrategias, reglas de carga y observadores extensibles](docs/monferno/reto3-patrones.md).

## Monferno Reto 4 - SOLID

[Comparación del rediseño y pruebas de intercambiabilidad](docs/monferno/reto4-solid.md).

## Monferno Reto 5 - Contexto C4

[Contexto v2, flujos y comparación con Chimchar](docs/monferno/reto5-c4/README.md) · [Diagrama SVG](docs/monferno/reto5-c4/contexto-monferno.svg).

## Monferno Reto 6 - RF/RNF

[Cuatro RF, cuatro RNF medibles, MoSCoW y prioridad RF-07/RF-08](docs/monferno/reto6-requerimientos.md).

## Monferno Reto 7 - Plantilla DOSW

[SC-07 — Asignar automáticamente drone a misión](docs/monferno/reto7-dosw.md).

## Monferno Reto 8 - Identidad y UX

[Componentes v2 y leyes UX](docs/monferno/reto8-ux/README.md) · [Flujo de tres pantallas](docs/monferno/reto8-ux/flujo-asignacion.svg).

## Monferno Reto 9 - Agilismo y Jira

[Planificación real, diez criterios Gherkin y DoD](docs/monferno/reto9-jira/README.md). SCRUM-13 y SCRUM-14–18 fueron consultados en Jira: Sprint 1, ID 3, 19/20 points. Captura visual real pendiente de incorporación manual.

## Monferno Reto 10 - Casos de Uso

[Actores, herencia e interpretación UML](docs/monferno/reto10-casos-uso/README.md) · [Diagrama SVG](docs/monferno/reto10-casos-uso/casos-uso-monferno.svg).

## Monferno Reto 11 - Mocks con IA

[Flujo y tres errores](docs/monferno/reto11-mocks/README.md): [Panel](docs/monferno/reto11-mocks/01-panel-flota.svg), [Formulario](docs/monferno/reto11-mocks/02-formulario-mision.svg) y [Confirmación](docs/monferno/reto11-mocks/03-confirmacion.svg).

[Prompt utilizado](docs/monferno/reto11-mocks/prompt-utilizado.md) · [Nueve heurísticas de Nielsen](docs/monferno/reto11-mocks/heuristicas-nielsen.md).

## Monferno Reto 12 - TDD y Mockito

[Red → Green → Refactor y commits reales](docs/monferno/reto12-tdd.md). Cinco escenarios iniciales con JUnit 5, AAA y Mockito; nueve pruebas adicionales de límites e intercambiabilidad. ApiMeteorologica es una abstracción local simulada: no realiza llamadas de red. NORMAL/BAJO usan mayor batería; URGENTE prefiere EXPRESS compatible.

## Monferno Reto 13 - JaCoCo

[Métricas reales, configuración y captura del reporte](docs/monferno/reto13-jacoco/README.md). Global: LINE **98,93%**, BRANCH **93,40%**; Monferno: LINE **100%**, BRANCH **91,89%**. `mvn clean verify` ejecuta 104 pruebas y aprueba el Quality Gate local: LINE >=80%, BRANCH >=70%, sin exclusiones.

Estado: **PARCIAL — JACOCO COMPLETO / SONAR PENDIENTE**. JaCoCo completo; el análisis Sonar exigido también por este reto no se ejecutó por decisión del usuario.

## Monferno Reto 14 - SonarQube

Estado: **PENDIENTE TÉCNICO — SONARQUBE**. [Objetivos oficiales y evidencia pendiente](docs/monferno/reto14-sonarqube/README.md). No se intentó SonarQube en este bloque; no se presentan métricas ni capturas Sonar.

## Estado de entrega Monferno

[Auditoría de los Retos 1–14](docs/monferno/entrega-final.md). Retos 1–12 completados, con captura Jira pendiente; Reto 13 parcial y Reto 14 pendiente. No se declara cierre 14/14 ni se crea tag v2.0.0.

# Evolución Infernape — SkyCampus Enterprise

[Contexto, tipos y alcance Enterprise](docs/infernape/README.md). La implementación parte del estado final actual de Monferno; los pendientes Sonar/Jira anteriores se conservan. El enunciado declara cinco tipos, pero no nombra los dos adicionales; el diseño utiliza perfiles extensibles.

## Infernape Reto 1 - Analytics con Streams

[Cuatro métricas por sede con una sola pasada de misiones](docs/infernape/reto1-analytics.md).

## Infernape Reto 2 - Release, Hotfix y Tags

La simulación usa ramas aisladas para representar main/develop; sus tags académicos no liberan main real. La evidencia se incorpora al terminar la ejecución.

## Infernape Reto 3 - Patrones Enterprise

[Composite, Strategy, Observer y Factory Method: código, diagramas y justificación](docs/infernape/reto3-patrones/README.md).

## Infernape Reto 4 - SOLID y Arquitectura por Capas

[Dominio, aplicación, infraestructura y pruebas Mockito](docs/infernape/reto4-solid/README.md) · [Diagrama](docs/infernape/reto4-solid/arquitectura-3-capas.svg).
