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

Este estado se conserva en un primer commit que contiene únicamente las pruebas y este documento. No se publica hasta completar la implementación.

## Green

Pendiente de implementación y ejecución real.

## Refactor

Pendiente de revisión y ejecución real.
