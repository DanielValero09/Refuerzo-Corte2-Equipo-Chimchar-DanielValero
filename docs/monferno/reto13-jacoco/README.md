# Monferno Reto 13 - JaCoCo

Estado: **PARCIAL — JACOCO COMPLETO / SONAR PENDIENTE**.

Fuente principal: `DOSW_Equipo_Chimchar_fixed.html`, apartado 13 Monferno; el objetivo >=85% también figura en el apartado 14. Esta entrega demuestra cobertura y comprobación local; no sustituye el análisis SonarQube requerido por el material.

## Configuración

Plugin `org.jacoco:jacoco-maven-plugin:0.8.11`, conservado en [pom.xml](../../../pom.xml). `prepare-agent` instrumenta las pruebas; `report` permanece en fase test y genera HTML, XML y CSV. La nueva ejecución `check`, en verify, aplica reglas globales de elemento BUNDLE. Se mantienen Java release 17, JUnit 5.11.4, Mockito 5.15.2 y Surefire 3.5.2.

Commit de configuración: `bd1099bf74feebddca48b7204734684d2340e831` — test: configura quality gate JaCoCo de Monferno. Cambia únicamente pom.xml; no incluye producción ni pruebas nuevas.

```powershell
mvn clean test jacoco:report
mvn clean verify
```

## Umbrales

| Contador | Valor | Mínimo obligatorio |
| --- | --- | ---: |
| LINE | COVEREDRATIO | 0.80 (80%) |
| BRANCH | COVEREDRATIO | 0.70 (70%) |

Objetivo deseado Monferno: LINE >=85%; alcanzado en el bundle global y en Monferno. No se agregaron exclusiones, @Generated, código muerto ni cambios de producción para variar el denominador. Las reglas abarcan todo el proyecto, incluidos Chimchar y Monferno.

## Métricas iniciales

Comando real `mvn clean test jacoco:report`, BUILD SUCCESS, finalizado 2026-10-08T11:56:29-05:00. Base Git `c5ec4ec47c65e1c6a801548758ef88bc488fa7df`, antes de agregar check. **104 pruebas, 0 failures, 0 errors, 0 skipped; 34 Chimchar + 70 Monferno**.

| Alcance | Contador | Covered | Missed | Total | Cobertura |
| --- | --- | ---: | ---: | ---: | ---: |
| GLOBAL | LINE | 369 | 4 | 373 | 98,93% |
| GLOBAL | BRANCH | 99 | 7 | 106 | 93,40% |
| MONFERNO | LINE | 190 | 0 | 190 | 100,00% |
| MONFERNO | BRANCH | 68 | 6 | 74 | 91,89% |

Contadores extraídos por programa de `target/site/jacoco/jacoco.xml`; [snapshot base](metricas-base.json). Para Monferno se suman covered/missed de los ocho packages cuyo nombre comienza por `monferno/`, sin promediar porcentajes ni mezclar instruction coverage con LINE. Fórmula aplicada por programa: covered / (covered + missed) × 100; las tablas redondean a dos decimales. N/A significa que JaCoCo no registra ramas ejecutables para ese elemento, no 100% de ramas.

## Métricas finales

Comando real `mvn clean verify`, BUILD SUCCESS, finalizado 2026-10-08T11:58:15-05:00. [Snapshot final](metricas-finales.json).

| Alcance | Contador | Covered | Missed | Total | Cobertura |
| --- | --- | ---: | ---: | ---: | ---: |
| GLOBAL | LINE | 369 | 4 | 373 | 98,93% |
| GLOBAL | BRANCH | 99 | 7 | 106 | 93,40% |
| MONFERNO | LINE | 190 | 0 | 190 | 100,00% |
| MONFERNO | BRANCH | 68 | 6 | 74 | 91,89% |

Las métricas no cambiaron: únicamente se agregó check. La cobertura base ya superaba >=85% LINE y >=70% BRANCH, por lo que no se añadieron pruebas innecesarias.

### Cobertura por package

Los valores iniciales y finales de todos los packages coinciden; ambos snapshots conservan los contadores completos.

| Package | LINE covered/missed | LINE | BRANCH covered/missed | BRANCH |
| --- | ---: | ---: | ---: | ---: |
| `model` | 13/0 | 100,00% | 0/0 | N/A (sin ramas) |
| `monferno.model` | 55/0 | 100,00% | 21/5 | 80,77% |
| `monferno.reto1` | 24/0 | 100,00% | 15/1 | 93,75% |
| `monferno.reto12` | 25/0 | 100,00% | 12/0 | 100,00% |
| `monferno.reto2` | 7/0 | 100,00% | 0/0 | N/A (sin ramas) |
| `monferno.reto3` | 28/0 | 100,00% | 6/0 | 100,00% |
| `monferno.reto3.observer` | 16/0 | 100,00% | 2/0 | 100,00% |
| `monferno.reto3.strategy` | 21/0 | 100,00% | 0/0 | N/A (sin ramas) |
| `monferno.reto3.validation` | 14/0 | 100,00% | 12/0 | 100,00% |
| `reto1` | 22/1 | 95,65% | 8/0 | 100,00% |
| `reto12` | 9/0 | 100,00% | 5/1 | 83,33% |
| `reto3` | 32/2 | 94,12% | 0/0 | N/A (sin ramas) |
| `reto3.builder` | 23/0 | 100,00% | 6/0 | 100,00% |
| `reto3.chain` | 23/0 | 100,00% | 10/0 | 100,00% |
| `reto3.strategy` | 11/0 | 100,00% | 2/0 | 100,00% |
| `reto4` | 26/1 | 96,30% | 0/0 | N/A (sin ramas) |
| `reto4.alert` | 3/0 | 100,00% | 0/0 | N/A (sin ramas) |
| `reto4.report` | 5/0 | 100,00% | 0/0 | N/A (sin ramas) |
| `reto4.repository` | 8/0 | 100,00% | 0/0 | N/A (sin ramas) |
| `reto4.route` | 4/0 | 100,00% | 0/0 | N/A (sin ramas) |

