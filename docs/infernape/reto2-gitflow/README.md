# Reto 2 - Release, hotfix y tags

## Alcance seguro

Simulación académica autorizada: `simulation/infernape-main` y `simulation/infernape-develop` nacieron del mismo commit de `evolution/infernape`. Representan main/develop solo para el ejercicio. **Los tags v3.0.0 y v3.0.1 no liberan main real ni declaran completa la evolución Enterprise.** No se creó PR ni se reescribió historial compartido.

## Historial verificable

| Paso | Commit real |
| --- | --- |
| Base común | `f9f97db52ba8e04d0ddeb4c2909c0991e06ece7e` |
| Preparación release 1 | `c047df5896ef121ed307fbc2e7c21e443293d27a` |
| Preparación release 2 | `3df72221ee1ad9fa5633a8694c1c5058506ba05e` |
| Merge release → simulation-main | `1f1e7072b50d68e76ff4ec78edae7e672c5c6f35` |
| Merge release → simulation-develop | `fe7858911e29ff7957f94083e6dcc39d4666ce31` |
| Hotfix de producción + regresiones | `9b3ac24889660327ba09851a942d1ac24ae23658` |
| Merge hotfix → simulation-main | `6c4b1c3f8662c4a0c257e1a07332dbd4a76c8133` |
| Merge hotfix → simulation-develop | `bbd849f783ba7746d7f9c59170de793f4acae3e7` |
| Integración → evolution/infernape | `97ddf1ae0539cc41c08062152a0bc6652a2535ad` |

`release/v3.0.0` contiene exactamente dos commits de preparación desde la base: [VERSION.md](VERSION.md) y [notas de release](release-notes-v3.0.0.md). Se hicieron merges `--no-ff` hacia ambas ramas simuladas. Luego `hotfix/ruta-inter-sede-unal` partió de simulation-main, conservó la release y corrigió producción; sus merges alcanzaron ambas ramas simuladas y el estado corregido se integró mediante `--no-ff` a evolution/infernape.

Los merges preservan historia de ramas compartidas. Un rebase solo sería adecuado para trabajo local no publicado; aquí no se ejecutó rebase, reset ni force push. Las ramas de simulación permanecen locales; el grafo se conserva mediante merges en la evolución y los tags anotados.

## Tags anotados

| Tag | Objeto anotado | Commit señalado (main simulado) |
| --- | --- | --- |
| v3.0.0 | `e175eec98cce0862d37d270e29b21b53d02235b0` | `1f1e7072b50d68e76ff4ec78edae7e672c5c6f35` |
| v3.0.1 | `d086952790e43534243dc4eb97c340c70d9830d9` | `6c4b1c3f8662c4a0c257e1a07332dbd4a76c8133` |

- v3.0.0: “SkyCampus Enterprise v3.0.0 — simulación académica”.
- v3.0.1: “SkyCampus Enterprise v3.0.1 — hotfix académico”.

Se comprobó previamente la ausencia de ambos tags locales y remotos. No se reemplaza ningún tag anterior y no se crea v2.0.0.

## Hotfix real: estación no disponible en ruta ECI → UNAL

La optimización inicial comparaba distancia/recargas y omitía consultar `EstacionCarga.disponible`. La regresión mostró que elegía la alternativa de 6 km por C-116 cerrada, por delante de una ruta directa operable de 10 km; también devolvía una ruta cuando todas requerían esa estación cerrada. No se introdujo una falla deliberada para el ejercicio.

Primero se añadieron dos pruebas a [PlanificadorRutaHotfixTest](../../../src/test/java/infernape/reto3/PlanificadorRutaHotfixTest.java), sin cambiar producción. Comando real:

```powershell
mvn "-Dtest=infernape.reto3.PlanificadorRutaHotfixTest" test
```

Fragmento real anterior a la corrección:

```text
[ERROR] Tests run: 2, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.200 s <<< FAILURE! -- in infernape.reto3.PlanificadorRutaHotfixTest
[ERROR] Tests run: 2, Failures: 2, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
[INFO] Finished at: 2026-10-08T12:52:47-05:00
```

