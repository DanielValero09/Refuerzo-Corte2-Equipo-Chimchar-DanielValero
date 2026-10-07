# Reto 10 - Diagramas de Casos de Uso

## Actores

- Operador de drones
- Solicitante
- Admin

Los actores están fuera del límite del sistema **SkyCampus**; los casos de uso están dentro.

## Casos de uso

- Registrar misión
- Validar disponibilidad del drone
- Ver flota
- Asignar drone
- Cancelar misión pendiente
- Confirmar entrega
- Configurar destinos y flota
- Consultar estado de misión

## Asociaciones

| Actor | Casos de uso asociados |
| --- | --- |
| Solicitante | Registrar misión; Consultar estado de misión |
| Operador de drones | Ver flota; Asignar drone; Cancelar misión pendiente; Confirmar entrega |
| Admin | Configurar destinos y flota |

Las asociaciones son líneas continuas sin flechas; las dependencias UML son líneas discontinuas con flecha abierta.

## Include

Registrar misión `<<include>>` Validar disponibilidad del drone.

La validación siempre forma parte del registro; la flecha apunta desde Registrar misión hacia Validar disponibilidad del drone.

## Extend

Cancelar misión pendiente `<<extend>>` Consultar estado de misión.

Condición: **estado = PENDIENTE**.

La cancelación es un comportamiento opcional al consultar una misión, disponible únicamente cuando está PENDIENTE; la flecha apunta desde Cancelar misión pendiente hacia Consultar estado de misión. El Operador de drones se asocia a la cancelación; la asociación del Solicitante con la consulta no le atribuye la cancelación.

## Archivos

- [Diagrama editable en Draw.io](casos-uso-chimchar.drawio)
- [Diagrama SVG](casos-uso-chimchar.svg)

## Fuente y alcance

HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 10, «Diagramas de Casos de Uso». Se aplica el include exigido por el HTML y el extend solicitado en este bloque con su condición explícita; el modelo funcional no agrega integraciones ni funcionalidades de evoluciones posteriores.
