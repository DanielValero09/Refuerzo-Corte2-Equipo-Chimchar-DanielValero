# Flujo interactivo y recuperación

1. Dashboard: muestra ECI, UNAL, UNIANDES y EAFIT con 25 drones sintéticos por sede, agrupados por estado.
2. Nueva misión: solicita origen, destino, peso entero positivo y prioridad; conserva valores al volver.
3. Planificar: muestra carga local de 350 ms para representar el estado procesando, sin afirmar tiempo real de red.
4. Ruta: muestra dos etapas de ejemplo, la estación C-SIM, autorización simulada y candidato compatible.
5. Revisión: muestra tipo, batería, prioridad, etapas y ETA ilustrativa antes de confirmar.
6. Confirmación: cambia a EN_VUELO en la simulación, actualiza disponibilidad y registra un evento local.

El botón Cancelar planificación vuelve al dashboard sin iniciar una misión. Volver a etapas o corregir conserva la solicitud. Cambiar un escenario invalida el plan anterior y obliga a planificar de nuevo. El tema ECI/UNAL modifica variables CSS, sin duplicar vistas.

| Escenario | Causa visible | Confirmación | Recuperación |
|---|---|---|---|
| Clima adverso | Viento/lluvia no aptos en la simulación. | Bloqueada | Volver y reintentar con condiciones aptas. |
| Sin drones aptos | No existen candidatos en el escenario. | Bloqueada | Volver y reintentar más tarde. |
| Paquete pesado | Peso mayor a 2000 g; se muestra el máximo. | Bloqueada | Corregir peso. |
| Aerocivil rechaza | Autorización simulada denegada. | Bloqueada | Reintentar con autorización verificable. |
| Sede inactiva | Destino no operable en el escenario. | Bloqueada | Corregir sede o reintentar con estado activo. |
| Estación cerrada | La alternativa requiere C-SIM no disponible. | Bloqueada | Replanificar cuando la estación esté disponible. |

Los controles de demostración provocan y restablecen estas condiciones. No envían datos fuera del equipo. Las distancias/ETA ilustrativas no son planificación aeronáutica real.
