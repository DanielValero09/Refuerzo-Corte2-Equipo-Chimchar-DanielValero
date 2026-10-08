# Estado de entrega Monferno — SkyCampus v2

Auditoría del 2026-10-08 sobre `evolution/monferno`, base del bloque 4/4 `c5ec4ec47c65e1c6a801548758ef88bc488fa7df`. Fuente principal: `DOSW_Equipo_Chimchar_fixed.html`, retos Monferno 1–14, con las precisiones autorizadas por el usuario en cada bloque.

Los Retos 1–12 tienen sus entregables académicos de código, documentación, diagramas, prototipos o pruebas. **COMPLETO** en esta tabla se refiere al reto concreto; no certifica una aplicación desplegada, todas las historias Jira implementadas ni el DoD final. Reto 9 conserva pendiente su captura visual. Reto 13 es parcial y Reto 14 pendiente por SonarQube: no se declara 14/14 ni se crea tag v2.0.0.

## Checklist de los 14 retos

| Reto | Estado | Evidencia | Observación |
| --- | --- | --- | --- |
| 1 | COMPLETO | [Streams](reto1-streams.md); `monferno.reto1.EstadisticasMisionesTest` | Cuatro consultas, groupingBy/counting/max, Optional, empate por ID, lista vacía y límite temporal; 12 pruebas; sin ciclos imperativos. |
| 2 | COMPLETO | [GitFlow real](reto2-gitflow.md); commits 7f9885d, 806a25a, 2f1d6ba y 709928a | Base común, dos features, conflicto real y resolución conservando ambos cambios; grafo alcanzable desde esta evolución; develop real intacto. No hubo merges ni cambios de rama durante el bloque 4/4. |
| 3 | COMPLETO | [Strategy y Observer](reto3-patrones.md); EstrategiasAsignacionTest y GestorFlotaObserverTest | Tres estrategias base, cadena de aptitud/capacidad, tres observadores y prueba de cuarto suscriptor. La política URGENTE se incorporó después en Reto 12. |
| 4 | COMPLETO | [SOLID](reto4-solid.md); GestorMisionesStrategyTest | SRP, OCP y DIP concretos; misma instancia con estrategias distintas y validador adicional; ISP explicado, sin inventar violación LSP. |
| 5 | COMPLETO | [C4](reto5-c4/README.md); [SVG](reto5-c4/contexto-monferno.svg); [Draw.io](reto5-c4/contexto-monferno.drawio) | Caja negra, cuatro actores, tres sistemas externos, 14 flujos y comparación con Chimchar. |
| 6 | COMPLETO | [RF/RNF y MoSCoW](reto6-requerimientos.md) | RF-07–10, RNF-04–07 y ocho prioridades; rapidez URGENTE prevalece sobre mayor batería, respetando aptitud. Las métricas RNF son requisitos, no mediciones operativas ya aprobadas. |
| 7 | COMPLETO | [DOSW SC-07](reto7-dosw.md) | Entradas tipadas y subobjetos, siete pasos, tres alternos, siete reglas y trazabilidad. Contrato funcional documentado; no se presenta como UI/red implementada. |
| 8 | COMPLETO | [Diseño UX](reto8-ux/README.md); [Componentes](reto8-ux/sistema-diseno.md) | Identidad Chimchar reutilizada, cinco estados de tarjeta/botón, tres rangos de batería y tres pantallas; Fitts, Hick y Miller; EN_CARGA/MANTENIMIENTO agregados sin eliminar estados. |
| 9 | COMPLETO — captura visual pendiente | [Jira real](reto9-jira/README.md) | SCRUM-13, HU SCRUM-14–18 y sprint 3 verificados mediante conector autenticado en el bloque 3/4; 19/20 points, diez Gherkin y DoD reales. Falta PNG auténtico. |
| 10 | COMPLETO | [Casos de uso](reto10-casos-uso/README.md); [SVG](reto10-casos-uso/casos-uso-monferno.svg); [Draw.io](reto10-casos-uso/casos-uso-monferno.drawio) | Cuatro actores humanos, Técnico → Operador, 15 CU internos, tres include y dos extend con condiciones y direcciones correctas. |
| 11 | COMPLETO | [Seis mocks](reto11-mocks/README.md); [Prompt](reto11-mocks/prompt-utilizado.md); [Nielsen](reto11-mocks/heuristicas-nielsen.md) | Flujo panel/formulario/confirmación, errores sin drones/clima/peso y nueve heurísticas concretas; SVG estáticos, datos y ETA ilustrativos. |
| 12 | COMPLETO | [TDD real](reto12-tdd.md); commits ff952d6 → 2e8bf86 → 9610944 → 84d8d25 | Setup, cinco tests antes de producción (Red), Green y Refactor; Mockito clima/notificador, AAA, nueve tests adicionales separados. |
| 13 | PARCIAL — JACOCO COMPLETO / SONAR PENDIENTE | [JaCoCo y umbrales](reto13-jacoco/README.md); [PNG real](reto13-jacoco/reporte-jacoco-monferno.png) | LINE global 98,93%, BRANCH 93,40%; Monferno LINE 100%, BRANCH 91,89%; check BUNDLE 80%/70% aprobado. El análisis Sonar también exigido sigue pendiente. |
| 14 | PENDIENTE TÉCNICO — SONARQUBE | [Pendiente Sonar](reto14-sonarqube/README.md) | No se intentó por decisión del usuario. Sin Quality Gate Sonar, métricas o capturas; no se infieren desde JaCoCo. |

