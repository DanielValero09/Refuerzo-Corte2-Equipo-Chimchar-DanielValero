# Reto 11 - Mocks con IA

## Propósito

Mostrar tres estados del panel del operador definidos en el HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 11 de Chimchar. Los SVG se construyeron con asistencia de IA/Codex a partir del [prompt completo](prompt-utilizado.md), usando texto y figuras vectoriales editables.

## Identidad reutilizada

Se leyeron el [manual del Reto 8](../reto8-ux/manual-identidad.md), su [panel base](../reto8-ux/panel-flota.svg) y los datos de [SC-01](../reto7-dosw.md) antes de diseñar. Se mantienen paleta, tipografías locales, colores semánticos, cinco drones y composición de tarjetas. Los tres SVG comparten dimensiones de 1600 × 1200 y `viewBox="0 0 1600 1200"`.

## Tres estados

| Pantalla | Estados ilustrados | Mensaje y controles |
| --- | --- | --- |
| [Normal](panel-normal.svg) | D-01, D-03 y D-05 DISPONIBLE; D-02 EN_VUELO; D-04 EN_CARGA. | D-04 muestra 18%, batería crítica y asignación bloqueada; solo los tres disponibles tienen controles de selección/asignación habilitados visualmente. |
| [FALLO](panel-fallo.svg) | Mismos estados salvo D-04, que pasa a FALLO. | «D-04 presenta un fallo y no puede ser asignado»; su selección y asignación se muestran bloqueadas. |
| [Sin drones disponibles](panel-vacio.svg) | Los cinco EN_VUELO. | «No hay drones disponibles en este momento»; ninguna selección ni asignación está habilitada. |

Cada tarjeta conserva ID, batería y ubicación: D-01 85% Bloque A; D-02 42% Biblioteca; D-03 91% Bloque C; D-04 18% Bloque B; D-05 67% Bloque D. Los resúmenes se calculan para cada escenario.

EN_CARGA y FALLO son escenarios visuales del prototipo y no atributos añadidos al record `Drone`, cuyo `available` sigue siendo booleano. Frente al ejemplo del Reto 8, D-04 se muestra en carga en el escenario normal de este reto. En vacío se representa una instantánea hipotética de cinco drones ya en misión, incluida la batería crítica de D-04; no se propone asignarlo con 18% ni se modifica el modelo Java.

## Archivos

- [panel-normal.svg](panel-normal.svg)
- [panel-fallo.svg](panel-fallo.svg)
- [panel-vacio.svg](panel-vacio.svg)
- [prompt-utilizado.md](prompt-utilizado.md)
- [heuristicas-nielsen.md](heuristicas-nielsen.md)
- Este README.

## Heurísticas y validación

Se documentan las heurísticas #1, #3, #5, #8 y #9 con elementos concretos en [Heurísticas de Nielsen](heuristicas-nielsen.md). Son decisiones del prototipo, sin afirmar implementación completa ni conformidad WCAG.

Los mocks son estáticos, sin JavaScript, imágenes externas ni fuentes descargadas. Los tres pasaron validación XML, comprobaciones de dimensiones, cinco drones, datos, estados y acciones bloqueadas. Se renderizaron con Chrome a 1600 × 1200 y se revisaron las tres imágenes: títulos, etiquetas, avisos y controles visibles, sin contenido cortado.
