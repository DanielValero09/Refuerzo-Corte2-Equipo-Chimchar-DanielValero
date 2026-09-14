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