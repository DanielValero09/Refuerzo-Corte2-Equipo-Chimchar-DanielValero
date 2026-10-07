# Reto 9 - Agilismo y Jira

Contenido preparado para cargar en Jira. En este bloque no se accedió a Jira, no se crearon tickets en una instancia real y no se adjunta ninguna captura.

## Épica

**Título:** Digitalizar el reparto interno de la ECI mediante una flota de drones supervisada

**Descripción:** Digitalizar el reparto interno de documentos de la ECI mediante cinco drones DJI Mini 3 supervisados manualmente por un operador, con destinos fijos y transporte de sobres y carpetas.

## Feature

**Título:** Gestión de flota

**Descripción:** Permitir al operador consultar la flota y administrar su participación en las misiones del MVP.

**Jerarquía prevista:** Épica → Feature Gestión de flota → historias de usuario → subtareas de HU-02.

## Historias de usuario

### HU-01 — Ver flota

Como operador de drones,
quiero consultar el estado de la flota,
para seleccionar un drone adecuado para una misión.

### HU-02 — Asignar drone a misión

Como operador de drones,
quiero asignar manualmente un drone disponible a una misión,
para iniciar el reparto con un recurso válido.

### HU-03 — Cancelar misión pendiente

Como operador de drones,
quiero cancelar una misión que aún está en estado PENDIENTE,
para evitar que se ejecute una entrega que ya no es necesaria.

## Subtareas de HU-02

| Código | Subtarea técnica |
| --- | --- |
| ST-01 | Validar disponibilidad y batería del drone. |
| ST-02 | Asociar el drone seleccionado a la misión. |
| ST-03 | Actualizar y mostrar el estado de la asignación. |

## Criterios de aceptación de HU-02

### CA-01

Dado un drone disponible con batería >=30%,
cuando el operador lo asigna a una misión PENDIENTE,
entonces el sistema registra la asociación correctamente.

### CA-02

Dado un drone no disponible o con batería <30%,
cuando el operador intenta asignarlo,
entonces el sistema rechaza la asignación e informa la causa.

## Evidencia real de Jira

**Pendiente de captura real en Jira.**

La evidencia debe mostrar:

- La épica.
- La feature Gestión de flota y su relación con la épica.
- Las tres historias de usuario.
- La HU Asignar drone a misión con sus tres subtareas.
- Sus dos criterios de aceptación.

Las claves HU/ST/CA de este documento son identificadores de planificación, no claves de tickets creados en Jira. El documento no certifica que las historias estén implementadas.

Instrucciones rápidas y ubicación prevista de la evidencia: [README de Jira](reto9-jira/README.md).

## Fuente

HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 09, «Agilismo y Jira»; textos, cantidades y estado pendiente de la evidencia siguen las instrucciones de este bloque.
