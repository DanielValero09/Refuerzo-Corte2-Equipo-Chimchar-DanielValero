# Prompt utilizado para los mocks Monferno

Los SVG fueron producidos con asistencia de IA/Codex mediante generación vectorial local, a partir de esta especificación completa; no se utilizaron herramientas externas de generación de imágenes.

```text
Actúa como diseñador UX/UI senior de sistemas de control.

SISTEMA: SkyCampus v2, reparto interno de documentos/paquetes ECI con 20 drones.
ACTOR: Operador de drones que supervisa la asignación automática.
ALCANCE: tres pantallas principales y tres estados de error, como SVG estáticos.

IDENTIDAD EXACTA DEL RETO 8:
Primario #0B4F6C; secundario #0F766E; fondo #F4F7FA;
texto principal #142D3A; texto secundario #526576;
superficie #FFFFFF y bordes #CBD5E1.
UI: Arial, Helvetica, sans-serif. IDs: Consolas, Courier New, monospace.
Semántica: DISPONIBLE verde #166534/#DCFCE7;
EN_VUELO azul #1E40AF/#DBEAFE; EN_CARGA amarillo #854D0E/#FEF3C7;
FALLO rojo #991B1B/#FEE2E2; MANTENIMIENTO gris #475569/#E2E8F0.
Usar texto y símbolos además del color.

FORMATO: seis SVG de 1280x1000, con viewBox, texto vectorial real y títulos.
Sin JavaScript, imágenes externas, fuentes descargadas ni controles funcionales.
Encabezado común de SkyCampus v2 / Operador / Prototipo.
Mostrar Panel -> Formulario -> Confirmación en la navegación.
Todos los datos son ejemplos; señalarlo en pantalla, especialmente clima, ruta y ETA.

PANEL:
20 drones: 8 DISPONIBLE, 6 EN_VUELO, 3 EN_CARGA, 2 MANTENIMIENTO y 1 FALLO.
Agrupar por tipo: 7 MINI (500 g), 7 CARGO (2000 g), 6 EXPRESS (800 g).
No dibujar veinte tarjetas gigantes en lista plana.
Mostrar estado general en supervisión, filtros Estado/Tipo, dos alertas ilustrativas
(D-04 FALLO; D-09 batería 18%) y máximo siete alertas visibles sin confirmar.
Acción grande Nueva misión y Cancelar misión accesible sin menú.

FORMULARIO:
Origen BLOQUE_A, destino BIBLIOTECA, peso 300 g, tipo SOBRE, prioridad URGENTE.
Clima apto de ejemplo: viento suave / sin lluvia, respuesta simulada del prototipo.
Candidatos: D-03 EXPRESS 91% propuesto, D-08 MINI 96% apto, D-12 CARGO 88% apto.
Recordar batería >=30%, máximo 2000 g, CARGO prohibido por debajo de 100 g.
URGENTE prefiere EXPRESS compatible aunque MINI tenga mayor batería.
Botones Volver y Continuar.

CONFIRMACIÓN:
ID D-03, tipo EXPRESS, batería 91%, prioridad URGENTE, misión M-V2-007,
paquete SOBRE 300 g, ruta estimada Bloque A -> Biblioteca, ETA ilustrativo 4 min.
Indicar que no es navegación real y que la autorización es un contrato externo.
Botones Confirmar y Cancelar; no simular una autorización real de vuelo.

ERRORES:
1. No hay drones aptos disponibles: misión PENDIENTE, explicación y Volver /
Reintentar más tarde; no mostrar confirmación activa.
2. Clima adverso: viento fuerte de ejemplo, asignación/vuelo bloqueados;
Volver / Reintentar consulta; no permitir confirmar vuelo.
3. Paquete pesado: peso ingresado 2300 g, máximo soportado 2000 g;
explicar y ofrecer Volver / Corregir peso, sin confirmar asignación.
Conservar visualmente el contexto y explicar causa y recuperación.

NIELSEN A VERIFICAR CON ELEMENTOS CONCRETOS:
1 Visibilidad; 2 correspondencia con el mundo real; 3 control y libertad;
4 consistencia; 5 prevención; 6 reconocer mejor que recordar;
7 flexibilidad/eficiencia; 8 minimalismo; 9 diagnóstico y recuperación.

CALIDAD: texto legible, acciones amplias, sin solapamientos ni recortes,
misma estructura y tokens en las seis pantallas, XML válido.
```
