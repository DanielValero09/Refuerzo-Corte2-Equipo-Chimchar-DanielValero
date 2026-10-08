# Monferno Reto 12 - TDD y Mockito

## Configuración

Mockito `mockito-junit-jupiter` 5.15.2, scope test, con Java release 17 y JUnit 5.11.4 sin cambios. Commit setup: `ff952d6b28b398967586c4a892b1417124d00c87`. Validación previa: 90 pruebas, cero fallos/errores/omitidas, BUILD SUCCESS (2026-10-08T00:21:48-05:00).

Se probó primero Mockito 5.23.0; su dependencia transitiva de junit-jupiter-api 5.13.4 produjo `org/junit/platform/engine/reporting/OutputDirectoryProvider` en Surefire, antes de ejecutar pruebas. Se eligió 5.15.2 para conservar JUnit existente y la validación pasó. Este fallo de configuración no se cuenta como RED.

## Red

Primero se escribió `src/test/java/monferno/reto12/AsignadorMisionTest.java` con exactamente cinco pruebas AAA y mocks de clima/notificador. En ese momento no existían ApiMeteorologica ni AsignadorMision de reto12.

Comando real en PowerShell (el argumento -D se cita para evitar separación por los puntos del package):

```powershell
mvn "-Dtest=monferno.reto12.AsignadorMisionTest" test
```

Resultado real: exit code 1, fallo de compilación, 2026-10-08T00:23:33-05:00. Fragmento de Maven:

```text
[ERROR] COMPILATION ERROR :
[ERROR] .../src/test/java/monferno/reto12/AsignadorMisionTest.java:[26,11] cannot find symbol
  symbol:   class ApiMeteorologica
  location: class monferno.reto12.AsignadorMisionTest
[ERROR] .../src/test/java/monferno/reto12/AsignadorMisionTest.java:[37,9] cannot find symbol
  symbol:   class AsignadorMision
  location: class monferno.reto12.AsignadorMisionTest
[INFO] 11 errors
[INFO] BUILD FAILURE
```

Se abrevia únicamente el prefijo de la ruta local. No hubo cinco tests ejecutados: la ausencia de producción impidió compilar las cinco pruebas. Escenarios: NORMAL exitosa, clima adverso, sin drones aptos, 2001 g y URGENTE EXPRESS con menos batería que MINI.

El commit RED `2e8bf867ba0add80bc3e6cb862bb1d8f5c383f76` incluye solamente estas pruebas y esta evidencia, antes de crear producción reto12; su árbol no contiene `src/main/java/monferno/reto12/`.

## Green

Commit: `9610944f121460315e8b5265e4db9837f15fce2f` — feat: implementa AsignadorMision Monferno con Mockito.

Se crearon ApiMeteorologica como puerto local `boolean esApto()`, AsignadorMision y EstrategiaUrgenteExpress. Se reutilizan la cadena de validadores y MayorBateriaStrategy, sin HTTP, Spring o REST. URGENTE prioriza EXPRESS, después MINI y CARGO compatibles; dentro del mismo tipo gana mayor batería y luego ID ascendente. La asignación devuelve Optional y copia el drone a EN_VUELO/no disponible conservando sus datos antes de notificar una sola vez.

Comando: `mvn "-Dtest=monferno.reto12.AsignadorMisionTest" test`. Salida real, 2026-10-08T00:25:30-05:00:

```text
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0 -- in monferno.reto12.AsignadorMisionTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Después, `mvn clean test` ejecutó 95 pruebas, cero fallos/errores/omitidas y BUILD SUCCESS (2026-10-08T00:25:51-05:00), antes del commit Green.

## Refactor

Commit: `84d8d259bd7bac31608efb93970b1c3429838d32` — refactor: simplifica asignacion automatica Monferno.

Cambios estructurales reales sin alterar los cinco escenarios:

- Constructor adicional que inyecta políticas normal/urgente mediante EstrategiaAsignacion, manteniendo el constructor original.
- Extracción de `solicitudAsignable` para peso 1–2000 y misión PENDIENTE.
- Extracción de `iniciarVuelo` para copia inmutable y notificación; elimina la lambda con varias responsabilidades del método de asignación.

Los cinco tests RED permanecen idénticos. Se agregaron nueve casos útiles en otro archivo, AsignadorMisionLimitesTest: batería 30%, EXPRESS incompatible con 801 g, prohibición CARGO <100 g, fallback urgente MINI, empate EXPRESS por ID, BAJO con mayor batería, peso cero, misión ya EN_VUELO e inyección de políticas.

`mvn clean test` tras el refactor, 2026-10-08T00:27:49-05:00:

```text
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0 -- in monferno.reto12.AsignadorMisionLimitesTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0 -- in monferno.reto12.AsignadorMisionTest
[INFO] Tests run: 104, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Interacciones y límites del alcance

Validación final del bloque, `mvn clean test`, 2026-10-08T00:41:57-05:00: **104 pruebas, Failures 0, Errors 0, Skipped 0, BUILD SUCCESS**. Los XML Surefire confirman 34 Chimchar y 70 Monferno. La auditoría del árbol RED verificó cinco pruebas sin producción reto12; los 70 archivos Java existentes en la base e3f0234 permanecen intactos.

Todas las pruebas muestran Arrange/Act/Assert y usan MockitoExtension con mocks de ApiMeteorologica y ObservadorDrone; `when(...).thenReturn(...)`, `verify`, `times(1)`, `never` y `verifyNoInteractions` verifican la consulta, inicio único y ausencia de notificación en rechazos. Un paquete inválido se rechaza antes de consultar clima o seleccionar drone.

No se consumió una dependencia externa de red: ApiMeteorologica es un puerto simulado por Mockito. La asignación devuelve un nuevo Drone; no muta la lista ni registra una nueva Mision/persistencia. El consumidor debe conservar el resultado operativo. El timeout real de 2 s, registro persistente de reparación y autorización aérea no se implementan ni se marcan como DoD cumplido aquí.

Mockito muestra avisos de autoanexión del agente Byte Buddy al ejecutar en JDK 21; las pruebas pasan con Java release 17. No se alteró la configuración JaCoCo heredada ni se creó Quality Gate/SonarQube de Reto 13/14.

## Historial verificable

Orden: setup `ff952d6` → RED `2e8bf86` → GREEN `9610944` → REFACTOR `84d8d25`. No se fabricaron commits retroactivos ni se reescribió historia.

Artefacto de pruebas: [Mockito JUnit Jupiter 5.15.2 en Maven Central](https://central.sonatype.com/artifact/org.mockito/mockito-junit-jupiter/5.15.2).
