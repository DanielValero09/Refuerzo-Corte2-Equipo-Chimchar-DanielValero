# Monferno Reto 10 - Casos de Uso

## Actores humanos

- Operador
- Solicitante
- Técnico de mantenimiento
- Admin

Generalización: **Técnico de mantenimiento —|> Operador**. El triángulo hueco apunta al Operador: el Técnico hereda sus CU y añade mantenimiento, sin repetir asociaciones heredadas.

## Casos de uso

| Participante | Casos de uso |
| --- | --- |
| Solicitante | Registrar solicitud de reparto; Consultar estado de misión |
| Operador | Ver flota; Asignar drone automáticamente; Consultar misión; Cancelar misión; Confirmar inicio de vuelo |
| Técnico | Diagnosticar fallo; Registrar reparación; además de los heredados |
| Admin | Configurar flota; Configurar destinos / reglas operativas |
| Incluidos / extensiones | Validar condiciones climáticas; Validar drone apto; Alertar técnico; Solicitar autorización de ruta |

Los 15 CU están dentro del límite SkyCampus v2; los cuatro actores están fuera.

## Include

| Base → incluido | Motivo |
| --- | --- |
| Asignar drone automáticamente → Validar condiciones climáticas | La asignación exige comprobar clima apto |
| Asignar drone automáticamente → Validar drone apto | Siempre valida batería, capacidad, disponibilidad y estado |
| Confirmar inicio de vuelo → Solicitar autorización de ruta | El contrato v2 exige autorización antes del inicio |

Todas las flechas discontinuas `<<include>>` apuntan del caso base al incluido.

## Extend

| Extensión → base | Condición |
| --- | --- |
| Alertar técnico → Asignar drone automáticamente | [drone candidato con batería entre 30% y 40%] |
| Cancelar misión → Consultar misión | [estado permite cancelación] |

Las flechas discontinuas `<<extend>>` apuntan de la extensión al caso base. El primer caso es el ejemplo académico de aviso preventivo solicitado para el diagrama; no modifica el Observer AlertaTecnico existente, que emite alertas por FALLO. Son motivaciones distintas y no se afirma que el aviso preventivo esté implementado.

Para este diagrama, cancelar se ofrece al consultar una misión PENDIENTE; una ENTREGADA no permite cancelación. La condición queda explícita junto a la flecha y no supone una operación de cancelación en vuelo ya implementada.

## Control Aéreo ECI

Es un sistema externo del C4, no un quinto actor humano. Se documenta su integración a través del CU «Solicitar autorización de ruta», incluido por «Confirmar inicio de vuelo»; no se agrega una figura de actor externo para mantener los cuatro actores académicos y la legibilidad. Este bloque no implementa red ni autorización real.

## Archivos y fuente

- [Draw.io editable](casos-uso-monferno.drawio)
- [SVG](casos-uso-monferno.svg)
- [Contexto v2](../reto5-c4/README.md)

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 10 de Monferno, con dirección UML y condiciones precisadas por el usuario. Se validan cuatro actores, una generalización, tres include y dos extend.
