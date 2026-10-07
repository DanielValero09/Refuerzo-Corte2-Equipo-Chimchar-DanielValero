# Reto 11 - Heurísticas de Nielsen

Decisiones de los tres mocks estáticos, sin afirmar que la aplicación ya implemente estos comportamientos. La batería y la ubicación conservan los datos base; los estados ilustran escenarios independientes.

| Heurística | Panel normal | Panel FALLO | Panel sin disponibles |
| --- | --- | --- | --- |
| #1 Visibilidad del estado del sistema | Cinco tarjetas con ID, porcentaje y barra de batería, estado y ubicación; resumen 3 disponibles, 1 en vuelo y 1 en carga. | El resumen informa 1 en fallo y la tarjeta D-04 muestra FALLO con borde y mensaje explícitos. | El resumen muestra 0 disponibles y 5 en vuelo; la flota sigue visible junto al mensaje de indisponibilidad. |
| #3 Control y libertad del usuario | Seleccionar drone y Asignar a misión se presentan como decisiones manuales, con Ver detalle antes de decidir. | Se mantienen los controles manuales de los otros drones y Ver detalle para revisar D-04 sin asignarlo. | Ver detalle permanece visible; la guía explica la selección manual y no elige otro recurso automáticamente. |
| #5 Prevención de errores | D-04 tiene 18%, EN_CARGA, aviso de batería crítica y selección/asignación bloqueadas; D-02 en vuelo tampoco puede asignarse. | D-04 FALLO no puede seleccionarse ni asignarse; el bloqueo se expresa con texto, además del estilo de botones. | Todos los controles Seleccionar drone y Asignar a misión se muestran deshabilitados porque los cinco están en misión. |
| #8 Diseño estético y minimalista | La misma jerarquía y cuadrícula del Reto 8 muestran únicamente ID, modelo, batería, estado, ubicación y acciones del MVP. | Se conserva la composición y se modifica solo la información necesaria para hacer visible el fallo. | Se conserva la cuadrícula; el mensaje de estado vacío describe la ausencia de recursos asignables sin ocultar los datos esenciales. |
| #9 Reconocer, diagnosticar y recuperarse de errores | El aviso nombra D-04, muestra 18% y el mínimo 30%, y propone seleccionar un drone disponible con batería suficiente. | «D-04 presenta un fallo y no puede ser asignado» identifica el recurso; el mensaje propone otro drone y revisar D-04 desde Ver detalle, sin inventar la causa técnica del fallo. | «No hay drones disponibles en este momento» explica el bloqueo; el texto informa que debe volver a estar disponible un drone antes de asignar. |

Las etiquetas DISPONIBLE, EN_VUELO, EN_CARGA y FALLO y los mensajes permiten reconocer el estado sin depender únicamente del color. Los botones son representaciones gráficas; no hay JavaScript, navegación ni operaciones reales.
