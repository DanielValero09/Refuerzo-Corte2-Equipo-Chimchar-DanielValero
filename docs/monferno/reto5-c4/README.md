# Monferno Reto 5 - Contexto C4

## Alcance

C4 nivel 1: SkyCampus v2 es una caja negra para el reparto interno mediante drones. Solo se muestran personas, sistemas externos y datos que atraviesan su límite; no se representan componentes, clases ni almacenamiento. Las integraciones son contratos de la v2, todavía sin implementación de red en este bloque.

## Actores

- Operador de drones
- Solicitante
- Admin
- Técnico de mantenimiento

## Sistemas externos

- API Meteorológica
- Control Aéreo ECI
- Sistema de Alertas

## Flujos

| Origen | Destino | Datos |
| --- | --- | --- |
| Solicitante | SkyCampus v2 | Registra solicitud de reparto: origen, destino, paquete y prioridad |
| SkyCampus v2 | Solicitante | Devuelve código y estado de la misión |
| Operador de drones | SkyCampus v2 | Supervisa flota y confirma/cancela misiones |
| SkyCampus v2 | Operador de drones | Estado de drones, misiones y alertas |
| Admin | SkyCampus v2 | Configura destinos, flota y reglas operativas |
| SkyCampus v2 | Admin | Confirma configuración |
| Técnico de mantenimiento | SkyCampus v2 | Diagnostica fallos y registra reparación |
| SkyCampus v2 | Técnico de mantenimiento | Notifica drones en FALLO y datos de diagnóstico |
| SkyCampus v2 | API Meteorológica | Solicita condiciones de viento y lluvia |
| API Meteorológica | SkyCampus v2 | Devuelve condiciones meteorológicas |
| SkyCampus v2 | Control Aéreo ECI | Registra vuelo y solicita autorización de ruta |
| Control Aéreo ECI | SkyCampus v2 | Devuelve autorización o rechazo |
| SkyCampus v2 | Sistema de Alertas | Envía alerta de drone en FALLO |
| Sistema de Alertas | SkyCampus v2 | Confirma recepción de la alerta |

El acuse del Sistema de Alertas confirma recepción, sin prometer un canal de entrega ni inventar proveedores. Hay 14 relaciones dirigidas y etiquetadas: ocho de actores y seis de sistemas externos.

## Comparación Chimchar / Monferno

| Aspecto | Chimchar MVP | Monferno v2 |
| --- | --- | --- |
| Actores | 3: Operador de drones, Solicitante, Admin | 4: los anteriores y Técnico de mantenimiento |
| Sistemas externos | 0 | 3: API Meteorológica, Control Aéreo ECI, Sistema de Alertas |
| Asignación | Manual por el operador | Automática según política, prioridad y aptitud |
| Flota | 5 drones DJI Mini 3 | 20 drones MINI, EXPRESS y CARGO |
| Estados | Disponibilidad booleana en Drone; estados visuales en el manual | Enum operativo: DISPONIBLE, EN_VUELO, ATERRIZANDO, FALLO, EN_CARGA, MANTENIMIENTO |
| Responsabilidad técnica | Sin actor de mantenimiento dedicado | Técnico diagnostica fallos y registra reparación |

Se mantienen el reparto interno, los tres actores iniciales, la supervisión y la consulta de misiones; crecen la flota, la automatización, el mantenimiento y las dependencias externas.

- [Contexto original Chimchar](../../chimchar/reto5-c4/README.md)
- [SVG Chimchar](../../chimchar/reto5-c4/contexto-chimchar.svg)
- [Contexto Monferno editable](contexto-monferno.drawio)
- [Contexto Monferno SVG](contexto-monferno.svg)

## Fuente y edición

HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, Monferno apartado 05, y flujos precisados por las instrucciones de este bloque. El archivo Draw.io contiene mxfile, diagram y mxGraphModel con nodos y relaciones editables; sus conexiones conservan origen, destino y etiqueta. El SVG reproduce las mismas entidades y relaciones.
