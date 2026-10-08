# Monferno Reto 8 - Identidad y UX

Sistema de componentes y flujo de asignación de SkyCampus v2. Extiende el [manual real Chimchar](../../chimchar/reto8-ux/manual-identidad.md) sin cambiar la marca; fuente funcional: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 08 de Monferno.

## Archivos

- [Sistema de diseño, estados y leyes UX](sistema-diseno.md)
- [Tarjeta de drone: cinco variantes](tarjeta-drone-estados.svg)
- [Botón Asignar misión: cinco estados](boton-asignar-estados.svg)
- [Indicador de batería: tres rangos](indicador-bateria.svg)
- [Flujo completo: panel → detalle → confirmación](flujo-asignacion.svg)

## Coherencia con el dominio

Se amplió únicamente `monferno.model.EstadoDrone` con EN_CARGA y MANTENIMIENTO, conservando DISPONIBLE, EN_VUELO, ATERRIZANDO y FALLO. Las cinco variantes pedidas no eliminan ATERRIZANDO: ese estado sigue operativo y debe mostrarse con texto explícito al presentarse. Los nuevos estados bloquean selección mientras no se regrese a DISPONIBLE; observadores existentes siguen recibiendo los cambios.

Los SVG son prototipos estáticos con texto real, fuentes locales y símbolos, sin scripts, imágenes externas ni controles funcionales. Las ubicaciones, alertas, clima, ruta y ETA son datos ilustrativos del diseño; no constituyen telemetría real ni integraciones implementadas. Las tarjetas de componentes son ejemplos independientes; no describen necesariamente el mismo snapshot que el flujo.

El panel agrupa 20 drones por tipo y resume sus estados; limita las alertas visibles sin confirmar a siete. La confirmación conserva control del operador sin convertir la elección automática del drone en selección manual.
