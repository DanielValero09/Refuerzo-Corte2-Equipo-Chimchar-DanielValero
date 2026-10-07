# Reto 3 - Patrones de Diseño

## Problema 1

Patrón: Builder

Justificación: Permite construir una misión con datos obligatorios y opcionales mediante una configuración legible.

Clases implementadas: `MissionBuilder`, `Mission`.

El Builder exige drone, origen y destino, conserva el ID y el tipo de carga del modelo y establece el estado inicial `PENDIENTE`. La misión almacena realmente prioridad (default 3), notas y horaMaximaEntrega (ambas vacías por defecto). El constructor anterior de seis argumentos de `Mission` sigue disponible con esos mismos valores por defecto.

## Problema 2

Patrón: Chain of Responsibility

Justificación: Separa las validaciones y permite encadenarlas para rechazar la misión en el primer incumplimiento.

Clases implementadas: `Validador`, `ValidadorBateria`, `ValidadorDestino`, `ValidadorCarga`.

Cadena utilizada: `ValidadorBateria -> ValidadorDestino -> ValidadorCarga`.

- Batería: rechaza valores menores al 30%.
- Destino: permite únicamente Bloque A, Bloque B, Bloque C, Bloque D y Biblioteca.
- Carga: permite `SOBRE` y `CARPETA`; rechaza `LIBRO` y `null`.

El MVP Chimchar no modela capacidad en gramos; la capacidad se representa mediante los tipos de carga permitidos por el alcance del MVP.

## Problema 3

Patrón: Strategy

Justificación: Permite intercambiar el criterio de asignación mediante una interfaz sin modificar el asignador.

Clases implementadas: `EstrategiaAsignacion` (interfaz), `AsignacionMayorBateria`, `AsignadorDrone`.

La estrategia filtra drones disponibles con batería mayor o igual al 30% y selecciona el de mayor batería; devuelve un `Optional` vacío si no hay candidatos. En la flota de ejemplo selecciona `D-03` con 91%.
