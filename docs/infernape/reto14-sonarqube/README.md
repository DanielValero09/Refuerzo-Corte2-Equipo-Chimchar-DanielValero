# Reto 14 - SonarQube Enterprise

**Estado: PENDIENTE TÉCNICO — SONARQUBE.** No se intentó iniciar ni ejecutar SonarQube por decisión explícita del usuario. No se solicitaron tokens, no se fabricaron métricas/capturas y no se declara Quality Gate verde.

## Objetivos oficiales aún no demostrados

- Análisis real Enterprise y captura auténtica del dashboard.
- 0 bugs y 0 vulnerabilidades.
- Deuda técnica <15 minutos; duplicación <3%; cobertura Sonar ≥85%.
- Complejidad ciclomática: analizar métodos respecto al límite académico 10.
- Identificar causa raíz y corregir issues críticos, documentando commits.
- Quality Gate Sonar verde y evolución verificable de métricas.

JaCoCo mide cobertura de ejecución. **No proporciona bugs Sonar, vulnerabilidades Sonar, deuda técnica Sonar, code smells Sonar ni duplicaciones Sonar.** Su check local 85/75 no demuestra los indicadores anteriores. No se reutilizan métricas de Chimchar/Monferno ni se infieren valores.

## Módulos realmente existentes

La fuente menciona PlanificadorRuta, GestorZonas y MonitorFlota. Existe [PlanificadorRuta](../../../src/main/java/infernape/reto3/strategy/PlanificadorRuta.java). No existen clases Enterprise llamadas GestorZonas o MonitorFlota; no se crean para aparentar métricas.

Sí existen [MonitorRed](../../../src/main/java/infernape/reto3/observer/MonitorRed.java), [ServicioResumenRed](../../../src/main/java/infernape/application/ServicioResumenRed.java), los módulos infernape.application, infernape.api e infernape.infrastructure.persistence. Podrán analizarse con sus nombres reales cuando se autorice Sonar. No son una sustitución ficticia de los módulos citados por el material.

La fuente es DOSW_Equipo_Chimchar_fixed.html. Su párrafo del Reto 14 menciona 60 drones, mientras el contexto y el cierre Enterprise indican 100; se conserva el contexto de 100 del proyecto sin inventar resultados de telemetría.

## Evidencia disponible y pendiente

[JaCoCo real y gate Maven local](../reto13-jacoco/README.md) está disponible. Análisis, dashboard, métricas y captura Sonar siguen pendientes. No se ejecuta ningún comando Sonar en este bloque.
