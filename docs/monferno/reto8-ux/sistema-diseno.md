# Sistema de diseño SkyCampus v2

## Identidad reutilizada

Valores copiados del [manual Chimchar](../../chimchar/reto8-ux/manual-identidad.md), leído antes del diseño:

| Token | Valor |
| --- | --- |
| Primario | #0B4F6C |
| Secundario | #0F766E |
| Fondo | #F4F7FA |
| Texto principal | #142D3A |
| Texto secundario | #526576 |
| Superficie | #FFFFFF |
| Borde | #CBD5E1 |
| Tipografía UI | Arial / Helvetica / sans-serif |
| Tipografía IDs | Consolas / Courier New / monospace |

Los estados usan los colores semánticos Chimchar y añaden gris para mantenimiento. Texto, símbolos y mensajes acompañan al color; no se afirma cumplimiento WCAG sin una evaluación completa.

## 1. Tarjeta de drone

Contenido constante: ID, tipo, porcentaje de batería, estado textual con símbolo y ubicación/sector representativo. La ubicación del prototipo no se agrega al modelo Java en este bloque.

| Estado | Texto/símbolo | Texto/icono | Fondo | Acción |
| --- | --- | --- | --- | --- |
| DISPONIBLE | ✓ Disponible | #166534 | #DCFCE7 | Asignación solo si batería, carga y clima son aptos |
| EN_VUELO | ↑ En vuelo | #1E40AF | #DBEAFE | Supervisar; nueva asignación bloqueada |
| EN_CARGA | + En carga | #854D0E | #FEF3C7 | Esperar retorno a DISPONIBLE; asignación bloqueada |
| FALLO | ! Fallo | #991B1B | #FEE2E2 | Mostrar causa y acceso al técnico; asignación bloqueada |
| MANTENIMIENTO | × Mantenimiento | #475569 | #E2E8F0 | Mostrar atención técnica; asignación bloqueada |

ATERRIZANDO permanece en el enum y tampoco es asignable; las cinco variantes visuales corresponden exactamente al componente exigido por el reto. La batería no cambia por sí sola el estado del drone.

[Composición con cinco tarjetas](tarjeta-drone-estados.svg).

## 2. Botón «Asignar misión»

| Estado | Representación | Comportamiento especificado |
| --- | --- | --- |
| Default | Primario #0B4F6C y texto blanco | Acción principal visible |
| Hover | Primario oscurecido #073D54 y contorno | Indicar que el control admite interacción |
| Procesando | Indicador circular estático y «Asignando…» | Bloquear envíos repetidos mientras se valida |
| Éxito | Verde semántico y «✓ Misión asignada» | Confirmar el resultado con texto |
| Deshabilitado | Gris y «Asignar misión — bloqueado» | Explicar falta de candidato o condición no apta |

El SVG muestra estados de diseño, sin ejecutar peticiones ni animaciones. El indicador de proceso es un spinner estático vectorial, no JavaScript.

[Cinco estados del botón](boton-asignar-estados.svg).

## 3. Indicador de batería

| Rango | Estado | Información adicional |
| --- | --- | --- |
| >=60% | Normal / positivo | Porcentaje y texto «Nivel normal» |
| 30–59% | Advertencia | Porcentaje y texto «Revisar autonomía»; cumple el mínimo de batería |
| <30% | Crítico / no asignable | Porcentaje, símbolo ! y texto «No asignable» |

Cumplir >=30% no garantiza aptitud completa: también se valida disponibilidad, estado, capacidad y clima. La barra complementa el número, no lo sustituye.

[Tres rangos de batería](indicador-bateria.svg).

## Flujo de asignación: tres pantallas

1. **Panel de flota:** resumen de 20 drones, agrupación MINI/CARGO/EXPRESS, estados agregados, filtros simples, alertas y acción «Nueva misión». La acción crítica «Cancelar misión» permanece visible fuera de menús.
2. **Detalle / formulario:** origen, destino, peso, tipo y prioridad; muestra candidatos aptos y clima resumido. Para URGENTE, el ejemplo de 300 g propone EXPRESS aunque MINI tenga más batería, conforme a RF-08.
3. **Confirmación:** ID del drone, tipo, batería, prioridad, ruta estimada y ETA; acciones Confirmar / Cancelar. La misión sigue PENDIENTE hasta su inicio; confirmar no representa autorización real de vuelo.

[Panel → detalle → confirmación](flujo-asignacion.svg). Sus valores son ilustrativos, no resultados de API meteorológica ni cálculo de rutas/ETA implementado. La autorización del Control Aéreo permanece como contrato externo del C4.

## Ley de Fitts

«Nueva misión», «Asignar misión» y «Confirmar» tienen áreas visibles y amplias en la zona de acciones. «Cancelar misión» aparece directamente en el panel y «Cancelar» junto a la confirmación, para acceso rápido ante una emergencia, sin esconderse en un menú. Estas decisiones buscan facilitar alcance y control; no se atribuyen tiempos, distancias físicas ni tasas de éxito no medidas. La operación real de cancelación en vuelo no se implementa con el prototipo.

## Ley de Hick

Los 20 drones se agrupan por tipo y se resumen por estado, en lugar de mostrar una lista plana de 20 tarjetas. Filtros sencillos por estado/tipo y la lista reducida de candidatos aptos disminuyen decisiones simultáneas. El operador revisa una propuesta automática en vez de comparar manualmente toda la flota.

## Regla de Miller

Máximo siete notificaciones visibles sin confirmar en el panel de alertas; el ejemplo muestra dos y declara el límite. Al llegar a siete, las adicionales se agrupan con contador y acceso a revisión, sin perder registros ni ocultar el aviso de fallo prioritario. Es una decisión de diseño, todavía sin implementar un panel dinámico.

## Alcance y verificación

No se descargan fuentes ni se usan imágenes externas, raster incrustado o scripts. Cada SVG tiene viewBox, dimensiones explícitas y texto vectorial editable. La validación XML y la revisión visual se registran en el README Monferno; no prueban métricas RNF de una aplicación interactiva.
