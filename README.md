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

## Compilación y ejecución

Se requiere un JDK 17 o superior y Maven. La compilación usa Java 17 y admite records. Los packages corresponden a las carpetas relativas a `src/main/java`: `model`, `reto1`, `reto3` y `reto4`, con sus subpackages. JUnit 5 está configurado para pruebas posteriores.

```bash
mvn clean test
java -cp target/classes reto1.reto1
java -cp target/classes reto3.Reto3
java -cp target/classes reto4.Reto4
```
