# Monferno Reto 6 - RF, RNF y MoSCoW

Alcance: especificación v2 según el HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 06, precisada por este bloque. Las políticas de urgencia, clima, reparación e integraciones describen comportamiento requerido; no se afirma que ya estén implementadas. Las métricas RNF son criterios de aceptación por verificar, no resultados de rendimiento medidos.

## Requerimientos funcionales

### RF-07 — Asignación automática estándar

Para una solicitud NORMAL o BAJO supervisada por el Operador de drones, SkyCampus selecciona automáticamente un drone disponible, en estado DISPONIBLE, con batería >=30% y capacidad compatible; la política estándar prioriza la mayor batería. Resultado observable: la misión contiene el ID del candidato seleccionado o permanece PENDIENTE con una causa si no existe uno apto.

### RF-08 — Prioridad URGENTE

Para una solicitud URGENTE, SkyCampus aplica prioridad absoluta frente a solicitudes NORMAL o BAJO y selecciona el drone compatible más rápido disponible: EXPRESS si soporta el peso, está disponible, en estado DISPONIBLE y tiene batería >=30%. Resultado observable: se identifica el candidato elegido por rapidez aunque otro apto tenga mayor batería. Si no hay EXPRESS apto, se considera MINI y luego CARGO compatible, respetando todas las restricciones; si no existe candidato, la misión sigue PENDIENTE. Este orden usa las características oficiales: EXPRESS rápido, MINI ágil y CARGO lento, sin inventar velocidades numéricas.

### RF-09 — Gestión de fallo y técnico

Cuando un drone entra en FALLO, SkyCampus notifica el cambio y permite al Técnico de mantenimiento consultar sus datos, diagnosticarlo y registrar su reparación. Resultado observable: el cambio y el registro de reparación son consultables; el drone permanece excluido de asignación mientras esté en FALLO y solo puede volver a ser apto cuando su estado y disponibilidad lo permitan.

### RF-10 — Consulta meteorológica

Antes de asignar e iniciar una misión solicitada, SkyCampus consulta la API Meteorológica para viento y lluvia. Resultado observable: si el clima es adverso, bloquea asignación/inicio del vuelo e informa la causa al operador. La ausencia de respuesta apta también bloquea el vuelo según RNF-06; no se inventan umbrales meteorológicos no definidos por el material.

## Tensión RF-07 / RF-08

RF-07 favorece mayor batería; RF-08 favorece rapidez para URGENTE. Son una prioridad de reglas:

1. Si prioridad == URGENTE, aplicar primero la política de rapidez y preferir EXPRESS compatible; una batería mayor en otro tipo no desplaza a un EXPRESS apto.
2. Si prioridad == NORMAL o BAJO, aplicar la estrategia normal, por ejemplo mayor batería.
3. Nunca ignorar batería >=30%, disponibilidad, estado DISPONIBLE, capacidad compatible, prohibición de CARGO para menos de 100 g y clima apto.

La urgencia no permite asignar un drone inseguro ni despegar sin condiciones aptas; ante ausencia de candidatos, se aplica el flujo alterno de SC-07.

## Requerimientos no funcionales

### RNF-04 — Rendimiento de asignación

El algoritmo debe seleccionar el drone en menos de 500 ms para una flota de hasta 50 drones, medido con JUnit 5 `assertTimeout`. Prioridad: MUST. La medición local debe aislar la selección de esperas de red.

### RNF-05 — Notificaciones de estado

El cambio de estado debe notificarse a los observadores registrados en menos de 500 ms para hasta 10 observadores locales. Prioridad: MUST. Se mide desde el cambio hasta completar las notificaciones locales.

### RNF-06 — Respuesta meteorológica

Si la consulta meteorológica no responde dentro de 2 segundos, SkyCampus debe aplicar comportamiento fail-safe y bloquear el vuelo. Prioridad: MUST. La verificación futura debe simular el timeout y comprobar que no se autoriza el inicio; no se implementa integración real de red en este bloque.

### RNF-07 — Visualización de flota

El panel debe representar los 20 drones y sus estados sin superar 1 segundo de actualización en el escenario de prueba local. Prioridad: SHOULD. Se mide desde recibir el estado hasta completar su representación; el SVG estático de este bloque no demuestra esa latencia.

## MoSCoW

| Código | Tipo | Requerimiento | MoSCoW | Justificación |
| --- | --- | --- | --- | --- |
| RF-07 | RF | Asignación automática estándar | MUST | La selección automática es el cambio central de la v2. |
| RF-08 | RF | Prioridad URGENTE | MUST | La urgencia requiere una política explícita que prevalezca sobre mayor batería. |
| RF-09 | RF | Gestión de fallo y técnico | MUST | Un recurso averiado debe quedar bloqueado y disponer de atención técnica trazable. |
| RF-10 | RF | Consulta meteorológica | MUST | El vuelo necesita condiciones aptas antes de comenzar. |
| RNF-04 | RNF | Selección <500 ms con hasta 50 drones | MUST | El operador necesita una decisión oportuna con la flota prevista. |
| RNF-05 | RNF | Notificación <500 ms a hasta 10 observadores | MUST | Panel y registros deben conocer oportunamente los cambios operativos. |
| RNF-06 | RNF | Timeout meteorológico de 2 s y bloqueo fail-safe | MUST | La falta de datos de clima no debe convertirse en permiso de vuelo. |
| RNF-07 | RNF | Panel de 20 drones actualizado <=1 s | SHOULD | Una vista ágil facilita supervisión sin sustituir las restricciones de seguridad. |
