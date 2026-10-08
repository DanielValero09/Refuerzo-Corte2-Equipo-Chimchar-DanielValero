# Reto 8 - Design Tokens y UX multi-sede

Una estructura de Panel de misión activa y una de Tarjeta de drone se reutilizan para ECI, UNAL y UNIANDES. Cada ejemplo conserva contenido y DOM equivalentes; solo el contenedor data-theme y los tokens cambian. EAFIT tiene su bloque completo de tokens preparado, sin inventar una identidad distinta.

- [Design tokens, contraste e incorporación de una sede](design-tokens.md).
- [CSS compartido y cuatro temas](tokens.css).
- [Componentes estáticos HTML](componentes-enterprise.html).
- [Panel: tres variantes SVG](panel-mision-variantes.svg).
- [Tarjeta: tres variantes SVG](tarjeta-drone-variantes.svg).

Abrir componentes-enterprise.html directamente en un navegador. No necesita servidor, fuentes descargadas, JavaScript ni recursos de red. La página permite navegar entre ejemplos mediante enlaces; no es el prototipo de flujos del Reto 11.

Datos de ejemplo constantes: ME-015, ECI → UNAL, URGENTE, DE-07 EXPRESS, batería 70%, etapa 1 de 2, EN_VUELO y progreso 50%; aviso de recarga en C-116 disponible. No son telemetría ni tiempos reales.

Las cuatro visualizaciones nuevas (dos C4 y estos dos comparativos), junto con el HTML, se verifican mediante XML/estructura y render real de Chrome headless. La evidencia de revisión se registra al cerrar el bloque; no se fabrican capturas.

## Validación realizada

Chrome headless renderizó realmente el HTML y ambos SVG comparativos. Se revisaron visualmente sin recortes; la validación del navegador confirmó 3 paneles y 3 tarjetas, equivalencia completa del HTML de cada componente, cero desbordamiento horizontal y 37 reglas CSS. Los cuatro temas producen los colores, fuentes declaradas y radios oficiales. La fuente efectiva puede ser el fallback local, como documenta la especificación.

El archivo de producción no contiene scripts. Un script de QA temporal, fuera del repositorio, consultó DOM y estilos calculados; no es una dependencia de los componentes. Las capturas reales de revisión permanecen temporales y no se presentan como evidencia de Jira o Sonar.
