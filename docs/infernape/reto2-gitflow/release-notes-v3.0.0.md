# SkyCampus Enterprise v3.0.0 — notas académicas

Esta release simulada contiene únicamente el primer bloque Enterprise:

- Analytics por sede con un Collector y una sola pasada sobre las misiones.
- Rutas multi-etapa mediante Composite, optimización con dos Strategy, eventos mediante Observer y tres creadores Factory Method.
- Asignación mediante puertos de dominio, aplicación e infraestructura local; sin HTTP ni base de datos real.
- Diagramas, justificaciones y pruebas parametrizadas/Mockito con AAA.

Validación de la base ejecutada el 8 de octubre de 2026: `mvn clean verify`, 155 pruebas (104 previas + 51 Enterprise), 0 fallos, 0 errores y 0 omitidas; BUILD SUCCESS. JaCoCo conserva los umbrales heredados LINE 80% y BRANCH 70%, con check satisfactorio.

Los tags son evidencia de GitFlow académico, no una publicación de `main` real ni un cierre de los 14 retos Enterprise. No hay integración real con Aerocivil, estaciones físicas o APIs. Los dos perfiles adicionales no tienen nombres oficiales y no se inventan. Los pendientes de SonarQube y capturas Jira de las evoluciones anteriores permanecen abiertos.
