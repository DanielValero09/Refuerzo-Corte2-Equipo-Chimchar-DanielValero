# Reto 11 - Prototipo Navegable Enterprise

[Abrir index.html](index.html) en un navegador local. Es HTML/CSS/JavaScript interactivo, sin servidor obligatorio, imágenes externas, fuentes descargadas ni consultas a proveedores. Los SVG del Reto 8 siguen siendo referencias de componentes, no sustituyen esta navegación.

## Alcance

Cuatro sedes, 100 drones en un resumen sintético y flujo Dashboard → Nueva misión → Solicitud → Ruta/estaciones → Confirmación. El mensaje «Datos simulados — Prototipo académico» permanece visible. Confirmar genera un evento local, reduce la disponibilidad de la sede y agrega actividad; no recibe telemetría de drones físicos ni conecta con la API Java.

La identidad usa directamente [tokens.css del Reto 8](../reto8-ux/tokens.css). ECI y UNAL cambian mediante `data-theme` en el mismo DOM. ETA de 12 minutos y trayectos de 3 + 3 km son ejemplos identificados, no mediciones geográficas reales.

## Interacción y recuperación

Botones operativos; volver conserva peso, sedes y prioridad. Planificar muestra carga local y deshabilita temporalmente su acción. Un cambio de escenario invalida el plan; todos los errores bloquean confirmación. Los controles de demostración permiten clima adverso, falta de drones aptos, peso excesivo, rechazo Aerocivil, sede inactiva y estación no disponible. Para recuperar, corregir los datos y cambiar el escenario a Operación apta cuando corresponde.

## Evidencia

Recorrido automatizado real en Chrome headless/CDP: 26 comprobaciones, cero fallos. Incluye clics, seis errores y recuperación, persistencia de formulario, evento de asignación, teclado y viewport móvil. [Resultados reales](evidencia-navegador.json).

Reproducción en Node con WebSocket nativo y Chrome instalado:

```text
node docs/infernape/reto11-prototipo/pruebas-navegador.cjs
```

La variable opcional CHROME_PATH permite indicar el ejecutable de Chrome; no contiene credenciales. Las capturas se producen en una carpeta temporal y no se presentan como capturas Jira. No se instala Playwright ni se descargan navegadores.

## Archivos

- [Flujo y errores](flujo.md)
- [Evaluación de diez heurísticas](heuristicas-nielsen.md)
- [Prueba automatizada y plantilla manual](prueba-usabilidad.md)
- [CSS](styles.css)
- [JavaScript](app.js)

Generado con asistencia de Codex a partir del material DOSW y los tokens existentes; no se usó Figma u otra herramienta de diseño externa.

**Prueba real con compañero: PENDIENTE.** No se inventan participantes, preguntas ni resultados humanos.
