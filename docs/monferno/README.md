# Evolución Monferno — SkyCampus v2

## Contexto

SkyCampus v2 contempla 20 drones, selección automática según carga y política intercambiable, peso del paquete, prioridad y notificaciones de cambio de estado. Agrega al Técnico de mantenimiento para los fallos. Fuente principal: `DOSW_Equipo_Chimchar_fixed.html`, contexto y apartados 01–04 de Monferno.

| Tipo | Capacidad máxima | Característica oficial |
| --- | --- | --- |
| MINI | 500 g | Ágil |
| CARGO | 2000 g | Lento |
| EXPRESS | 800 g | Rápido, batería limitada |

Prioridades: URGENTE, NORMAL y BAJO. Estados de drone: DISPONIBLE, EN_VUELO, ATERRIZANDO y FALLO. Estados de misión: PENDIENTE, EN_VUELO, ENTREGADA y FALLIDA.

`FlotaInicial.crear()` proporciona una lista inmutable de 20 drones con IDs únicos: siete MINI, siete CARGO y seis EXPRESS. Son datos representativos reproducibles, no telemetría real. Las pruebas usan subconjuntos pequeños cuando basta para comprobar una regla.

## Modelo propio y compatibilidad

Los packages `monferno.*` extienden el mismo proyecto Maven. No se reemplazaron `model.Drone`, `model.Mission` ni los retos Chimchar; sus 34 pruebas permanecen intactas y pasan.

- `Drone`: ID, tipo, batería, disponible, estado y `misionesCompletadas` (mínimo dato necesario para menor uso acumulado); constructor de cinco argumentos aplica uso cero.
- `Mision`: ID, drone opcional, destino, peso, prioridad, estado, `creadaEn` y entrega opcional `entregadaEn`.
- `Optional<Drone>` representa una misión todavía sin asignación, evitando null.
- `creadaEn` permite evaluar pendientes de más de diez minutos. `entregadaEn` distingue creación y entrega para contar correctamente «completadas hoy», incluso si la misión nació ayer; no agrega telemetría del drone.
- Una ENTREGADA exige drone e instante de entrega no anterior a su creación. `conDrone` crea una nueva misión sin alterar los otros datos.

La capacidad deriva de `TipoDrone` y no se duplica como un dato arbitrario de cada drone. Disponibilidad y estado se comprueban conjuntamente antes de seleccionar; GestorFlota actualiza ambos al cambiar el estado.

## Retos de este bloque

- [Reto 1 - Streams](reto1-streams.md)
- [Reto 2 - GitFlow y conflicto real](reto2-gitflow.md)
- [Reto 3 - Strategy y Observer](reto3-patrones.md)
- [Reto 4 - SOLID](reto4-solid.md)

## Validación

```powershell
mvn clean test
rg -n "\b(for|while|do)\b" src/main/java/monferno/reto1
git diff --check
git branch --show-current
```

El conjunto final de este bloque ejecuta 85 casos JUnit: 34 Chimchar y 51 Monferno, incluidos los casos parametrizados de límites y estrategias. Todas las pruebas nuevas muestran Arrange, Act y Assert. Java release 17; sin Mockito ni dependencias externas nuevas.

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

Solo se trabajan Retos 1–4. No se creó tag v2.0.0, no se inició Reto 5 y SonarQube Chimchar sigue pendiente de acceso autorizado, sin intervenir en esa instancia durante este bloque.
