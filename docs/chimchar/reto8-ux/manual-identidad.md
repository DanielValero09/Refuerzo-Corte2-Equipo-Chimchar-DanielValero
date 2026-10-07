# Reto 8 - Manual de Identidad y UX/UI

## Alcance del prototipo

Identidad definida antes de construir el [panel de flota](panel-flota.svg). El SVG es un mock estático con texto vectorial y los cinco drones del MVP; sus controles muestran decisiones de diseño, sin ejecutar operaciones.

## Paleta

| Uso | HEX | Decisión |
| --- | --- | --- |
| Color primario | #0B4F6C | Azul profundo para marca y acciones principales, asociado a una interfaz técnica y sobria. |
| Color secundario | #0F766E | Verde azulado para acentos y aptitud de asignación. |
| Fondo | #F4F7FA | Fondo claro que separa el contenido de las tarjetas. |
| Superficie | #FFFFFF | Fondo de tarjetas y controles. |
| Texto principal | #142D3A | Texto oscuro para títulos y datos. |
| Texto secundario | #526576 | Etiquetas y explicaciones legibles sobre superficies claras. |
| Bordes | #CBD5E1 | Delimitación suave de tarjetas y controles. |

| Estado | Texto/icono | Fondo | Uso |
| --- | --- | --- | --- |
| DISPONIBLE | #166534 | #DCFCE7 | Drone disponible, identificado con etiqueta explícita. |
| EN_VUELO | #1E40AF | #DBEAFE | Representación visual del drone no disponible. |
| EN_CARGA | #854D0E | #FEF3C7 | Estilo reservado para este estado, sin adjudicarlo a los datos actuales. |
| FALLO | #991B1B | #FEE2E2 | Estilo para mensajes de fallo con causa y acción de recuperación. |

La batería crítica usa texto y aviso rojo (#991B1B sobre #FEE2E2), además del porcentaje; no cambia por sí sola el estado de disponibilidad del drone. El diseño utiliza texto oscuro sobre fondos claros y blanco sobre el primario; no se afirma conformidad WCAG de la aplicación.

## Tipografías

- Interfaz: `Arial, Helvetica, sans-serif`, pesos 400 y 700.
- IDs técnicos: `Consolas, Courier New, monospace`.
- Jerarquía: título de panel 36 px, ID 24 px, datos 18 px y etiquetas auxiliares 14–16 px.
- El SVG utiliza fuentes locales de respaldo; no descarga fuentes, imágenes ni scripts.

## Estados y datos del MVP

El modelo Java tiene `available` booleano: `true` se representa como DISPONIBLE y `false` como EN_VUELO **solo como convención visual del MVP**; el booleano no demuestra que un drone esté físicamente volando. EN_CARGA y FALLO quedan definidos como estilos, sin inventar estados actuales ni telemetría.

| Drone | Modelo | Batería | Disponibilidad real del ejemplo | Estado visual | Ubicación |
| --- | --- | --- | --- | --- | --- |
| D-01 | DJI Mini 3 | 85% | Disponible (`true`) | DISPONIBLE | Bloque A |
| D-02 | DJI Mini 3 | 42% | No disponible (`false`) | EN_VUELO | Biblioteca |
| D-03 | DJI Mini 3 | 91% | Disponible (`true`) | DISPONIBLE | Bloque C |
| D-04 | DJI Mini 3 | 18% | Disponible (`true`) | DISPONIBLE | Bloque B |
| D-05 | DJI Mini 3 | 67% | Disponible (`true`) | DISPONIBLE | Bloque D |

Los totales del encabezado se derivan de estos datos: cinco drones, cuatro disponibles y tres aptos para asignar por disponibilidad y batería >=30%. D-04 sigue disponible, pero no es apto para asignación; D-02 tampoco puede seleccionarse. Ninguna elección se realiza automáticamente.

## Heurísticas de Nielsen aplicadas al mock

| Número | Heurística | Decisión visible en el prototipo |
| --- | --- | --- |
| 1 | Visibilidad del estado del sistema | Cada tarjeta muestra ID, modelo, porcentaje y barra de batería, etiqueta de estado, disponibilidad y ubicación sin abrir otra pantalla. |
| 3 | Control y libertad del usuario | Las acciones «Seleccionar drone» son manuales; la guía del operador identifica «Cancelar misión pendiente» y explica que se ofrece únicamente si estado = PENDIENTE. |
| 5 | Prevención de errores | D-04 muestra 18%, aviso de batería crítica y selección bloqueada antes de asignar; D-02 presenta «No disponible» y selección bloqueada. |
| 8 | Diseño estético y minimalista | Las cinco tarjetas repiten la misma estructura y muestran únicamente los datos esenciales y la acción de selección del MVP. |
| 9 | Ayudar a reconocer, diagnosticar y recuperarse de errores | El aviso nombra D-04, explica que 18% es inferior al mínimo del 30% y solicita seleccionar otro drone disponible; no depende únicamente del color. |

Estas son decisiones del prototipo, no una afirmación de que la aplicación completa ya implemente las heurísticas. Para un futuro mensaje con estado FALLO, el patrón debe mostrar causa y recuperación, por ejemplo: «FALLO: No se pudo registrar la misión; el drone ya no está disponible. Seleccione otro drone y vuelva a confirmar». Es un ejemplo de redacción, no un fallo real de la flota mostrada.

## Tono de voz

Mensajes breves, específicos y profesionales: identificar el recurso, explicar la causa y proponer una acción manual para recuperarse.

## Fuente

HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, enunciado Chimchar y apartado 08, «Manual de Identidad y UX/UI»; el mapeo booleano y los datos exactos siguen las instrucciones de este bloque.
