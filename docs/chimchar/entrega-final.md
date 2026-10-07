# Entrega final - Chimchar

Auditoría del 7 de octubre de 2026, basada en `DOSW_Equipo_Chimchar_fixed.html`, los requisitos de los bloques autorizados y las evidencias del repositorio. Rama de trabajo exclusiva: `evolution/chimchar`; sin PR, merge, rebase, reset, force push ni modificaciones de otras ramas en estos bloques.

## Checklist de los 14 retos

| Reto | Requisito principal | Evidencia | Estado |
| --- | --- | --- | --- |
| 1 | Cuatro consultas Streams, batería/ubicación/conteo/formato y sin for. | [Código](../../src/main/java/reto1/reto1.java), [prueba de los cuatro resultados](../../src/test/java/reto1/Reto1Test.java) y README principal. | COMPLETO |
| 2 | main, develop, ramas feature y PR de integración documentados factual y verificablemente. | [README principal](../../README.md): PR #2 (`a2c42e8`) y PR #4 (`85c4e63`); referencias Git verificadas en lectura. | COMPLETO |
| 3 | Builder conserva obligatorios/opcionales, Chain batería/destino/carga y Strategy disponible con batería >=30%. | [Documentación](reto3-patrones.md), pruebas MissionBuilderTest, ValidadoresTest, AsignacionMayorBateriaTest y Reto3Test; D-03 seleccionado al 91%. | COMPLETO |
| 4 | SRP, OCP, ISP y DIP aplicados; LSP explicado sin inventar violación directa. | [Rediseño SOLID](reto4-solid.md) y pruebas de asignación, repositorio, servicios y demostración. | COMPLETO |
| 5 | Contexto C4: tres actores, cero sistemas externos y seis flujos dirigidos; SkyCampus como caja negra. | [Alcance y enlaces SVG/Draw.io](reto5-c4/README.md); XML y relaciones revisados. | COMPLETO |
| 6 | Tres RF, tres RNF medibles y seis prioridades MoSCoW con justificación. | [Tabla de requisitos](reto6-requerimientos.md), conteos verificados. | COMPLETO |
| 7 | SC-01, entradas con tipos reales, cinco pasos, dos alternos y dos reglas. | [Plantilla DOSW](reto7-dosw.md), conteos y tipos revisados. | COMPLETO |
| 8 | Identidad, paleta HEX, tipografías, cinco drones y heurísticas Nielsen. | [Manual](reto8-ux/manual-identidad.md) y [panel SVG](reto8-ux/panel-flota.svg). | COMPLETO |
| 9 | Épica, feature, tres HU, tres subtareas HU-02, dos criterios y captura real de Jira. | [Contenido preparado](reto9-jira.md); falta captura real de Jira, no se fabricó evidencia. | PENDIENTE EVIDENCIA |
| 10 | Tres actores externos, límite SkyCampus, include correcto, extend correcto y condición PENDIENTE. | [Documentación y archivos](reto10-casos-uso/README.md); direcciones UML revisadas. | COMPLETO |
| 11 | Paneles normal, FALLO y vacío; identidad reutilizada, prompt y Nielsen. | [Tres SVG y documentos](reto11-mocks/README.md); validación XML, estados y revisión visual real. | COMPLETO |
| 12 | RED → GREEN → REFACTOR, JUnit 5, exactamente nueve pruebas del validador y AAA. | [Evidencia con salidas reales](reto12-tdd.md); commit RED `6afbfc1` anterior a producción `70319d5`. | COMPLETO |
| 13 | >=80% LINE de ValidadorMision y global, con métricas reales y sin exclusiones artificiales. | [Mediciones](reto13-jacoco.md) y [captura real](reto13-jacoco/reporte-jacoco.png): 100% y 97,81%; 34 pruebas. | COMPLETO |
| 14 | Análisis SonarQube real, métricas/capturas antes/después y correcciones de issues reales. | [Intento real y error HTTP 401](reto14-sonarqube/README.md); servidor UP, falta autenticación válida. | PENDIENTE TÉCNICO |

Resultado: 12 retos COMPLETOS, 1 PENDIENTE EVIDENCIA y 1 PENDIENTE TÉCNICO. No se declara la entrega completamente cerrada mientras falten Jira y el análisis SonarQube.

## Verificaciones de código y comportamiento

