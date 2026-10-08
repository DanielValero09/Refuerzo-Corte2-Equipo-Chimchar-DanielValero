# Reto 12 - TDD e Integración REST

## Red

Las pruebas se escribieron antes de crear IniciadorMision, EnterpriseApi y la persistencia. La configuración de Spring Boot 3.4.13 ya había compilado y pasado las 186 pruebas anteriores; este fallo corresponde a clases de producción aún ausentes, no a dependencias rotas.

Comando ejecutado el 8 de octubre de 2026:

```text
mvn -B -ntp -Dtest=IniciadorMisionTest,MisionesEndpointTest test
```

Fragmento real:

```text
[ERROR] COMPILATION ERROR :
[ERROR] symbol: class IniciadorMision
[ERROR] symbol: class EnterpriseApi
[ERROR] symbol: class MisionesJpaRepository
[INFO] BUILD FAILURE
[INFO] Finished at: 2026-10-08T16:24:42-05:00
```

Código de salida: 1. El commit RED contiene nueve pruebas unitarias del orquestador y doce pruebas del endpoint, sin su implementación. Los hashes y las fases Green/Refactor se completarán con sus resultados reales.
