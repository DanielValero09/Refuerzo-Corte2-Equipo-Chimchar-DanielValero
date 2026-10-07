# Reto 4 - Principios SOLID

## Código original: GestorDrone

### SRP — Violado

GestorDrone tenía múltiples razones para cambiar porque asignaba, persistía, alertaba, reportaba y calculaba rutas.

Se separaron esas responsabilidades en `AsignadorMision`, `RepositorioMisionMemoria`, `AlertaConsola`, `GeneradorReporte` y las implementaciones de `EstrategiaRuta`.

### OCP — Violado

El if/else de calcularRuta obligaba a modificar GestorDrone cada vez que aparecía un nuevo tipo de ruta.

`EstrategiaRuta` permite agregar nuevas implementaciones sin modificar las existentes; la demostración intercambia `RutaDirecta` por `RutaEvitarObstaculos` usando la misma interfaz.

### LSP — No hay violación directa

No existe una violación directa de LSP en el fragmento original porque no se presenta una jerarquía de herencia.

### ISP — Posible violación / mejorado por separación

Un consumidor que solo requería asignación quedaba expuesto a responsabilidades de persistencia, alertas y reportes.

Ahora cada servicio/interfaz es pequeño y específico: asignar no requiere conocer almacenamiento, notificaciones, reportes ni rutas.

### DIP — Violado

GestorDrone dependía directamente de DriverManager/MySQL en lugar de una abstracción.

Ahora el código puede depender de `RepositorioMision` y `AlertaOperador`; `Reto4` utiliza esos contratos y configura las implementaciones de memoria y consola para la demostración.

## Tabla de rediseño

| Responsabilidad | Clase/Interfaz | Única razón para cambiar |
| --- | --- | --- |
| Asignación | AsignadorMision | Cambian reglas de asignación |
| Persistencia | RepositorioMision | Cambia contrato de almacenamiento |
| Almacenamiento en memoria | RepositorioMisionMemoria | Cambia implementación en memoria |
| Alertas | AlertaOperador | Cambia contrato de notificación |
| Reporte | GeneradorReporte | Cambia formato del reporte |
| Ruta | EstrategiaRuta | Cambia contrato del cálculo |
| Ruta directa | RutaDirecta | Cambia algoritmo directo |
| Evitar obstáculos | RutaEvitarObstaculos | Cambia algoritmo seguro |

## Demostración

`AsignadorMision` devuelve una nueva misión con el drone asignado y conserva ID, origen, destino, tipo de carga, estado, prioridad, notas y hora máxima; la misión original permanece intacta. El repositorio guarda por ID, reemplaza la misión al guardar de nuevo el mismo ID, devuelve `Optional.empty()` si no existe y lista una copia inmutable de las misiones en orden de inserción.

La alerta y el reporte son textuales; las rutas representan el recorrido de forma textual dentro del alcance del ejercicio.

```bash
mvn clean test
java -cp target/classes src.main.java.reto4.Reto4
```
