# Reto 13 - JaCoCo Enterprise

**Estado académico: PARCIAL — JACOCO COMPLETO / SONAR PENDIENTE.** Fuente: `DOSW_Equipo_Chimchar_fixed.html`, sección Infernape 13. No se declara Quality Gate Sonar verde.

## Configuración real

[pom.xml](../../../pom.xml) mantiene `org.jacoco:jacoco-maven-plugin:0.8.11`, prepare-agent, report en test y check en verify. La regla global BUNDLE aplica COVEREDRATIO: LINE mínimo 0.85 y BRANCH mínimo 0.75. No hay exclusiones, @Generated añadido, eliminación de producción ni cambio artificial del denominador. Java release 17, JUnit 5.11.4, Mockito 5.15.2, Surefire 3.5.2 y Spring Boot 3.4.13 permanecen.

```powershell
mvn -B -ntp clean verify
```

Commit de configuración: `daf051e9244f2c0aa1399a7955e0c2bc6343634e`. Base auditada: `428a3c60c2b6cc58e2301f12f9436ad22fe3f638`; el JSON final identifica la revisión de producción/configuración que se midió, anterior al commit documental.

## Comparación de estándares

| Evolución | LINE requerida | BRANCH requerida | Resultado histórico / actual |
|---|---:|---:|---|
| Chimchar | 80% | No exigida por el HTML | 34 pruebas; LINE global 97,81% en su cierre; [evidencia histórica](../../chimchar/reto13-jacoco.md). |
| Monferno | 80% | 70% | 104 pruebas; LINE global 98,93%, BRANCH global 93,40% en su cierre; [evidencia histórica](../../monferno/reto13-jacoco/README.md). |
| Infernape | 85% | 75% | 228 pruebas; resultados actuales abajo. |

Son mediciones de revisiones distintas, no porcentajes comparables del mismo denominador. El check actual 85/75 cubre todo el proyecto acumulado.

## Métricas iniciales

Medición ejecutada: 2026-10-08T18:21:45-05:00, BUILD SUCCESS. Snapshot extraído: 2026-10-08T18:23:45.788568-05:00. 226 pruebas: 34 Chimchar + 70 Monferno + 122 Infernape, cero failures/errors/skipped.

| Ámbito / contador | Covered | Missed | Porcentaje |
|---|---:|---:|---:|
| GLOBAL LINE | 747 | 12 | 98.4190% |
| GLOBAL BRANCH | 254 | 22 | 92.0290% |
| INFERNAPE LINE | 378 | 8 | 97.9275% |
| INFERNAPE BRANCH | 155 | 15 | 91.1765% |

[Snapshot inicial real](metricas-iniciales.json).

## Métricas finales

Validación final: 2026-10-08T18:27:27-05:00. Snapshot extraído: 2026-10-08T18:30:33.237603-05:00.

| Ámbito / contador | Covered | Missed | Porcentaje |
|---|---:|---:|---:|
| GLOBAL LINE | 751 | 12 | 98.4273% |
| GLOBAL BRANCH | 254 | 22 | 92.0290% |
| INFERNAPE LINE | 382 | 8 | 97.9487% |
| INFERNAPE BRANCH | 155 | 15 | 91.1765% |

Fórmula: `covered / (covered + missed) * 100`. GLOBAL lee los counters directos de report; INFERNAPE suma los counters directos de los paquetes infernape/. No se promedian porcentajes de paquetes. Un contador sin elementos se registra como null / N/A, no como 100%.

[Snapshot final por clase y paquete](metricas-finales.json). LINE global ≥85% y BRANCH global ≥75%; también Infernape supera ambos límites.

## Cobertura por módulos Enterprise

| Paquete | LINE covered/total | LINE | BRANCH covered/total | BRANCH |
|---|---:|---:|---:|---:|
| infernape.infrastructure | 28/33 | 84.8485% | 11/14 | 78.5714% |
| infernape.reto1 | 28/28 | 100.0000% | 6/6 | 100.0000% |
| infernape.reto3.factory | 10/10 | 100.0000% | 2/2 | 100.0000% |
| infernape.infrastructure.persistence | 15/15 | 100.0000% | 0/0 | N/A — sin ramas registradas |
| infernape.reto3.strategy | 14/14 | 100.0000% | 3/4 | 75.0000% |
| infernape.application | 82/82 | 100.0000% | 41/42 | 97.6190% |
| infernape.domain | 143/146 | 97.9452% | 84/94 | 89.3617% |
| infernape.api | 16/16 | 100.0000% | 2/2 | 100.0000% |
| infernape.reto3.observer | 26/26 | 100.0000% | 2/2 | 100.0000% |
| infernape.reto3.ruta | 20/20 | 100.0000% | 4/4 | 100.0000% |

