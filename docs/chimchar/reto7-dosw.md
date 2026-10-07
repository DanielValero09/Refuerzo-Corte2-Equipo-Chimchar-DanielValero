# Reto 7 - Plantilla DOSW

## Información general

| Campo | Valor |
| --- | --- |
| Código | SC-01 |
| Nombre | Registrar misión de reparto de documento |
| Actor principal | Operador de drones |

## Precondiciones

- Existe al menos un drone disponible.
- El drone que se seleccione debe tener batería mayor o igual al 30%; antes de iniciar debe existir al menos un candidato que cumpla este umbral.
- Los destinos del MVP están configurados: Bloque A, Bloque B, Bloque C, Bloque D y Biblioteca.

Estas condiciones permiten iniciar el caso de uso; las restricciones del dominio se vuelven a comprobar durante el registro.

## Datos de entrada

| Campo | Tipo | Obligatorio | Regla |
| --- | --- | --- | --- |
| droneAsignado.id | String | Sí | Formato D-XX, donde XX son dos dígitos; debe identificar un drone existente. |
| droneAsignado.modelo | String | Sí | DJI Mini 3. |
| droneAsignado.bateria | Integer | Sí | Valor entero de 0 a 100. |
| droneAsignado.disponible | Boolean | Sí | Debe ser true. |
| droneAsignado.ubicacion | String | Sí | Ubicación actual del drone. |
| origen | Enum(BLOQUE_A,BLOQUE_B,BLOQUE_C,BLOQUE_D,BIBLIOTECA) | Sí | Uno de los lugares configurados del MVP. |
| destino | Enum(BLOQUE_A,BLOQUE_B,BLOQUE_C,BLOQUE_D,BIBLIOTECA) | Sí | Uno de los lugares configurados del MVP. |
| tipoCarga | Enum(SOBRE,CARPETA) | Sí | Documento permitido por el alcance del MVP. |

Los atributos documentados del drone corresponden a `Drone.id`, `model`, `battery`, `available` y `ubication` del modelo Java existente. Los valores de origen y destino se representan en ese modelo como cadenas: BLOQUE_A → «Bloque A», BLOQUE_B → «Bloque B», BLOQUE_C → «Bloque C», BLOQUE_D → «Bloque D» y BIBLIOTECA → «Biblioteca».

Aunque `ChargeType` contiene `LIBRO` por compatibilidad del modelo existente, el MVP Chimchar documentado permite únicamente `SOBRE` y `CARPETA`.

## Datos de salida

| Campo | Tipo | Regla |
| --- | --- | --- |
| codigoMision | String | Formato M-XX, donde XX son dos dígitos; código único generado al registrar, por ejemplo M-01. |
| estado | Enum(PENDIENTE,EN_VUELO,ENTREGADA,FALLIDA) | Estado inicial PENDIENTE. |

En el modelo Java, `codigoMision` corresponde a `Mission.id` y el estado a `MissionState`. La generación del código es el comportamiento requerido por esta especificación, no una funcionalidad agregada en este bloque.

## Flujo básico

1. El operador ingresa origen, destino y tipo de carga.
2. El sistema muestra los drones disponibles con batería suficiente.
3. El operador selecciona manualmente un drone.
4. El sistema valida disponibilidad, batería, destino y tipo de carga.
5. El sistema registra la misión, genera el código y la deja en estado PENDIENTE.

## Flujos alternos

### A1 — Batería insuficiente

Condición: en la validación del paso 4, `droneAsignado.bateria < 30%`.

Resultado: el sistema rechaza la asignación, no registra la misión y solicita seleccionar otro drone en el paso 3.

### A2 — Destino inválido

Condición: en la validación del paso 4, el destino está fuera de Bloque A, Bloque B, Bloque C, Bloque D y Biblioteca.

Resultado: el sistema rechaza el registro y solicita un destino válido en el paso 1.

## Reglas de negocio

- **RN-01:** Un drone debe tener batería >=30% para ser asignado.
- **RN-02:** Un drone no puede tener más de una misión activa simultáneamente; se consideran activas las misiones PENDIENTE con drone asignado y EN_VUELO.

## Fuente

HTML oficial de SkyCampus, `DOSW_Equipo_Chimchar_fixed.html`: enunciado Chimchar y apartado 07, «Plantilla DOSW»; los campos y restricciones concretan las instrucciones de este bloque.
