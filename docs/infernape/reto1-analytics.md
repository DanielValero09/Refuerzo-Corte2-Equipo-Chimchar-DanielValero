# Infernape Reto 1 - Analytics con Streams

## Una sola pasada de las misiones

`infernape.reto1.AnalyticsRed.calcular` ejecuta una única operación terminal sobre la lista: `misiones.stream().collect(groupingBy(..., Collector.of(...)))`. Cada AcumuladorSede pertenece al collector, no es una variable externa capturada ni un contador en un array. Mantiene total, entregadas, urgentes, suma de minutos y conteo por ID.

El combiner fusiona particiones y conteos; tiene una prueba específica. La búsqueda del drone ganador recorre el mapa de frecuencias ya agregado, no la lista de misiones. La transformación sobre Sede.values añade los Optional de las cuatro sedes, permitida por el requisito. No se usa for, while o do-while en el algoritmo.

## Modelo y resultados

| Resultado por sede | Regla |
| --- | --- |
| tasaExito | ENTREGADAS / total × 100 |
| tiempoPromedioEntrega | Solo duraciones de ENTREGADAS; OptionalDouble.empty si hubo actividad pero ninguna entrega |
| droneMasUtilizado | Todas las misiones de la sede con droneId presente; mayor conteo, desempate por ID ascendente |
| porcentajeUrgentes | URGENTE / total × 100 |
| Sin actividad | Optional.empty para esa sede, sin métricas ficticias en cero |

El resultado es un Map<Sede, Optional<MetricasSede>> inmutable con ECI, UNAL, UNIANDES y EAFIT. MisionEnterprise representa drone no asignado mediante Optional y duración mediante OptionalDouble. Una ENTREGADA exige drone y duración positiva finita; otros estados no registran duración. La sede de una misión es la sede responsable de esa operación; no se cuenta dos veces una entrega inter-sede.

## Pruebas reales

`infernape.reto1.AnalyticsRedTest`: @ParameterizedTest / @MethodSource, con cinco escenarios: sede vacía, una misión, empate de uso entre drones, empate entre sedes y red completa de cuatro universidades.

Cuatro pruebas adicionales verifican actividad PENDIENTE sin drone/promedio, promedio de entregadas sin incluir FALLIDA, lectura exacta de cada elemento una sola vez y combinación de particiones. Nueve casos ejecutados, todos AAA. No se atribuye rendimiento o telemetría real a los datos de ejemplo.

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, Infernape apartado 01. [Índice](README.md).
