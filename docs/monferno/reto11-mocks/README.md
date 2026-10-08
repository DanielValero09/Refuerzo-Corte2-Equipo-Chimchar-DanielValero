# Monferno Reto 11 - Mocks con IA

Se generaron seis SVG originales con asistencia de IA/Codex a partir del [prompt completo](prompt-utilizado.md). Reutilizan exactamente la paleta, tipografías y estados del [sistema de diseño Reto 8](../reto8-ux/sistema-diseno.md).

## Flujo principal

1. [Panel de flota](01-panel-flota.svg): 20 drones resumidos por estado y agrupados por tipo, estado general, alertas y «Nueva misión».
2. [Formulario](02-formulario-mision.svg): origen, destino, peso, tipo, prioridad, clima, candidatos aptos y restricciones; «Continuar».
3. [Confirmación](03-confirmacion.svg): drone automático D-03 EXPRESS, batería 91%, URGENTE, ruta/ETA ilustrativos y «Confirmar / Cancelar».

La navegación se muestra en cada encabezado. Las acciones son estáticas; las cifras de clima, ruta, ETA y alertas son ejemplos, no datos reales ni cálculo operativo.

## Errores y recuperación

- [Sin drones aptos](error-sin-drones.svg): misión PENDIENTE, sin confirmar asignación; «Volver» o «Reintentar más tarde».
- [Clima adverso](error-clima-adverso.svg): condición no apta y flujo bloqueado; «Volver» o «Reintentar consulta».
- [Paquete pesado](error-paquete-pesado.svg): ejemplo 2300 g frente al máximo de 2000 g; «Volver» o «Corregir peso».

## Nielsen y validación

[Nueve heurísticas verificadas en elementos concretos](heuristicas-nielsen.md). La verificación corresponde al diseño visual estático, no a pruebas de usabilidad de una aplicación implementada.

Los seis SVG comparten dimensiones 1280 × 1000, viewBox, fuentes locales, texto real y la misma estructura. No contienen scripts, imágenes externas ni raster incrustado. Se validan como XML y se renderizan en Chrome para revisar contenido y recortes.

Fuente académica: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 11 de Monferno. No se usaron Figma, Midjourney ni DALL-E.
