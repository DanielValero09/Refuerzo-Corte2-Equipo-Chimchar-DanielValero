# Heurísticas de Nielsen — Monferno

Se verifican nueve heurísticas sobre los elementos visibles del prototipo; no se atribuye funcionalidad interactiva ni resultados de usuarios que no se hayan medido.

| Número | Heurística | Pantalla concreta | Elemento y cumplimiento visual |
| --- | --- | --- | --- |
| 1 | Visibilidad del estado del sistema | 01-panel-flota; 02-formulario-mision | Resumen 20 drones, conteos por estado, dos alertas y bloque «Clima apto en el ejemplo» informan el estado antes de continuar. |
| 2 | Correspondencia sistema-mundo real | 02-formulario-mision; error-paquete-pesado | «Peso del paquete», gramos y máximo 2000 g emplean unidades y términos del reparto. |
| 3 | Control y libertad del usuario | 03-confirmacion; los tres errores | «Cancelar», «Volver» y acciones de recuperación permiten revisar o abandonar la propuesta; no se obliga a confirmar. |
| 4 | Consistencia y estándares | Las seis pantallas | Marca, encabezado, tipografías, paleta semántica y botones primarios se mantienen; el aviso de prototipo aparece en todas. |
| 5 | Prevención de errores | 02-formulario-mision; error-clima-adverso | Se muestran batería >=30%, capacidad, CARGO <100 g y urgencia; el error meteorológico bloquea el flujo sin confirmar vuelo. |
| 6 | Reconocer mejor que recordar | 02-formulario-mision; 03-confirmacion | Candidatos con ID/tipo/batería y confirmación con paquete/prioridad/ruta evitan recordar datos de pantallas anteriores. |
| 7 | Flexibilidad y eficiencia | 01-panel-flota; 02-formulario-mision | Filtros Estado/Tipo, agrupación de flota y lista corta de candidatos aptos reducen comparación manual y navegación. |
| 8 | Diseño estético y minimalista | 01-panel-flota; 03-confirmacion | Tres grupos reemplazan 20 tarjetas grandes; la confirmación concentra únicamente recurso, paquete, prioridad, ruta/ETA ilustrativos y decisión. |
| 9 | Reconocer, diagnosticar y recuperarse de errores | error-sin-drones; error-clima-adverso; error-paquete-pesado | Cada aviso nombra causa y efecto y ofrece reintentar, volver o corregir; el peso muestra 2300 g frente a 2000 g, sin depender solo del rojo. |

Archivos y [navegación del flujo](README.md). Revisión visual mediante Chrome headless; los controles son estáticos y las heurísticas 3/7 describen posibilidades del diseño, no acciones ejecutables en los SVG.