La corrección en [PlanificadorRuta](../../../src/main/java/infernape/reto3/strategy/PlanificadorRuta.java) filtra rutas cuyas etapas visitan estaciones no disponibles (origen o destino) antes de delegar la optimización. Conserva las dos estrategias y no usa instanceof ni decisiones según clases concretas. Cuando no queda ruta operable devuelve Optional.empty. La disponibilidad es un snapshot local; no se implementa hardware ni una consulta remota.

Fragmento real posterior:

```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.129 s -- in infernape.reto3.PlanificadorRutaHotfixTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[INFO] Finished at: 2026-10-08T12:53:15-05:00
```

## Integridad de ramas protegidas

| Rama real local | Antes | Después |
| --- | --- | --- |
| main | `72a554d1aeb4b292137b91235f10aa8cfb554ce6` | `72a554d1aeb4b292137b91235f10aa8cfb554ce6` |
| develop | `bca19dda94a9a5f6a98fe3a5cb46805fcb93226e` | `bca19dda94a9a5f6a98fe3a5cb46805fcb93226e` |
| evolution/chimchar | `2c7fe37f06f09d6a0b950c73f44da9a9eb46f337` | `2c7fe37f06f09d6a0b950c73f44da9a9eb46f337` |
| evolution/monferno | `0cd28e03049120bd09ffc92cc8246698356e41b6` | `0cd28e03049120bd09ffc92cc8246698356e41b6` |

También se verificaron por lectura los remotos contra las referencias obtenidas en el fetch inicial: `origin/main` conserva `f012b9549c4a56694cf29d45558ebb08dad04224` y `origin/develop` conserva `bca19dda94a9a5f6a98fe3a5cb46805fcb93226e`; las evoluciones remotas Chimchar/Monferno conservan los hashes de la tabla. La rama local main estaba en un commit distinto de origin/main desde antes del bloque; ninguna se actualizó ni se modificó.

## Grafo real

Salida de `git log --oneline --graph --decorate --all -10`, capturada después de integrar la simulación y antes del commit documental/publicación:

```text
*   97ddf1a (HEAD -> evolution/infernape) feat: integra simulacion release y hotfix Enterprise
|\  
| *   bbd849f (simulation/infernape-develop) fix: integra hotfix de ruta en develop simulado
| |\  
| * | fe78589 chore: integra release Enterprise en develop simulado
|/| | 
| | | *   6c4b1c3 (tag: v3.0.1, simulation/infernape-main) fix: integra hotfix de ruta en main simulado
| | | |\  
| | | |/  
| | |/|   
| | * | 9b3ac24 (hotfix/ruta-inter-sede-unal) fix: corrige validacion de ruta inter-sede
| | |/  
| | * 1f1e707 (tag: v3.0.0) chore: integra release Enterprise en main simulado
| |/| 
|/|/  
| * 3df7222 (release/v3.0.0) docs: agrega notas de release Enterprise 3.0.0
| * c047df5 chore: prepara version Enterprise 3.0.0
|/  
* f9f97db feat: agrega analytics patrones y capas Enterprise
* 0cd28e0 (origin/evolution/monferno, origin/evolution/infernape, evolution/monferno) docs: audita entrega de Monferno
```

## Validación final del código integrado

`mvn clean verify`, 2026-10-08T12:55:50-05:00: **157 pruebas = 34 Chimchar + 70 Monferno + 53 Infernape**, Failures 0, Errors 0, Skipped 0, BUILD SUCCESS; código de salida Maven 0. El check heredado JaCoCo LINE ≥80% / BRANCH ≥70% pasa. No se cambia pom.xml ni se adelanta el Quality Gate Enterprise de Reto 13.

[Salida real de validación](validacion-final.txt). La advertencia de auto-attach de Mockito en JDK 21 no es un fallo de pruebas; un pipeline PowerShell la trató como stderr y devolvió un código de wrapper 1, por lo que la ejecución final conservó directamente el código real de Maven (0).
