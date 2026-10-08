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

El commit RED incluye solamente estas pruebas y esta evidencia, antes de crear producción reto12. Sus hashes y las siguientes fases se completan tras ejecutar los comandos reales.
