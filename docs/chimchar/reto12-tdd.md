# Reto 12 - TDD

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 12 de Chimchar. JUnit 5 y exactamente nueve pruebas en `reto12.ValidadorMisionTest`, tres por método, con comentarios Arrange, Act y Assert. Las pruebas de excepciones preparan un `Executable` cuya ejecución controla la aserción de JUnit.

## Red

Comando: `mvn -Dtest=ValidadorMisionTest test`.

Ejecución real: 2026-10-07T15:58:26-05:00. Código de salida: 1. Se escribió primero el archivo de pruebas, sin crear `ValidadorMision` ni `DestinoInvalidoException`. Maven falló en testCompile porque ambas clases aún no existían; las nueve pruebas no llegaron a ejecutarse.

Fragmento literal de la salida:

```text
[ERROR] COMPILATION ERROR :
  symbol:   class ValidadorMision
  location: class reto12.ValidadorMisionTest
  symbol:   class DestinoInvalidoException
  location: class reto12.ValidadorMisionTest
[INFO] 3 errors
[INFO] BUILD FAILURE
[INFO] Finished at: 2026-10-07T15:58:26-05:00
```

Este estado se conserva en el commit `6afbfc189c8ff82096cdd4a28d6a750027c3e43d`, que contiene únicamente las pruebas y este documento. No se publica hasta completar la implementación.

## Green

Se crearon `ValidadorMision` y `DestinoInvalidoException` después del commit RED. La batería exige >=30%; la disponibilidad solo consulta `available`; el destino debe ser uno de los cinco del MVP. Un destino distinto, incluido null, lanza la excepción del dominio. La primera implementación construía el conjunto de destinos dentro de `validarDestino`.

Comando: `mvn -Dtest=ValidadorMisionTest test`. Código de salida: 0.

Salida real:

```text
[INFO] Running reto12.ValidadorMisionTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[INFO] Finished at: 2026-10-07T16:00:19-05:00
```

## Refactor

Se extrajo el conjunto local a `private static final Set<String> DESTINOS_VALIDOS`: se define una vez y la regla queda centralizada. Se revisaron nombres, imports y métodos pequeños, sin modificar las nueve pruebas ni su comportamiento.

Comando: `mvn test`. Código de salida: 0.

Salida real después del refactor:

```text
[INFO] Running reto12.ValidadorMisionTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[INFO] Finished at: 2026-10-07T16:00:49-05:00
```

## Casos verificados

| Método | Entrada | Resultado |
| --- | --- | --- |
| tieneBateriaSuficiente | Batería 29%, disponible | false |
| tieneBateriaSuficiente | Batería 30%, disponible | true |
| tieneBateriaSuficiente | Batería 91%, no disponible | true |
| validarDestino | Bloque A | No lanza excepción |
| validarDestino | Biblioteca | No lanza excepción |
| validarDestino | Edificio Inexistente | DestinoInvalidoException |
| droneEstaDisponible | available = true | true |
| droneEstaDisponible | available = false | false |
| droneEstaDisponible | D-04, batería 18%, available = true | true |

Los casos de 91% no disponible y D-04 disponible con 18% demuestran que batería y disponibilidad se evalúan independientemente. Los destinos se comparan por contenido mediante `Set.contains`, no con `==`.

## Validación final

Comando: `mvn clean test`. Se recompilaron 26 archivos de producción y un archivo de pruebas con release 17. Código de salida: 0.

Salida real:

```text
[INFO] Compiling 26 source files with javac [debug release 17] to target\classes
[INFO] Compiling 1 source file with javac [debug release 17] to target\test-classes
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[INFO] Finished at: 2026-10-07T16:04:35-05:00
```

Los SVG y el código final se incluyen en el segundo commit `feat: implementa ValidadorMision con TDD`; ambos commits se publican juntos exclusivamente en `evolution/chimchar`. No se agregan JaCoCo ni análisis SonarQube en este bloque.
