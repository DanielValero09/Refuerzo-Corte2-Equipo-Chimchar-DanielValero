# Evolución Infernape — SkyCampus Enterprise

Fuente principal: `DOSW_Equipo_Chimchar_fixed.html`, contexto Enterprise y retos 01–14, con la simulación aislada de Git del bloque 1 autorizada por el usuario.

## Contexto y alcance de implementación

Enterprise conecta ECI, UNAL, UNIANDES y EAFIT mediante una red compartida. El enunciado contempla 100 drones, cinco tipos/perfiles, transferencias entre sedes, estaciones intermedias de carga, rutas multi-etapa, optimización y analytics por sede, autorización Aerocivil y panel de Superadministrador.

Nuevos participantes: Superadministrador de red, Coordinador por sede, Aerocivil y Estación de carga autónoma. Son contexto de la evolución, no integraciones reales ya implementadas en este bloque.

**La fuente declara cinco tipos pero solo nombra MINI, CARGO y EXPRESS heredados de Monferno.** No se inventan nombres oficiales para los otros dos. `PerfilDrone` describe código y límites de peso por datos, con tres constantes conocidas; permite perfiles futuros sin un enum cerrado ni switch creciente. `PERFIL_DE_PRUEBA` existe únicamente en una prueba y no se presenta como oficial. No se genera una flota ficticia de 100 elementos para aparentar telemetría.

El bloque 1/4 implementó analytics, cuatro patrones combinados y asignación por tres capas con adaptadores locales. Su alcance no incluyó hardware, HTTP/BD reales, integración Aerocivil, transferencia persistida de drones o panel interactivo. Las distancias y tiempos de pruebas son ejemplos reproducibles, no mediciones reales entre universidades.

## Índice del bloque 1/4

- [Reto 1 - Analytics con una sola pasada](reto1-analytics.md)
- [Reto 2 - Release, hotfix y tags académicos](reto2-gitflow/README.md)
- [Reto 3 - Composite, Strategy, Observer y Factory Method](reto3-patrones/README.md)
- [Reto 4 - SOLID y arquitectura por capas](reto4-solid/README.md)

## Compatibilidad y validación

Los packages `infernape.*` son propios. No se sustituyen fuentes o pruebas Chimchar/Monferno. Los bloques 1 y 2 conservaron pom.xml; el bloque 3 agrega Spring Boot y H2 para las pruebas REST. Se conservan Java release 17, JUnit 5.11.4, Mockito 5.15.2 y JaCoCo 0.8.11; el check actual del Reto 13 Enterprise es LINE 85% / BRANCH 75%.

Base remota exacta: `origin/evolution/monferno`, `0cd28e03049120bd09ffc92cc8246698356e41b6`. La rama de trabajo es evolution/infernape; main, develop y las evoluciones previas permanecen protegidas.

Primera validación estable: `mvn clean verify`, 2026-10-08T12:37:54-05:00: **155 pruebas = 34 Chimchar + 70 Monferno + 51 Infernape**, cero failures/errors/skipped, BUILD SUCCESS y `All coverage checks have been met.`. El test de arquitectura tuvo inicialmente un escape regex incorrecto; se corrigió antes de esta validación.

En el bloque 1/4 no se inició Reto 5. SonarQube Chimchar/Monferno y capturas Jira siguen pendientes; no se intenta resolverlos en este bloque.

Validación final tras integrar el hotfix: `mvn clean verify`, 2026-10-08T12:55:50-05:00: **157 pruebas = 34 Chimchar + 70 Monferno + 53 Infernape**, 0 failures/errors/skipped, BUILD SUCCESS y check JaCoCo heredado satisfactorio. [Evidencia real](reto2-gitflow/validacion-final.txt).

Auditoría de tamaño con el parser Java: 39 fuentes de producción y 10 de pruebas/soporte; ninguna clase supera 150 líneas ni ningún método de producción supera 15 líneas de cuerpo. Dos pruebas integrales conservan 18 y 17 líneas de cuerpo (incluyendo comentarios AAA y llaves) para hacer visibles preparación, interacción y resultado.

Auditoría final del bloque 1/4: 188 enlaces relativos comprobados sin roturas; 23 SVG y 4 Draw.io válidos (incluidos los cinco SVG nuevos revisados visualmente con Chrome headless); 137 archivos previos y diez ramas locales protegidas intactos. Sin packages antiguos, ciclos imperativos en Reto 1, instanceof en Enterprise, imports externos prohibidos en dominio, secretos detectados o archivos generados versionados. `git diff --check` pasa. Las menciones genéricas de tokens/credenciales en la documentación previa no son secretos reales.

## Índice del bloque 2/4

