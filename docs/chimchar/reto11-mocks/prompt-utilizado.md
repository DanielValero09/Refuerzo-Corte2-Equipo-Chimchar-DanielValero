# Reto 11 - Prompt utilizado

La siguiente especificación completa se escribió antes de generar los tres SVG y se utilizó para construirlos con asistencia de IA/Codex, mediante código vectorial SVG. No se utilizaron Figma, Midjourney, DALL-E ni un generador externo de imágenes.

## Especificación

```text
Actúa como diseñador UX/UI senior de sistemas de control.

SISTEMA:
SkyCampus — Panel de control de flota de drones ECI

PANTALLA:
Panel de monitoreo de la flota, vista principal del operador.
Título visible: SkyCampus — Panel de flota.

ACTOR:
Operador de drones: consulta los datos y selecciona manualmente un recurso.

REFERENCIAS DEL PROYECTO:
Reutiliza la composición de docs/chimchar/reto8-ux/panel-flota.svg,
la identidad de docs/chimchar/reto8-ux/manual-identidad.md y los datos
de SC-01 en docs/chimchar/reto7-dosw.md.
Fuente de alcance: HTML oficial DOSW_Equipo_Chimchar_fixed.html,
apartado 11 de Chimchar. No introducir evoluciones posteriores.

IDENTIDAD:
La definida en el Reto 8, sin sustituirla por otra identidad.
Primario #0B4F6C; secundario #0F766E; fondo #F4F7FA;
superficie #FFFFFF; texto principal #142D3A; texto secundario #526576;
bordes #CBD5E1.
DISPONIBLE: texto #166534, fondo #DCFCE7.
EN_VUELO: texto #1E40AF, fondo #DBEAFE.
EN_CARGA: texto #854D0E, fondo #FEF3C7.
FALLO: texto #991B1B, fondo #FEE2E2.
Interfaz Arial, Helvetica, sans-serif; IDs Consolas, Courier New, monospace.
Fuentes locales de respaldo. No afirmar cumplimiento WCAG sin medirlo.

DATOS:
Mostrar ID, batería en porcentaje y barra, estado textual y ubicación.
Exactamente cinco drones, conservando estas baterías y ubicaciones:
D-01: 85%, Bloque A.
D-02: 42%, Biblioteca.
D-03: 91%, Bloque C.
D-04: 18%, Bloque B.
D-05: 67%, Bloque D.
Modelo DJI Mini 3 opcional. No inventar telemetría adicional.

ACCIONES:
Seleccionar drone, Asignar a misión, Ver detalle.
Mostrar decisiones manuales y acciones bloqueadas cuando corresponde.
Son controles visuales estáticos, sin JavaScript ni interacción funcional.

ESTADOS:
1. Normal: D-01 DISPONIBLE, D-02 EN_VUELO, D-03 DISPONIBLE,
   D-04 EN_CARGA, D-05 DISPONIBLE.
   D-04 muestra batería crítica de 18% y asignación bloqueada.
2. FALLO: misma composición; D-04 pasa a FALLO con 18%.
   Mensaje explícito: D-04 presenta un fallo y no puede ser asignado.
   Solicitar seleccionar otro drone disponible y revisar D-04.
   Su selección y asignación deben verse deshabilitadas.
3. Sin drones disponibles: los cinco aparecen EN_VUELO.
   Mensaje: No hay drones disponibles en este momento.
   Ningún drone se puede seleccionar ni asignar.
   Mantener la flota visible para identificar los recursos en misión.
   Este escenario hipotético no representa una nueva asignación de D-04.

COMPOSICIÓN:
Tres SVG de 1600 x 1200, viewBox 0 0 1600 1200.
Cabecera, título, resumen de disponibles/en vuelo/en carga/fallo,
mensaje contextual, cinco tarjetas y guía de acciones manuales.
Mantener las posiciones entre estados para facilitar comparación.
Texto real, barras visibles, etiquetas completas y sin recortes.
No imágenes externas, raster incrustado, scripts ni fuentes descargadas.

NIELSEN:
#1 Visibilidad del estado: datos, etiquetas y contadores explícitos.
#3 Control y libertad del usuario: selección y asignación manuales.
#5 Prevención de errores: batería crítica y acciones bloqueadas.
#8 Diseño estético y minimalista: solo datos esenciales del MVP.
#9 Diagnóstico de errores: identificar D-04, explicar el bloqueo y
   proponer seleccionar otro drone; en vacío explicar la indisponibilidad.
Documentar decisiones del prototipo, no funciones ya implementadas.

ENTREGABLES:
panel-normal.svg, panel-fallo.svg y panel-vacio.svg.
Validar XML y revisar los tres mediante renderizado.
```

El fondo claro prevalece sobre el ejemplo genérico de fondo oscuro del HTML porque se conserva la identidad ya definida, según las instrucciones de este bloque. EN_CARGA y FALLO representan escenarios del prototipo; no son propiedades nuevas del record `Drone`.
