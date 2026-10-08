# Reto 10 - Casos de Uso Enterprise

Diagrama UML de especificación organizado en exactamente cinco paquetes. Describe funciones objetivo: no acredita que todas estén operativas en el Java local ni que exista comunicación externa real.

## Actores y herencia

Operador, Coordinador de sede, Superadministrador, Solicitante, Admin y Técnico de mantenimiento. Coordinador hereda de Operador; Superadministrador hereda de Coordinador. El triángulo hueco apunta al padre. Las asociaciones heredadas no se duplican; las específicas del Coordinador y Superadministrador complementan lo heredado. Admin gestiona configuración/usuarios; Técnico participa en mantenimiento; Solicitante registra solicitudes y consulta su misión.

## Paquetes y casos de uso

### Gestión de Misiones

- Ver solicitudes pendientes
- Crear misión
- Cancelar misión
- Ver historial
- Consultar misión
- Registrar solicitud de reparto

### Gestión de Flota

- Ver estado de drones
- CU-E07 — Asignar drone de flota compartida
- CU-E06 — Transferir drone entre sedes
- CU-E01 — Consultar analytics por sede
- Validar drone apto

### Rutas y Navegación

- Calcular ruta simple
- CU-E03 — Planificar ruta multi-etapa
- CU-E04 — Autorizar ruta con Aerocivil
- CU-E05 — Gestionar estación de carga
- Validar estaciones de carga

### Mantenimiento

- Diagnosticar fallo
- Marcar en mantenimiento
- Aprobar retorno a servicio

### Administración

- Configurar sede
- Gestionar usuarios
- CU-E08 — Ver dashboard Enterprise
- Generar reportes
- CU-E02 — Configurar radio máximo de vuelo
- Configurar destinos / reglas operativas

Los ocho códigos CU-E01 a CU-E08 mantienen sus nombres de la [matriz de trazabilidad](../reto6-trazabilidad.md). «Consultar analytics por sede» representa Ver analytics; «Asignar drone de flota compartida» representa Asignar drone; «Planificar ruta multi-etapa» representa Calcular ruta multi-etapa. Se conserva un único caso por función equivalente.

## Include

1. Crear misión → Validar drone apto: validación obligatoria antes de asignar/iniciar.
2. CU-E03 Planificar ruta multi-etapa → CU-E04 Autorizar ruta con Aerocivil: autorización obligatoria para la ruta inter-sede.
3. CU-E03 Planificar ruta multi-etapa → Validar estaciones de carga: la alternativa multi-etapa con recarga siempre verifica disponibilidad.

Todas las flechas `<<include>>` apuntan del caso base al incluido. Gestionar la estación (CU-E05) y validar su disponibilidad son responsabilidades distintas.

## Extend

1. Cancelar misión → Consultar misión, condición `[estado = PENDIENTE]`: cancelación opcional al consultar una misión pendiente; no se permite cancelar una entrega ya completada.
2. Marcar en mantenimiento → Diagnosticar fallo, condición `[requiere intervención]`: el diagnóstico puede completarse sin intervención física; marcar mantenimiento añade ese comportamiento cuando corresponde.

Las flechas `<<extend>>` apuntan de la extensión al caso base. Esta segunda relación evita una dependencia visual cruzada de notificación y vuelo, conservando una extensión coherente en Mantenimiento. Las asociaciones simples de actores son participación y no expresan secuencia temporal. La planificación autorizada se mantiene diferenciada de la solicitud de reparto.

## Archivos

- [General editable Draw.io](casos-uso-enterprise.drawio)
- [Diagrama general SVG](casos-uso-enterprise.svg)
- [Gestión de Misiones ampliada](misiones.svg)
- [Gestión de Flota ampliada](flota.svg)
- [Rutas y Navegación ampliada](rutas.svg)
- [Mantenimiento ampliado](mantenimiento.svg)
- [Administración ampliada](administracion.svg)

Las vistas ampliadas muestran cada paquete para leerlo sin reducir el texto. El general conserva las cinco agrupaciones, relaciones entre paquetes y herencia de actores.
