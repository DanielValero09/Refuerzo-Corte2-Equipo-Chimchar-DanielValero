# SC-07 — Asignar automáticamente drone a misión

## Identificación

- ID: SC-07.
- Nombre: Asignar automáticamente drone a misión.
- Actor principal: Operador.
- Actores/sistemas secundarios: API Meteorológica, Control Aéreo ECI, PanelOperador, SistemaLog.

API Meteorológica y Control Aéreo ECI son sistemas externos; PanelOperador y SistemaLog son participantes internos de notificación, no sistemas externos adicionales del C4. El contrato de Control Aéreo comprende registro de vuelo/autorización de ruta antes del inicio; no añade un paso al flujo de asignación ni supone una implementación de red en este bloque.

## Precondiciones

- Existe una solicitud de misión identificable y PENDIENTE.
- Están configurados los destinos permitidos y la política aplicable a la prioridad.
- La flota y sus estados pueden consultarse; su falta de candidatos aptos se trata en FA-01.
- El operador está supervisando la operación.

## Datos de entrada y subobjetos calculados

| Campo | Tipo | Obligatorio | Regla / dirección |
| --- | --- | --- | --- |
| paquete | Objeto/contenedor | Sí | Entrada que agrupa peso, tipo y prioridad |
| paquete.peso | Integer | Sí | Entrada: 1–2000 gramos; un valor superior activa FA-03 |
| paquete.tipo | Enum(SOBRE,CARPETA,LIBRO,EQUIPO) | Sí | Entrada: uno de los tipos especificados |
| paquete.prioridad | Enum(URGENTE,NORMAL,BAJO) | Sí | Entrada: define la prioridad de reglas RF-07/RF-08 |
| origen | Enum(BLOQUE_A,BLOQUE_B,BLOQUE_C,BLOQUE_D,BIBLIOTECA) | Sí | Entrada: punto de salida configurado |
| destino | Enum(BLOQUE_A,BLOQUE_B,BLOQUE_C,BLOQUE_D,BIBLIOTECA) | Sí | Entrada: punto de entrega permitido |
| droneAsignado | Objeto/contenedor Drone | No, salida | Calculado por el sistema, nunca selección manual del operador |
| droneAsignado.id | String | No, salida | Formato D-XX |
| droneAsignado.tipo | TipoDrone | No, salida | MINI, EXPRESS o CARGO |
| droneAsignado.bateria | Integer | No, salida | 0–100%; asignación válida requiere >=30% |

Los subobjetos describen el contrato funcional, no nuevas clases Java implementadas aquí. El modelo v2 existente recibe el peso y prioridad directamente; esta especificación tampoco agrega ubicación/sector al record Drone.

## Flujo básico

1. **Operador / SkyCampus:** recibir la solicitud con origen, destino, paquete y prioridad, dejándola identificada para procesarla.
2. **SkyCampus / API Meteorológica:** consultar viento y lluvia; continuar solo con condiciones aptas y respuesta dentro del límite RNF-06.
3. **SkyCampus:** filtrar drones aptos por disponibilidad, estado DISPONIBLE, batería y capacidad.
4. **SkyCampus:** aplicar la estrategia de selección; URGENTE prioriza rapidez según RF-08, NORMAL/BAJO siguen RF-07.
5. **SkyCampus:** validar nuevamente batería, capacidad y disponibilidad del candidato antes de confirmar la asociación.
6. **SkyCampus:** asignar el drone a la misión y devolver la asociación, conservando el estado PENDIENTE hasta el inicio del vuelo.
7. **SkyCampus / observadores:** notificar la asignación/estado al PanelOperador y SistemaLog para actualizar la supervisión y el registro.

## Flujos alternos

### FA-01 — Sin drones disponibles/aptos

Condición: el filtrado o la validación final no deja candidatos aptos. Resultado: la misión permanece PENDIENTE y se informa al operador que no hay drone asignable.

### FA-02 — Condiciones meteorológicas adversas

Condición: viento/lluvia adversos o ausencia de respuesta apta dentro de 2 segundos. Resultado: bloquear asignación/inicio del vuelo e informar la causa; un timeout se comunica como falta de condiciones verificables, no como clima favorable.

### FA-03 — Paquete supera capacidad máxima

Condición: peso >2000 g. Resultado: rechazar asignación e informar el peso máximo permitido de 2000 g.

## Reglas de negocio

- **RN-01:** batería mínima 30% para asignar un drone.
- **RN-02:** MINI admite máximo 500 g.
- **RN-03:** EXPRESS admite máximo 800 g.
- **RN-04:** CARGO admite máximo 2000 g.
- **RN-05:** CARGO no puede usarse para paquetes <100 g.
- **RN-06:** una misión URGENTE aplica la prioridad de rapidez de RF-08 antes que mayor batería.
- **RN-07:** no iniciar vuelo si el clima es adverso; sin datos aptos se bloquea de forma fail-safe.

## Salidas

| Campo | Tipo | Resultado |
| --- | --- | --- |
| codigoMision | String | ID de misión; ejemplo documental M-V2-007 |
| droneAsignado.id | String | Drone asociado, formato D-XX, o sin asociación si se rechaza |
| droneAsignado.tipo | TipoDrone | Tipo del drone seleccionado |
| droneAsignado.bateria | Integer | Batería usada en la validación |
| estado | Enum(PENDIENTE,EN_VUELO,ENTREGADA,FALLIDA) | PENDIENTE tras asignar, antes de iniciar vuelo |
| motivoRechazo | String opcional | Causa explícita en FA-01, FA-02 o FA-03; ausente en asignación válida |

## Trazabilidad

| Requisito | Parte de SC-07 |
| --- | --- |
| RF-07 | Filtrado, selección estándar y asociación: pasos 3–6 |
| RF-08 | Prioridad de rapidez: paso 4 y RN-06 |
| RF-10 | Consulta y bloqueo meteorológico: paso 2, FA-02 y RN-07 |
| RNF-04 | Límite de 500 ms para la selección del paso 4 |
| RNF-06 | Timeout de 2 segundos y bloqueo fail-safe: paso 2 y FA-02 |

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, Monferno apartado 07, y precisiones de este bloque. [Requerimientos relacionados](reto6-requerimientos.md).
