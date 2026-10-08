# Monferno Reto 2 - GitFlow

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 02; simulación autorizada en ramas temporales, sin modificar develop real ni las ramas existentes de Chimchar.

## Base común e historial real

La evolución se creó desde `origin/evolution/chimchar` en `2c7fe37f06f09d6a0b950c73f44da9a9eb46f337`. Después de compilar la base Monferno (83 pruebas, cero fallos/errores), se crearon integración y ambas features desde exactamente el mismo commit:

`7f9885d2677ed5d922d1381fde2168443176e971` — feat: agrega modelo y patrones base de Monferno.

| Paso | Rama | Commit real |
| --- | --- | --- |
| Base común | evolution/monferno | 7f9885d2677ed5d922d1381fde2168443176e971 |
| Asignación automática | feature/monferno-asignacion | 5d427ce7884131de1d31bb31adf60e24c10c557b |
| Notificación | feature/monferno-alertas | d29010daf610bf6fde03f60a9e1a9479a2c01816 |
| Merge 1, asignación | integration/monferno-develop | 806a25a2f9538657064e2729311a06c041f54d03 |
| Merge 2, resolución manual | integration/monferno-develop | 2f1d6ba841770769b5b1efe1338071739f647acb |
| Integración final válida | evolution/monferno | 709928a2bf024608b5b50ab70e8d039ab87b43b8 |

`git merge-base feature/monferno-asignacion feature/monferno-alertas` devolvió el commit común `7f9885d2677ed5d922d1381fde2168443176e971`.

## Cambios compatibles y conflicto real

Archivo: `src/main/java/monferno/reto2/AsignadorMision.java`, fachada útil que devuelve una nueva misión asignada y recibe el notificador por dependencia.

La base conservaba una asociación ya existente. La feature de asignación cambió el mismo return para seleccionar automáticamente mediante GestorMisiones; la de alertas agregó la notificación a esa asociación. Ambas modificaron la misma línea para demostrar el conflicto de integración.

Comandos reales en la integración temporal:

```powershell
git merge --no-ff feature/monferno-asignacion -m "merge: integra asignacion Monferno"
git merge --no-ff feature/monferno-alertas -m "merge: integra asignacion y alertas Monferno"
```

El primer merge finalizó mediante la estrategia ort. El segundo devolvió código 1 y esta salida real:

```text
Auto-merging src/main/java/monferno/reto2/AsignadorMision.java
CONFLICT (content): Merge conflict in src/main/java/monferno/reto2/AsignadorMision.java
Automatic merge failed; fix conflicts and then commit the result.
```

Git status mostró `You have unmerged paths` y `both modified: src/main/java/monferno/reto2/AsignadorMision.java`. El archivo contenía las marcas de HEAD y feature/monferno-alertas.

## Resolución manual conservando ambos cambios

Asignación aportaba:

```java
return gestor.asignar(flota, mision).map(mision::conDrone);
```

Alertas aportaba:

```java
return mision.drone().map(mision::conDrone).map(this::notificar);
```

Se editaron manualmente las marcas para conservar selección automática, creación de la misión y notificación posterior:

```java
return gestor.asignar(flota, mision).map(mision::conDrone).map(this::notificar);
```

No se usó checkout --ours/--theirs como solución. Se añadieron pruebas que verifican el drone seleccionado, una sola notificación con el resultado y cero notificaciones si no hay candidato. Antes de concluir el merge se ejecutó `mvn clean test`: 85 pruebas, 0 failures, 0 errors, 0 skipped, BUILD SUCCESS (2026-10-07T23:03:45-05:00).

Después de marcar la resolución mediante git add, git status indicó `All conflicts fixed but you are still merging`. El commit `2f1d6ba` conserva dos padres: `806a25a` y `d29010d`.

Se regresó a evolution/monferno y se ejecutó un merge normal con --no-ff desde integration/monferno-develop. El resultado compilable y útil queda en producción; no se conserva una simulación defectuosa como clase de ejemplo.

## Grafo real

Salida recortada de `git log --oneline --graph --decorate --all -12`, capturada tras la integración y antes del commit documental final; únicamente se retiraron espacios finales:

```text
*   709928a (HEAD -> evolution/monferno) merge: incorpora simulacion GitFlow Monferno
|\
| *   2f1d6ba (integration/monferno-develop) merge: integra asignacion y alertas Monferno
| |\
| | * d29010d (feature/monferno-alertas) feat: notifica asignaciones Monferno
| |/
|/|
| * 806a25a merge: integra asignacion Monferno
|/|
| * 5d427ce (feature/monferno-asignacion) feat: delega asignacion automatica Monferno
|/
* 7f9885d feat: agrega modelo y patrones base de Monferno
* 2c7fe37 (origin/evolution/monferno, origin/evolution/chimchar, evolution/chimchar) chore: completa calidad y auditoria final de Chimchar
```

## Política y ramas protegidas

Merge para ramas compartidas preserva el historial; rebase únicamente en ramas locales no publicadas, según el material. En este bloque no se hizo rebase ni se reescribió historial.

develop real conserva `bca19dda94a9a5f6a98fe3a5cb46805fcb93226e`; evolution/chimchar conserva el cierre `2c7fe37`. main y las feature existentes no recibieron cambios. Las ramas temporales quedan locales: todos sus commits son alcanzables desde evolution/monferno mediante merges y se publican con esa evolución. No se crearon PR ni tag v2.0.0.
