# Reto 6 - RF, RNF y MoSCoW

Alcance: MVP Chimchar con cinco drones DJI Mini 3, destinos fijos y asignación manual para sobres y carpetas. Esta especificación define lo requerido; no afirma que todos los requisitos estén implementados o medidos en el código actual.

## Requisitos y prioridades

| Código | Tipo | Requerimiento | MoSCoW | Justificación |
| --- | --- | --- | --- | --- |
| RF-01 | RF | **Ver flota disponible:** el Operador de drones consulta los drones disponibles con ID, batería, disponibilidad y ubicación; obtiene la lista actual de drones aptos para revisión. | Must Have | La revisión de la flota es indispensable para que el operador seleccione un drone manualmente. |
| RF-02 | RF | **Registrar misión de reparto:** el Operador de drones registra origen, destino, tipo de carga y drone asignado; obtiene un código de misión generado y estado inicial PENDIENTE. | Must Have | Registrar la misión es necesario para identificar y gestionar cada reparto del MVP. |
| RF-03 | RF | **Confirmar entrega:** el Operador de drones confirma la entrega de una misión; observa su estado actualizado a ENTREGADA. | Must Have | Confirmar la entrega permite cerrar el reparto y conocer su resultado. |
| RNF-01 | RNF | **Rendimiento:** el panel de flota muestra el resultado de una consulta de hasta cinco drones en menos de dos segundos, medidos desde la solicitud de consulta hasta la visualización completa. | Should Have | El límite de respuesta agiliza la revisión de la flota sin sustituir las funciones esenciales del reparto. |
| RNF-02 | RNF | **Usabilidad:** el panel muestra simultáneamente los cuatro datos —ID, batería, disponibilidad y ubicación— de cada drone consultado, sin navegar a otra pantalla. | Should Have | Ver los cuatro datos juntos reduce la navegación necesaria para elegir un drone. |
| RNF-03 | RNF | **Confiabilidad y validación:** el sistema rechaza el 100% de las asignaciones con batería inferior al 30% antes de registrar la misión. | Must Have | Rechazar una batería insuficiente es obligatorio para respetar el umbral de asignación del MVP. |

## Cómo verificar las métricas

- Rendimiento: cronometrar una consulta del panel con la flota de cinco drones y comprobar un tiempo inferior a dos segundos.
- Usabilidad: comprobar que cada drone muestra los cuatro datos simultáneamente en el mismo panel.
- Confiabilidad y validación: intentar asignaciones con cada valor entero de batería de 0 a 29 y comprobar que todas se rechazan y ninguna misión se registra.

## Fuente

HTML oficial de SkyCampus, `DOSW_Equipo_Chimchar_fixed.html`: enunciado Chimchar y apartado 06, «RF vs RNF y Prioridad MoSCoW»; los seis requisitos y sus prioridades concretan las instrucciones de este bloque.