- [Reto 5 - C4 Enterprise, niveles 1 y 2](reto5-c4/README.md).
- [Reto 6 - Matriz de trazabilidad](reto6-trazabilidad.md).
- [Reto 7 - Plantilla DOSW SC-15](reto7-dosw-sc15.md).
- [Reto 8 - Design tokens y UX multi-sede](reto8-ux/README.md).

El bloque 2 agrega configuración operativa subordinada a restricciones, puerto de autorización Aerocivil y simulación local de timeout, transferencia lógica de drones y resumen inmutable de red. No cambia fuentes/tests previos ni pom.xml. La arquitectura C4 y el flujo SC-15 son objetivos; no se afirma ejecución completa de vuelo, HTTP/JDBC/BD reales o control de hardware.

Validación: `mvn clean verify`, 2026-10-08T15:54:27-05:00: **186 pruebas = 34 Chimchar + 70 Monferno + 53 Infernape bloque 1 + 29 bloque 2**, Failures/Errors/Skipped 0, BUILD SUCCESS y check heredado JaCoCo 80/70 aprobado. [Evidencia real](reto6-validacion.txt).

Los cuatro SVG nuevos y el HTML se renderizaron realmente con Chrome headless. Se corrigió el desbordamiento de nombres de actores antes de la revisión final; no quedan recortes. El navegador confirmó tres paneles y tres tarjetas equivalentes, 37 reglas CSS, tokens exactos y ausencia de desbordamiento horizontal. No hay scripts o fuentes de red obligatorias en los componentes.

Al cierre del bloque 2 no se habían iniciado Retos 9–14. El bloque 3 añade Retos 9–12, conservando pendientes Retos 13 y 14. SonarQube Chimchar/Monferno y capturas Jira anteriores continúan pendientes. Los tags v3.0.0/v3.0.1 y todas las ramas distintas de evolution/infernape se conservan.

Auditoría del bloque 2/4: 224 enlaces relativos válidos; 27 SVG y 6 Draw.io XML válidos en todo docs (cuatro SVG y dos Draw.io nuevos); ningún RF sin CU/HU/prueba. Se preservan 199 archivos previos fuera de los dos índices autorizados, catorce ramas locales distintas de la evolución actual y los dos tags anotados. Sin secretos detectados, packages antiguos, imports externos/red/BD en dominio o generados versionados; git diff --check pasa.


## Infernape Reto 9 - Roadmap Jira

[Planificación real y ceremonias](reto9-jira/README.md), [roadmap](reto9-jira/roadmap.md) y [retrospectiva simulada](reto9-jira/retrospectiva-simulada.md). Jira real: ocho HU, tres sprints futuros y 45 SP; capacidad estimada de 20 SP por sprint, sin velocidad histórica medida. Captura auténtica pendiente.

## Infernape Reto 10 - Casos de Uso

[Cinco paquetes y relaciones UML](reto10-casos-uso/README.md) · [diagrama general](reto10-casos-uso/casos-uso-enterprise.svg) · [Draw.io editable](reto10-casos-uso/casos-uso-enterprise.drawio).

## Infernape Reto 11 - Prototipo Navegable

[Prototipo y evidencia](reto11-prototipo/README.md) · [HTML interactivo](reto11-prototipo/index.html). Temas ECI/UNAL, cuatro sedes y seis errores; 26 comprobaciones reales en Chrome. Prueba humana con compañero pendiente.

## Infernape Reto 12 - TDD e Integración REST

[Red → Green → Refactor y tres capas de pruebas](reto12-tdd/README.md). POST /api/v3/misiones, MockMvc y H2 real en memoria. 226 pruebas aprobadas; cobertura global LINE 98.42% / BRANCH 92.03%. Estas cifras corresponden al cierre del bloque 3; el bloque 4 eleva el check a 85/75 y documenta métricas actualizadas abajo. Sonar permanece pendiente.

## Infernape Reto 13 — JaCoCo Enterprise

[Métricas, snapshots y captura auténtica](reto13-jacoco/README.md). Global: LINE **98.4273%**, BRANCH **92.0290%**; Infernape: LINE **97.9487%**, BRANCH **91.1765%**. `mvn -B -ntp clean verify`: 228 pruebas aprobadas y Quality Gate LOCAL Maven LINE ≥85% / BRANCH ≥75%, sin exclusiones.

Estado: **PARCIAL — JACOCO COMPLETO / SONAR PENDIENTE**. El check local no demuestra Quality Gate Sonar.

## Infernape Reto 14 — SonarQube

**PENDIENTE TÉCNICO — SONARQUBE** por decisión del usuario. [Objetivos oficiales aún no demostrados](reto14-sonarqube/README.md). No se ejecuta Sonar, no se fabrican métricas ni capturas.

## Estado final de Infernape

[Auditoría de los 14 retos y límites REST](entrega-final.md). Reto 11 parcial por prueba con compañero, Reto 13 parcial por Sonar y Reto 14 pendiente; captura Jira auténtica pendiente. No se declara 14/14.
