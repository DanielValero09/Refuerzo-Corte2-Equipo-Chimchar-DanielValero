# Evolución Infernape — SkyCampus Enterprise

Fuente principal: `DOSW_Equipo_Chimchar_fixed.html`, contexto Enterprise y retos 01–04, con la simulación aislada de Git autorizada por el usuario.

## Contexto y alcance de implementación

Enterprise conecta ECI, UNAL, UNIANDES y EAFIT mediante una red compartida. El enunciado contempla 100 drones, cinco tipos/perfiles, transferencias entre sedes, estaciones intermedias de carga, rutas multi-etapa, optimización y analytics por sede, autorización Aerocivil y panel de Superadministrador.

Nuevos participantes: Superadministrador de red, Coordinador por sede, Aerocivil y Estación de carga autónoma. Son contexto de la evolución, no integraciones reales ya implementadas en este bloque.

**La fuente declara cinco tipos pero solo nombra MINI, CARGO y EXPRESS heredados de Monferno.** No se inventan nombres oficiales para los otros dos. `PerfilDrone` describe código y límites de peso por datos, con tres constantes conocidas; permite perfiles futuros sin un enum cerrado ni switch creciente. `PERFIL_DE_PRUEBA` existe únicamente en una prueba y no se presenta como oficial. No se genera una flota ficticia de 100 elementos para aparentar telemetría.

Este bloque implementa analytics, cuatro patrones combinados y asignación por tres capas con adaptadores locales. No implementa hardware, HTTP, BD, Aerocivil, transferencia persistida de drones o panel interactivo. Las distancias y tiempos de pruebas son ejemplos reproducibles, no mediciones reales entre universidades.

## Índice del bloque 1/4

- [Reto 1 - Analytics con una sola pasada](reto1-analytics.md)
- [Reto 2 - Release, hotfix y tags académicos](reto2-gitflow/README.md)
- [Reto 3 - Composite, Strategy, Observer y Factory Method](reto3-patrones/README.md)
- [Reto 4 - SOLID y arquitectura por capas](reto4-solid/README.md)

## Compatibilidad y validación

Los packages `infernape.*` son propios. No se sustituyen fuentes o pruebas Chimchar/Monferno ni se cambia pom.xml. Se conservan Java release 17, JUnit, Mockito y el check JaCoCo 80% LINE / 70% BRANCH; 85%/75% se reserva para Reto 13 Enterprise.

Base remota exacta: `origin/evolution/monferno`, `0cd28e03049120bd09ffc92cc8246698356e41b6`. La rama de trabajo es evolution/infernape; main, develop y las evoluciones previas permanecen protegidas.

Primera validación estable: `mvn clean verify`, 2026-10-08T12:37:54-05:00: **155 pruebas = 34 Chimchar + 70 Monferno + 51 Infernape**, cero failures/errors/skipped, BUILD SUCCESS y `All coverage checks have been met.`. El test de arquitectura tuvo inicialmente un escape regex incorrecto; se corrigió antes de esta validación.

No se inicia Reto 5. SonarQube Chimchar/Monferno y capturas Jira siguen pendientes; no se intenta resolverlos en este bloque.

Validación final tras integrar el hotfix: `mvn clean verify`, 2026-10-08T12:55:50-05:00: **157 pruebas = 34 Chimchar + 70 Monferno + 53 Infernape**, 0 failures/errors/skipped, BUILD SUCCESS y check JaCoCo heredado satisfactorio. [Evidencia real](reto2-gitflow/validacion-final.txt).

Auditoría de tamaño con el parser Java: 39 fuentes de producción y 10 de pruebas/soporte; ninguna clase supera 150 líneas ni ningún método de producción supera 15 líneas de cuerpo. Dos pruebas integrales conservan 18 y 17 líneas de cuerpo (incluyendo comentarios AAA y llaves) para hacer visibles preparación, interacción y resultado.

Auditoría final: 188 enlaces relativos comprobados sin roturas; 23 SVG y 4 Draw.io válidos (incluidos los cinco SVG nuevos revisados visualmente con Chrome headless); 137 archivos previos y diez ramas locales protegidas intactos. Sin packages antiguos, ciclos imperativos en Reto 1, instanceof en Enterprise, imports externos prohibidos en dominio, secretos detectados o archivos generados versionados. `git diff --check` pasa. Las menciones genéricas de tokens/credenciales en la documentación previa no son secretos reales.
