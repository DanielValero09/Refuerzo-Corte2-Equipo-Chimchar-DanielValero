# Evaluación de las diez heurísticas de Nielsen

Esta evaluación relaciona elementos observables del prototipo con pruebas automatizadas y revisión visual. No equivale a validación con usuarios ni a certificación de accesibilidad.

| # | Heurística | Pantalla / elemento | Resultado verificable | Estado |
|---|---|---|---|---|
| 1 | Visibilidad del estado | Dashboard y Planificar: resumen por sede, carga, actividad y aviso live. | Confirmar reduce disponibles de ECI de 12 a 11 y añade evento local; carga visible durante planificación. | Verificado en navegador |
| 2 | Correspondencia con el mundo real | Solicitud/Ruta: sedes, gramos, batería, estación y autorización. | Muestra unidades y secuencia de reparto; trayectos y ETA se identifican como ejemplos. | Revisado visualmente; comprensión humana pendiente |
| 3 | Control y libertad | Ruta/Confirmación: Volver, Corregir y Cancelar planificación. | Volver conserva 700 g y URGENTE; cancelar vuelve a la red sin crear evento. | Verificado en navegador |
| 4 | Consistencia y estándares | Todas: mismos componentes y tokens ECI/UNAL. | Cambiar tema modifica #00457C a #7B0000 sin duplicar formulario. | Verificado en navegador |
| 5 | Prevención de errores | Solicitud: sedes distintas, peso positivo, máximos y seis escenarios. | Seis errores y sedes iguales bloquean continuar y confirmar. | Verificado en navegador |
| 6 | Reconocer mejor que recordar | Ruta/Confirmación: resumen visible de solicitud, etapas y candidato. | Prioridad y peso siguen visibles al revisar; el usuario no debe volver para recordarlos. | Verificado en navegador |
| 7 | Flexibilidad y eficiencia | Dashboard/solicitud: teclado nativo, valores iniciales y diseño adaptable. | Enter activa Nueva misión; viewport de 390 px sin desbordamiento horizontal. | Parcial: sin atajos avanzados ni tiempos de tarea humanos |
| 8 | Diseño estético y minimalista | Dashboard: cuatro tarjetas agregadas, sin lista plana de cien drones. | Cuatro grupos legibles, acción primaria visible y datos esenciales. | Revisión visual realizada; valoración humana pendiente |
| 9 | Reconocer, diagnosticar y recuperar | Aviso de error con causa y botón Corregir/reintentar. | Cada error es visible y permite volver a Ruta tras recuperar el escenario. | Verificado en navegador |
| 10 | Ayuda y documentación | Controles de demostración y ayuda; README y flujo. | Se abre la ayuda y presenta instrucciones del flujo y límites de simulación. | Verificado en navegador; suficiencia para compañero pendiente |

La prueba real de compañero sigue PENDIENTE. El recorrido no evalúa lector de pantalla, todas las combinaciones de dispositivos ni facilidad subjetiva; esos resultados no se inventan. Una primera comprobación automatizada de teclado falló por una secuencia CDP incompleta de Enter; tras enviar rawKeyDown/char/keyUp, la acción y el foco real fueron verificados sin añadir una imitación de teclado al producto.
