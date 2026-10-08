# SC-15 — Planificar ruta multi-etapa inter-sede

## Identificación

- Código: SC-15.
- Nombre: Planificar ruta multi-etapa inter-sede.
- Actor principal: Operador.
- Actores/sistemas secundarios: Coordinador de sede, Aerocivil, Estación de carga autónoma y Servicio de Rutas.
- Propósito: obtener una ruta operable y autorizada para repartir una carga entre sedes con etapas y recarga.

## Precondiciones

- Sedes origen y destino registradas en la red y distintas.
- Existen candidatos de ruta y drones que pueden ser evaluados.
- La configuración operativa de la sede está subordinada a las restricciones regulatorias.
- Hay datos actuales de disponibilidad de estaciones y un medio para verificar autorización; la ausencia de verificación no equivale a autorización.

## Datos de entrada

| Campo | Tipo concreto | Obligatorio | Regla |
| --- | --- | --- | --- |
| mision | Objeto MisionSolicitud | Sí | Contiene los subcampos definidos abajo |
| mision.id | String | Sí | Identificador no vacío, por ejemplo ME-015 |
| mision.paquete | Objeto PaqueteSolicitud | Sí | Contenedor tipado de peso, tipo, prioridad y restricciones |
| mision.paquete.pesoGramos | Integer | Sí | Mayor que 0; compatible con cada drone asignado |
| mision.paquete.tipo | Enum(SOBRE,CARPETA,LIBRO,EQUIPO) | Sí | Valor del catálogo Enterprise |
| mision.paquete.prioridad | Enum(URGENTE,NORMAL,BAJO) | Sí | Política de selección correspondiente |
| mision.paquete.restriccionesEspeciales | String | No | Ausencia representable mediante Optional<String> |
| mision.sedeOrigen | Enum(ECI,UNAL,UNIANDES,EAFIT) | Sí | Sede registrada |
| mision.sedeDestino | Enum(ECI,UNAL,UNIANDES,EAFIT) | Sí | Distinta del origen |
| condicionesEspacioAereo | Objeto CondicionesEspacioAereo | Sí para autorizar | Datos verificados; si no se obtienen, bloquear inicio |
| condicionesEspacioAereo.autorizado | Boolean | Sí | Debe ser true para iniciar |
| condicionesEspacioAereo.alturaMaximaMetros | Integer | Sí | Máximo aplicable entre 1 y 120 para autorización urbana; restricción menor prevalece |
| condicionesEspacioAereo.observacion | String | No | Explicación de autorización/rechazo, si existe |

Estos tipos describen el contrato DOSW; no implican llamadas reales ni todos estos DTO implementados. El puerto Aerocivil y su simulación local solo demuestran la verificación mínima.

## Datos de salida

| Campo | Tipo concreto | Regla |
| --- | --- | --- |
| rutaPlanificada | Objeto RutaPlanificada | Resultado de la planificación |
| rutaPlanificada.id | String | Código no vacío, por ejemplo RE-015 |
| rutaPlanificada.numeroEtapas | Integer | Al menos una etapa |
| rutaPlanificada.distanciaTotalKm | Decimal | Suma positiva de distancias; sin inventar coordenadas |
| rutaPlanificada.estacionCarga | String opcional | Identificador de estación intermedia si aplica; varias estaciones se identifican en las etapas de la ruta |
| rutaPlanificada.estadoAutorizacion | Enum(PENDIENTE,AUTORIZADA,RECHAZADA) | Solo AUTORIZADA permite inicio |

Postcondición exitosa: ruta continua, drones y estaciones aptos, autorización confirmada. El rechazo conserva una causa visible y no inicia el vuelo.

## Flujo básico

1. **Verificar Aerocivil:** Servicio de Rutas solicita/verifica autorización y límites para la operación presentada por el Operador.
2. **Calcular etapas:** Servicio de Rutas construye alternativas continuas entre sede origen y destino.
3. **Asignar estaciones de carga:** Servicio de Rutas selecciona estaciones disponibles; el Coordinador mantiene su configuración operativa.
4. **Asignar drones por etapa:** sistema aplica creadores/políticas y valida peso, disponibilidad, batería y autonomía con carga.
5. **Confirmar ruta:** Operador revisa etapas, recargas y autorización; sistema conserva la ruta autorizada confirmada.
6. **Iniciar vuelo:** sistema habilita el inicio solicitado por el Operador únicamente después de verificar todas las reglas y notifica eventos de etapa.

## Flujos alternos

### FA-01 — Aerocivil rechaza

Condición: autorización rechazada o no verificable. Resultado: ruta no inicia; informar causa o indisponibilidad de verificación. Un timeout simulado mayor de 2 segundos bloquea.

### FA-02 — No existe estación de carga disponible

Condición: las alternativas requieren una estación no disponible. Resultado: recalcular una alternativa operable o rechazar la ruta; no elegir la cerrada por ser más corta.

### FA-03 — Paquete demasiado pesado para ruta larga

Condición: ningún drone/cadena de etapas puede cumplir peso y autonomía con recargas permitidas. Resultado: rechazar planificación y explicar la restricción; no inventar un perfil nuevo como solución.

## Reglas de negocio

- **RN-SC15-01:** un drone no puede recorrer más de 5 km con carga sin recargar. El límite aplica al tramo acumulado entre recargas, no al total de toda la ruta.
- **RN-SC15-02:** un paquete no puede permanecer en una estación de carga más de 30 minutos.
- **RN-SC15-03:** no iniciar ruta inter-sede sin autorización regulatoria verificable.
- **RN-SC15-04:** no utilizar estación no disponible.
- **RN-SC15-05:** respetar el máximo regulatorio aplicable y nunca superar los 120 m urbanos definidos por RNF-09 en el material.

Esta plantilla especifica el flujo objetivo. Composite/Strategy, disponibilidad de estaciones y guardia regulatoria tienen evidencia local; la orquestación completa, permanencia temporal y control de autonomía entre recargas no se presentan como funcionalidades ya implementadas en este bloque.

## Trazabilidad

| Requisito | HU real suministrada | CU reservado |
| --- | --- | --- |
| RF-13 | SCRUM-22 | CU-E03 — Planificar ruta multi-etapa |
| RF-14 | SCRUM-23 | CU-E04 — Autorizar ruta con Aerocivil |
| RF-15 | SCRUM-24 | CU-E05 — Gestionar estación de carga |
| RNF-09 | SCRUM-23, como restricción de autorización | CU-E04 |
| RNF-11 | SCRUM-23, como fail-safe de autorización | CU-E04 |

[Matriz y pruebas asociadas](reto6-trazabilidad.md). Los IDs Jira se reutilizan; no se crean HU duplicadas ni se inicia el Reto 9.
