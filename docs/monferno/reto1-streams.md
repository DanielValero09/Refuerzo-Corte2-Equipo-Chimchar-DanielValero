# Monferno Reto 1 - Streams

Implementación: `monferno.reto1.EstadisticasMisiones`. Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 01 de Monferno.

| Consulta | Método | Operaciones y resultado |
| --- | --- | --- |
| Completadas hoy por tipo | completadasHoyPorTipo(misiones, hoy) | Filtra ENTREGADA y fecha de entrega; `groupingBy` + `counting`; Map<TipoDrone, Long>. |
| Drone con más completadas | droneConMasCompletadas(misiones) | Filtra ENTREGADA, agrupa por ID con `groupingBy` + `counting` y obtiene `max`; Optional<Drone>. |
| Porcentaje de fallidas | porcentajeFallidas(misiones) | Cuenta FALLIDA mediante Stream y divide sobre todas las misiones; double, 0.0 si la lista está vacía. |
| Urgente pendiente antigua | existeUrgentePendienteAntigua(misiones, ahora) | `anyMatch`: URGENTE, PENDIENTE y creadaEn anterior a ahora menos diez minutos. |

## Fechas y desempates

El día de referencia se recibe como `LocalDate`; la comparación de pendientes recibe `LocalDateTime ahora`. No se consulta el reloj interno ni se usa una fecha actual hardcodeada. Los LocalDateTime deben compartir la referencia horaria del campus.

Una misión creada ayer y entregada hoy se cuenta hoy; una entregada ayer queda fuera. Se usa el instante real de entrega, no el de creación como sustituto.

Para el ganador se cuentan las ENTREGADAS de la lista recibida, no el contador acumulado almacenado en Drone. Se agrupa por ID para no dividir un drone por sus distintos snapshots inmutables. En empate gana el ID lexicográficamente menor; se devuelve el primer snapshot entregado de ese ID presente en la entrada. El ganador por ID no depende del orden de las misiones.

El mapa solo contiene tipos con entregas ese día; no se inventan entradas de cero. Sin entregadas, el ganador es Optional.empty(). Exactamente diez minutos no cumple «más de diez»; once minutos sí.

## Pruebas

`EstadisticasMisionesTest`: 12 pruebas AAA, incluyendo los tres tipos, fechas de entrega, cruce de día, ganador por conteo, snapshots, empate en ambos órdenes, porcentaje 25%, lista vacía, urgente pendiente de 11 minutos, límite exacto de 10, NORMAL antigua, urgente en vuelo y fecha futura.

El código usa Streams y no contiene ciclos for, while ni do-while.
