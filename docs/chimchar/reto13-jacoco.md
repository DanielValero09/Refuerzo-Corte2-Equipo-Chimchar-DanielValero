# Reto 13 - JaCoCo

## Configuración

- Plugin: `org.jacoco:jacoco-maven-plugin`.
- Versión: `0.8.11`, según el HTML oficial `DOSW_Equipo_Chimchar_fixed.html`.
- `prepare-agent` instrumenta las pruebas y `report` genera HTML, CSV y XML durante la fase `test`.
- Se mantienen Java release 17, JUnit 5.11.4 y Surefire 3.5.2.
- No se agregaron exclusiones de clases, paquetes ni líneas para elevar la cobertura.

```powershell
mvn clean test
mvn clean test jacoco:report
```

## Pruebas

`ValidadorMisionTest` conserva exactamente sus nueve pruebas originales. El proyecto ejecuta 34 pruebas: nueve de ese archivo y 25 adicionales en otros diez archivos de pruebas, con un helper para capturar salida de consola y restaurarla después de cada ejecución.

## Primera medición

Ejecución inicial finalizada: 2026-10-07T16:14:35-05:00; métricas extraídas el 2026-10-07T16:15:57-05:00.

| Ámbito | Líneas cubiertas | Líneas no cubiertas | Total | Cobertura LINE |
| --- | --- | --- | --- | --- |
| ValidadorMision | 7 | 0 | 7 | 100% |
| Proyecto completo | 10 | 173 | 183 | 5.46% |

Se ejecutaron nueve pruebas, con cero failures, errors y skipped. El requisito del validador ya se cumplía; el global necesitaba pruebas del resto de Chimchar.

## Ajustes

| Archivo de pruebas | Comportamiento comprobado | Pruebas |
| --- | --- | --- |
| model/MissionTest.java | Constructor compatible y conservación de datos/defaults. | 1 |
| reto1/Reto1Test.java | Resultados completos de las cuatro consultas Streams de la demostración. | 1 |
| reto3/MissionBuilderTest.java | Opcionales almacenados, defaults, obligatorios ausentes e inmutabilidad al reutilizar el builder. | 4 |
| reto3/ValidadoresTest.java | Cadena, umbral de batería, destinos, cargas y detención antes del siguiente validador. | 5 |
| reto3/AsignacionMayorBateriaTest.java | Mayor batería disponible, límite 30%, ausencia de aptos y estrategia intercambiable. | 3 |
| reto3/Reto3Test.java | Integración Builder/Chain/Strategy y selección de D-03 al 91%. | 1 |
| reto4/AsignadorMisionTest.java | Copia inmutable, conservación de todos los datos y entradas nulas. | 2 |
| reto4/RepositorioMisionMemoriaTest.java | Guardar, recuperar, actualizar, orden, copia inmutable y entradas inválidas. | 3 |
| reto4/ServiciosReto4Test.java | Reportes con/sin misiones, dos rutas y contenido de la alerta. | 4 |
| reto4/Reto4Test.java | Demostración integrada de asignación, repositorio, alerta, reporte y rutas. | 1 |

Las pruebas de demostraciones comprueban resultados observables, no solo que se ejecute el main. No se modificó producción para elevar cobertura y no se añadieron más pruebas después de superar el 80%.

## Resultado final

Medición después de ampliar las pruebas: 2026-10-07T16:19:08-05:00, con `mvn clean test jacoco:report` finalizado a las 16:18:57-05:00.

| Ámbito | Líneas cubiertas | Líneas no cubiertas | Total | Cobertura LINE |
| --- | --- | --- | --- | --- |
| ValidadorMision | 7 | 0 | 7 | 100% |
| Proyecto completo | 179 | 4 | 183 | 97.81% |

- Total de pruebas: 34.
- Failures: 0.
- Errors: 0.
- Skipped: 0.
- Resultado: BUILD SUCCESS.

El porcentaje se calcula como `LINE covered / (LINE covered + LINE missed) × 100`, usando los contadores LINE de `jacoco.xml`; no se confunde con cobertura de instrucciones o ramas. El contador global del XML se utiliza directamente, sin sumar líneas compartidas entre clases.

## Reporte

Reporte reproducible local: `target/site/jacoco/index.html`; también se generan `jacoco.csv` y `jacoco.xml`. `target/` permanece ignorado y no se versiona.

La [captura real del reporte HTML](reto13-jacoco/reporte-jacoco.png) muestra los contadores de líneas en la tabla. Las columnas «Cov.» del HTML corresponden a instrucciones y ramas; la métrica LINE se obtiene de «Missed / Lines» y del XML.