## Verificación Java y cobertura

Medición base real: `mvn clean test jacoco:report`, BUILD SUCCESS, 2026-10-08T11:56:29-05:00. Verificación final del código/configuración: `mvn clean verify`, BUILD SUCCESS, 2026-10-08T11:58:15-05:00.

```text
[INFO] Tests run: 104, Failures: 0, Errors: 0, Skipped: 0
[INFO] --- jacoco:0.8.11:check (check) @ chimchar ---
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
```

34 pruebas Chimchar y 70 Monferno. No se añadieron pruebas porque la medición base ya superaba el objetivo >=85% LINE. Las cinco originales de AsignadorMisionTest y las 34 Chimchar permanecen sin cambios; también se conserva el resto de fuentes y pruebas Java.

| Alcance | LINE covered/missed | LINE | BRANCH covered/missed | BRANCH |
| --- | ---: | ---: | ---: | ---: |
| Global, Chimchar + Monferno | 369/4 | 98,93% | 99/7 | 93,40% |
| Packages Monferno agregados | 190/0 | 100% | 68/6 | 91,89% |

Inicial y final coinciden: solo se agregó la ejecución check al plugin 0.8.11 existente, sin exclusiones ni cambios de producción. [Snapshots y cobertura por package/clase](reto13-jacoco/README.md) conservan contadores reales del XML. La captura se obtuvo directamente del reporte HTML con Chrome headless y se revisó visualmente; no se recreó el informe.

## Alcance operativo y afirmaciones

- ApiMeteorologica es una abstracción local simulada con Mockito; no se demuestra HTTP ni timeout de red real. Los RNF de latencia son requisitos documentados, no resultados medidos en producción.
- SC-07 describe asociación PENDIENTE antes del inicio. La fachada de Reto 2 conserva ese contrato; el ejercicio TDD de Reto 12 combina selección e inicio y devuelve una copia Drone EN_VUELO/no disponible. No modifica la Mision ni la lista recibida ni persiste la transición; el consumidor debe conservar el resultado.
- El aviso por batería 30–40% es una extensión del diagrama académico. No se atribuye a AlertaTecnico existente, que notifica FALLO.
- Autorización aérea, registro persistente de reparación y acciones de UI son contratos/prototipos, no integraciones operativas implementadas en estos bloques.
- Jira existe realmente y la planificación fue verificada. Las historias y su DoD no se marcan como ejecutados ni se afirma revisión mediante PR, Sonar o todas las condiciones Gherkin cumplidas.
- La cobertura JaCoCo no establece bugs, vulnerabilidades, code smells, deuda técnica ni Quality Gate Sonar.

## Integridad y reproducción

La validación recorre todos los enlaces Markdown del README y documentación, todos los SVG y Draw.io, declaraciones package/rutas, archivos generados seguidos por Git y menciones de credenciales. Las referencias locales a target dentro de bloques de comandos son rutas de reportes regenerables, no enlaces a archivos versionados.

```powershell
git branch --show-current
mvn clean verify
git diff --check
rg -n "src\.main\.java" src/main/java src/test/java
git ls-files target out .idea
```

La revisión de secretos captura los resultados de la búsqueda solicitada en memoria, clasifica menciones genéricas documentales y evita imprimir valores sensibles. No se almacenan tokens o credenciales. Los resultados finales de enlaces/XML y heads protegidos se registran en el [índice Monferno](README.md).

Resultados reales: **36 Markdown, 163 enlaces relativos y cero rotos; 18 SVG y 4 Draw.io XML válidos; 75 archivos Java previos intactos; nueve heads protegidos sin cambios**. Cero packages antiguos, archivos generados versionados o credenciales detectadas. Las ocho coincidencias de la búsqueda son menciones documentales genéricas, incluida una expresión PowerShell de conversión de una variable temporal, sin literal de credencial. `git diff --check` aprobado; los avisos LF/CRLF no representan errores de formato.

## Pendientes reales de cierre

1. SonarQube Monferno: análisis real y evidencia necesaria para completar también Reto 13 y Reto 14.
2. SonarQube Chimchar: continúa pendiente; no se intervino en este bloque.
3. Captura auténtica del Sprint 1 Monferno y captura Jira Chimchar, todavía ausentes del repositorio.

No se creó tag v2.0.0 ni se inició Infernape. El trabajo de este bloque permanece exclusivamente en evolution/monferno, sin PR, merge, rebase, reset ni force push.