### Clases clave

| Clase | LINE covered/missed | LINE | BRANCH covered/missed | BRANCH |
| --- | ---: | ---: | ---: | ---: |
| `monferno.reto12.AsignadorMision` | 18/0 | 100,00% | 12/0 | 100,00% |
| `monferno.reto12.EstrategiaUrgenteExpress` | 7/0 | 100,00% | 0/0 | N/A (sin ramas) |
| `monferno.reto3.GestorMisiones` | 6/0 | 100,00% | 0/0 | N/A (sin ramas) |
| `monferno.reto3.GestorFlota` | 22/0 | 100,00% | 6/0 | 100,00% |

### Huecos identificados

Se inspeccionaron los contadores y las líneas de sourcefile en el XML:

| Archivo | Líneas completamente sin cubrir | Ramas pendientes |
| --- | --- | --- |
| reto3/Reto3.java | 19, 137 | Ninguna registrada |
| reto4/Reto4.java | 16 | Ninguna registrada |
| reto1/reto1.java | 8 | Ninguna |
| monferno/model/Drone.java | Ninguna | 1 en línea 11 |
| monferno/model/Mision.java | Ninguna | 2 en línea 18; 1 en línea 21; 1 en línea 24 |
| monferno/reto1/EstadisticasMisiones.java | Ninguna | 1 en línea 33 |
| reto12/ValidadorMision.java | Ninguna | 1 en línea 16 |

Menor cobertura LINE: Reto3 32/34 (94,12%), Reto4 19/20 (95%) y reto1 22/23 (95,65%). En Monferno todas las líneas están cubiertas; menor cobertura BRANCH entre clases con ramas: Mision 14/18 (77,78%), Drone 7/8 (87,50%) y EstadisticasMisiones 15/16 (93,75%). Estos huecos siguen visibles; no se ocultaron para mejorar porcentajes.

## Pruebas añadidas

Ninguna. Se conservan las 104 pruebas previas, incluidas las 34 Chimchar, las nueve de ValidadorMisionTest y las cinco originales del TDD de AsignadorMision. No se modificaron fuentes ni pruebas Java.

## Resultado mvn clean verify

```text
[INFO] Tests run: 104, Failures: 0, Errors: 0, Skipped: 0
[INFO] --- jacoco:0.8.11:report (report) @ chimchar ---
[INFO] --- jacoco:0.8.11:check (check) @ chimchar ---
[INFO] Analyzed bundle 'chimchar' with 45 classes
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
[INFO] Finished at: 2026-10-08T11:58:15-05:00
```

JaCoCo genera el informe automáticamente durante test; check se ejecuta al llegar a verify. `mvn test` por sí solo no ejecuta el umbral. Mockito emite avisos de carga dinámica del agente en el JDK 21 del entorno; no hay errores ni pruebas omitidas y la compilación mantiene release 17.

## Reporte y captura real

Reporte local: `target/site/jacoco/index.html`, con `jacoco.xml` y `jacoco.csv` en la misma carpeta. No se versiona target; se regenera con los comandos anteriores.

[PNG real del reporte](reporte-jacoco-monferno.png), capturado con Chrome headless directamente desde el archivo HTML generado, 1600 × 1100, y revisado visualmente. No se recreó ni alteró el reporte. El título conserva el nombre Maven heredado «SkyCampus Chimchar»; su bundle contiene ambos niveles y muestra los packages Monferno. La primera columna Cov. del HTML corresponde a instrucciones; el porcentaje LINE documentado procede de los contadores LINE del XML.

## Estado SonarQube

La parte JaCoCo y el Quality Gate local están completados.

El análisis SonarQube exigido también por el enunciado del Reto 13 NO se ejecutó por decisión de no continuar con la configuración local de SonarQube. No se intentó ejecutarlo en este bloque y no existen métricas ni capturas Sonar de Monferno.

Por eso el Reto 13 se clasifica: **PARCIAL — JACOCO COMPLETO / SONAR PENDIENTE**. La comprobación local JaCoCo no equivale a un Quality Gate Sonar verde. [Pendiente SonarQube](../reto14-sonarqube/README.md) · [Auditoría final](../entrega-final.md).