- Las pruebas del Reto 1 comprueban `[D-03, D-01, D-05]`, disponibilidad en Bloque C, un drone con batería <20% y la lista completa de porcentajes.
- Builder: obligatorios drone/origen/destino, prioridad default 3, notas/hora vacías, conservación de datos y estado PENDIENTE.
- Chain: batería <30 rechazada, cinco destinos, SOBRE/CARPETA aceptados, LIBRO/null rechazados y detención al fallar una validación.
- Strategy: disponible, batería >=30, mayor batería y Optional vacío sin candidatos; intercambiable mediante interfaz.
- SOLID: asignación inmutable conservando todos los datos, repositorio por abstracción, copia de listado inmutable, alerta y reporte textuales y dos estrategias de ruta.
- TDD conserva nueve casos y nueve comentarios de cada fase AAA; las 25 pruebas adicionales están en otros archivos.

Las especificaciones de RF/RNF, Jira y casos de uso describen el comportamiento requerido y no certifican una aplicación completa implementada. Los paneles son prototipos estáticos y no telemetría real. No se incorporan evoluciones posteriores.

## Evidencia Git revisada

- Existen referencias a main y develop y ramas feature de Retos 1–3.
- PR #2: `a2c42e8 Merge pull request #2 from DanielValero09/feature/reto1-chimchar`.
- PR #4: `85c4e63 Merge pull request #4 from DanielValero09/feature/Valero-modelo-flota`.
- El historial previo incluye una integración a main; la documentación no afirma que nunca ocurriera ni reescribe ese historial.
- El Reto 12 conserva dos commits consecutivos: pruebas/evidencia RED antes de implementación.

## Calidad medible

| Medición | Inicial | Final |
| --- | --- | --- |
| JaCoCo LINE ValidadorMision | 7/7 = 100% | 7/7 = 100% |
| JaCoCo LINE global | 10/183 = 5,46% | 179/183 = 97,81% |
| Pruebas del proyecto | 9 | 34 |
| Failures / Errors / Skipped | 0 / 0 / 0 | 0 / 0 / 0 |
| Métricas SonarQube | No disponibles: HTTP 401 | Pendientes de autenticación y análisis real |

La cifra global JaCoCo proviene del contador LINE del XML, no de las columnas de instrucciones/ramas del HTML. El reporte local se regenera con `mvn clean test jacoco:report` y queda bajo `target/`, ignorado por Git.

## Pendientes que requieren intervención humana

1. Jira: crear o verificar los elementos documentados en la instancia real, adjuntar una captura que muestre épica, feature, tres HU, tres subtareas y dos criterios, y actualizar el estado del Reto 9.
2. SonarQube: habilitar acceso autorizado a la instancia local existente. Después ejecutar el análisis, conservar métricas reales, corregir issues (al menos tres code smells si existen tres), reanalizar y adjuntar las capturas antes/después. Los comandos pendientes están en el documento del Reto 14; los tokens se usan solo en el entorno temporal.

## Validación reproducible

```powershell
git branch --show-current
mvn clean test
mvn clean test jacoco:report
rg -n "src\.main\.java" src/main/java src/test/java
git ls-files target
git ls-files out
git ls-files .idea
git diff --check
```

Se revisan todos los enlaces relativos del README y de la documentación Chimchar, todos los SVG y Draw.io como XML, packages/rutas, archivos generados versionados y posibles credenciales sin exponer sus valores.

## Resultado de la validación final

- `mvn clean test`: BUILD SUCCESS, finalizado 2026-10-07T16:27:31-05:00.
- `mvn clean test jacoco:report`: BUILD SUCCESS, finalizado 2026-10-07T16:27:38-05:00.
- Ambas ejecuciones: 34 pruebas, 0 failures, 0 errors y 0 skipped.
- Medición final del XML: 2026-10-07T16:27:44-05:00; LINE global 179/183 = 97,8142%; ValidadorMision 7/7 = 100%.
- Enlaces: 17 archivos Markdown revisados, 61 enlaces relativos resueltos y cero enlaces rotos.
- XML: seis SVG y dos Draw.io válidos.
- Packages/imports antiguos: cero coincidencias; packages y rutas corresponden.
- `.idea/`, `out/`, `target/` y `*.class` versionados: cero.
- Secretos: cero candidatos detectados en la revisión por patrones de archivos versionados y entregables nuevos; no se imprimieron valores sensibles. Esta revisión básica no equivale a una certificación de seguridad.
- Producción y las nueve pruebas originales de `ValidadorMisionTest` permanecen sin cambios respecto al cierre del bloque anterior.
- `git diff --check`: sin errores.