## Clases importantes

| Clase o módulo | LINE | BRANCH |
|---|---:|---:|
| infernape.application.IniciadorMision | 100.0000% | 100.0000% |
| infernape.application.AsignadorMisionEnterprise | 100.0000% | 100.0000% |
| infernape.application.AutorizadorRutaInterSede | 100.0000% | 100.0000% |
| infernape.reto3.strategy.PlanificadorRuta | 100.0000% | 75.0000% |
| infernape.reto1.AnalyticsRed | 100.0000% | N/A — sin ramas registradas |
| infernape.api.MisionesController | 100.0000% | N/A — sin ramas registradas |
| infernape.infrastructure.persistence (paquete completo) | 100.0000% | N/A — sin ramas registradas |

## Cinco clases con menor cobertura LINE global

| Clase | LINE | BRANCH |
|---|---:|---:|
| infernape.infrastructure.ConfiguracionEnterpriseLocal | 61.5385% | N/A — sin ramas registradas |
| infernape.domain.CondicionesEspacioAereo | 80.0000% | 62.5000% |
| infernape.domain.ResumenSede | 83.3333% | 62.5000% |
| infernape.domain.ResumenRed | 88.8889% | 90.0000% |
| reto3.Reto3 | 94.1176% | N/A — sin ramas registradas |

No se añaden pruebas para ejecutar adaptadores por defecto o getters sin comprobar comportamiento: la cobertura ya supera el objetivo. Las líneas sin cubrir permanecen visibles.

## Pruebas y regresión útil

Las 226 pruebas anteriores permanecen intactas. Se agregaron únicamente dos pruebas AAA en [NotificacionPersistenciaTest](../../../src/test/java/infernape/api/NotificacionPersistenciaTest.java): fallo al guardar sin notificación, y éxito con guardar antes de notificar exactamente una vez. Usan el Controller, Iniciador y asignador reales con repositorio/observador mock para inducir el fallo y comprobar el orden; las pruebas H2 reales anteriores siguen aprobadas.

RED `ce3a6f88456cc5fcbfecdd8cad0b83061f07e368`: 2 pruebas, 2 failures, 0 errors; Mockito registró NeverWantedButInvoked y VerificationInOrderFailure. GREEN `8d54e4cc29db6b82aca8e757c507d87cbbcb4845` corrige el cableado REST y añade observador post-persistencia a IniciadorMision. Se mantiene la firma anterior del constructor y el comportamiento autónomo del asignador para compatibilidad.

La configuración REST inyecta un observador sin efectos en el asignador y el observador real en el iniciador. Este guarda mediante el puerto y solo después emite el evento. En el adaptador Spring actual, la transacción de guardar termina antes de que regrese su proxy. **No es un outbox ni entrega exactamente una vez**: caída del proceso o fallo del observador después del commit puede dejar una misión persistida sin evento. La documentación de [auditoría REST](../entrega-final.md#integración-rest-y-límites) describe ese límite y la carrera concurrente de IDs/flota; no se presentan como solucionados.

## Resultado de Maven

```text
[INFO] Tests run: 228, Failures: 0, Errors: 0, Skipped: 0
[INFO] --- jacoco:0.8.11:check (check) @ chimchar ---
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
[INFO] Finished at: 2026-10-08T18:27:27-05:00
```

228 = 34 Chimchar + 70 Monferno + 124 Infernape. [Extractos reales inicial/RED/GREEN/final](evidencia-maven.txt).

## Reporte y captura auténtica

Reporte generado: `target/site/jacoco/index.html`, con jacoco.xml y jacoco.csv. target/ no se versiona. La [captura PNG real](reporte-jacoco-infernape.png) se obtuvo directamente de ese archivo mediante Chrome headless; no es una tabla reconstruida ni una imagen con métricas alteradas. El título del reporte conserva el nombre Maven heredado SkyCampus Chimchar y contiene los paquetes de las tres evoluciones.

También se repitieron 26 pruebas reales Chrome del prototipo, cero fallos; [evidencia nueva](evidencia-navegador.json). Esto no demuestra una prueba con compañero, que sigue pendiente.

## Estado SonarQube

JaCoCo y el Quality Gate LOCAL Maven están completados. El análisis SonarQube exigido también por el enunciado NO se ejecutó por decisión del usuario. Quality Gate SONAR pendiente. [Reto 14 pendiente](../reto14-sonarqube/README.md). No se fabrican métricas Sonar.
