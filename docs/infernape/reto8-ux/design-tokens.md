# Design tokens Enterprise

Fuente: tabla oficial Infernape 08 en DOSW_Equipo_Chimchar_fixed.html. Los valores institucionales se conservan exactamente.

## Tokens institucionales

| Token | ECI | UNAL | UNIANDES | EAFIT |
| --- | --- | --- | --- | --- |
| --color-primary | #00457C | #7B0000 | #0057A8 | #2D6A4F |
| --color-alert | #E63946 | #E63946 | #E63946 | #E63946 |
| --font-ui | Space Grotesk | Merriweather | Inter | Source Sans |
| --border-radius | 10px | 4px | 8px | 12px |

Las familias se declaran por nombre, con fallback compartido Arial, Helvetica, sans-serif. No se descargan fuentes ni se garantiza que las cuatro estén instaladas; si no están, el navegador usa fallback. IDs técnicos usan Consolas, Courier New, monospace.

## Tokens compartidos

| Token | Valor | Uso |
| --- | --- | --- |
| --color-background | #F4F7FA | Fondo |
| --color-surface | #FFFFFF | Superficies de componentes |
| --color-text | #142D3A | Texto principal |
| --color-muted | #526576 | Etiquetas y notas |
| --color-border | #D3DEE7 | Separadores y pista de progreso |
| --color-alert-surface | #FDEDEE | Fondo del aviso |
| --spacing-1 / 2 / 3 / 4 | 0.5rem / 1rem / 1.5rem / 2rem | Escala de espacio |
| --shadow-card | 0 8px 24px rgb(20 45 58 / 8%) | Elevación suave |
| --font-mono | Consolas, Courier New, monospace | IDs |

Estos valores son iguales entre los cuatro temas. No se añade CSS específico por universidad para panel o tarjeta. tokens.css contiene los bloques [data-theme="eci"], [data-theme="unal"], [data-theme="uniandes"] y [data-theme="eafit"].

## Componentes reutilizables

**Panel de misión activa:** article.panel-mision, encabezado con ID/estado, dl de origen/destino/prioridad/drone/batería/etapa, progress y aviso textual. **Tarjeta:** article.tarjeta-drone, ID/tipo, estado/adscripción y batería/progreso. Son instancias de una misma estructura por componente, con los mismos datos; no tres implementaciones divergentes.

El generador utilizó una única plantilla para cada componente y repitió instancias estáticas. La validación compara sus estructuras completas para probar equivalencia. Se adapta de tres columnas a una cuando la pantalla se reduce. Los enlaces tienen foco visible y la página usa lang=es, encabezados, dl y progress con nombre accesible.

## Accesibilidad y contraste medido

El estado mostrado combina símbolo ●, texto EN_VUELO y color; prioridad incluye ↑ y texto URGENTE. El aviso usa !, explicación completa, borde rojo y fondo claro. El porcentaje es visible junto a cada indicador; el color no es el único canal.

Se calculó contraste sRGB: convertir cada canal a luminancia lineal, L = 0.2126R + 0.7152G + 0.0722B, razón = (L mayor + 0.05)/(L menor + 0.05). Valores reales, redondeados a tres decimales:

| Combinación | Color A | Color B | Contraste |
| --- | --- | --- | --- |
| ECI primario / blanco | #00457C | #FFFFFF | 9.803:1 |
| UNAL primario / blanco | #7B0000 | #FFFFFF | 11.399:1 |
| UNIANDES primario / blanco | #0057A8 | #FFFFFF | 7.170:1 |
| EAFIT primario / blanco | #2D6A4F | #FFFFFF | 6.391:1 |
| Texto / superficie | #142D3A | #FFFFFF | 14.323:1 |
| Secundario / superficie | #526576 | #FFFFFF | 6.031:1 |
| Texto / aviso | #142D3A | #FDEDEE | 12.635:1 |
| Alerta / blanco | #E63946 | #FFFFFF | 4.168:1 |

El rojo oficial sobre blanco da 4.168:1: se conserva como borde/símbolo y no como texto pequeño del aviso. El texto del aviso usa #142D3A sobre #FDEDEE (12.635:1). Las combinaciones de texto principal, secundario y blanco sobre primarios medidas superan 4.5:1. Esto no constituye una certificación WCAG completa; faltaría una auditoría integral de todos los estados y usos futuros.

## Incorporación de una nueva sede

La especificación está diseñada para permitir a otro equipo configurar una quinta universidad en menos de 2 horas; no se cronometró realmente. Alcance: tema de los componentes existentes. Incorporar una nueva sede operativa al dominio requeriría además cambiar su catálogo y pruebas, fuera de ese presupuesto visual.

1. Recopilar los cuatro tokens institucionales y aprobación de identidad (presupuesto orientativo: 20 min).
2. Crear un bloque data-theme nuevo con esos tokens, sin copiar componentes (15 min).
3. Calcular contraste de texto, badges y alertas; conservar valores oficiales y ajustar el uso/fondos cuando haga falta (20 min).
4. Revisar Panel con datos largos, progreso y aviso, también en ancho reducido (15 min).
5. Revisar Tarjeta, estados textuales, IDs y batería (15 min).
6. Ejecutar checklist: misma estructura DOM, tokens completos, fallback sin red, foco, símbolos/texto, CSS válido y ausencia de recortes (15 min).
7. Obtener aprobación de la identidad y registrar la revisión (10 min).

Presupuesto de diseño: 110 minutos. Es una estimación de procedimiento, no una medición histórica. Una desviación de contraste o aprobación puede requerir más tiempo; nunca se omite la revisión para cumplir el objetivo.

## Alcance del bloque

Ejemplos estáticos HTML/CSS/SVG. No se inicia prototipo navegable, frontend operativo, descarga de fuentes o Reto 9. [Demostración local](componentes-enterprise.html) · [CSS](tokens.css).
