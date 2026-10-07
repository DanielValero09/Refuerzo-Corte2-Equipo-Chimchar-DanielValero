# Reto 5 - Diagrama de Contexto C4

## Alcance

SkyCampus MVP se representa como caja negra en C4 nivel 1: sistema de reparto interno mediante drones, sin detallar su implementación.

## Actores

- Operador de drones
- Solicitante
- Admin

## Sistemas externos

Ninguno.

## Flujos

| Origen | Destino | Datos |
| --- | --- | --- |
| Solicitante | SkyCampus App | Envía solicitud: origen, destino y tipo de carga |
| SkyCampus App | Solicitante | Devuelve código y estado de la misión |
| Operador de drones | SkyCampus App | Asigna drone y confirma entrega |
| SkyCampus App | Operador de drones | Muestra flota: ID, batería, disponibilidad, ubicación y estado de misión |
| Admin | SkyCampus App | Configura destinos y datos de la flota |
| SkyCampus App | Admin | Confirma configuración |

## Archivos

- [Diagrama editable en Draw.io](contexto-chimchar.drawio)
- [Diagrama exportado en SVG](contexto-chimchar.svg)

## Fuente

HTML oficial de SkyCampus, `DOSW_Equipo_Chimchar_fixed.html`: enunciado Chimchar y apartado 05, «Diagrama de Contexto C4»; los textos de los seis flujos se precisan según las instrucciones de este bloque.
