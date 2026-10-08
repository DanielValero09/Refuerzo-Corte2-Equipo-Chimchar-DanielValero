# Monferno Reto 14 - SonarQube

Estado: **PENDIENTE TÉCNICO — SONARQUBE**.

Por decisión del usuario no se intenta SonarQube durante el bloque Monferno 4/4. No se ejecutaron Docker, scanner, configuración o análisis Sonar; no se reutilizan resultados de Chimchar ni se deducen métricas Sonar desde JaCoCo.

## Objetivo oficial pendiente

Fuente: `DOSW_Equipo_Chimchar_fixed.html`, apartado 14 Monferno.

- Ejecutar SkyCampus v2 en SonarQube.
- Obtener Quality Gate verde.
- Verificar cobertura >=85% dentro de Sonar.
- Alcanzar 0 bugs y 0 vulnerabilidades.
- Dejar los code smells del nivel Monferno en 0.
- Alcanzar deuda técnica <=30 minutos.
- Incorporar captura real del dashboard; conservar medición antes/después si se realizan correcciones.

Estos valores son **objetivos pendientes**, no resultados obtenidos. El apartado 13 también exige análisis y capturas Sonar antes/después, con deuda <30 minutos; por ello ese reto se mantiene parcial aunque JaCoCo pase.

## Evidencia disponible y ausente

| Elemento | Situación real |
| --- | --- |
| JaCoCo y comprobación local | Completados; ver [Reto 13](../reto13-jacoco/README.md) |
| Quality Gate Sonar | Sin análisis, resultado desconocido |
| Bugs, vulnerabilidades, code smells y deuda Sonar | Sin análisis, sin métricas |
| Cobertura reportada por Sonar | Sin análisis; no se sustituye por JaCoCo |
| Dashboard antes/después | Sin captura Sonar |

## Pasos futuros, sin ejecutar

1. Disponer de una instancia SonarQube accesible y autenticación autorizada, manteniendo credenciales fuera de Git.
2. Ejecutar `mvn clean verify` para generar los resultados Surefire y `target/site/jacoco/jacoco.xml`.
3. Ejecutar un scanner Maven compatible para un proyecto Monferno, importando ese XML de cobertura.
4. Conservar las métricas iniciales reales y resolver issues según prioridad: bugs, vulnerabilidades y code smells.
5. Repetir pruebas y análisis; verificar el Quality Gate y las metas anteriores con métricas reales.
6. Incorporar capturas auténticas y actualizar [entrega-final.md](../entrega-final.md).

Este bloque no inicia Infernape ni crea tag v2.0.0. SonarQube Chimchar permanece pendiente y no se interviene en su configuración.
