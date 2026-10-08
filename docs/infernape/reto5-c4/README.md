# Reto 5 - C4 Enterprise

## Alcance y estado de implementación

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado Infernape 05, y precisiones del bloque 2/4. Ambos niveles representan **arquitectura objetivo Enterprise**. No acreditan servicios desplegados, JDBC, bases de datos reales, HTTP ni integración real con Aerocivil, ERP o Analytics. El código actual sigue usando puertos y adaptadores locales.

## Nivel 1 - Contexto

SkyCampus Enterprise es una caja negra. Hay exactamente seis actores: Operador, Solicitante, Admin, Técnico de mantenimiento, Coordinador de sede y Superadministrador de red. Los seis sistemas externos son API Meteorológica, Control Aéreo ECI, Sistema de Alertas, Aerocivil, ERP universitario y Plataforma Analytics. No se inventan proveedores.

Las 22 relaciones muestran datos y dirección; las respuestas agregadas a los actores explicitan qué observan. No hay servicios internos ni almacenamiento en este nivel.

| Origen | Destino | Datos |
| --- | --- | --- |
| Operador | SkyCampus Enterprise | Gestiona misiones y supervisa flota de su sede |
| SkyCampus Enterprise | Operador | Estado de misiones y flota |
| Solicitante | SkyCampus Enterprise | Registra y consulta solicitudes |
| SkyCampus Enterprise | Solicitante | Código y estado de solicitud |
| Admin | SkyCampus Enterprise | Administra usuarios, destinos y configuración |
| SkyCampus Enterprise | Admin | Confirma configuración |
| Técnico de mantenimiento | SkyCampus Enterprise | Gestiona mantenimiento y retorno al servicio |
| SkyCampus Enterprise | Técnico de mantenimiento | Fallos y datos de mantenimiento |
| Coordinador de sede | SkyCampus Enterprise | Configura operación y flota de la sede |
| SkyCampus Enterprise | Coordinador de sede | Estado operativo y configuración de sede |
| Superadministrador de red | SkyCampus Enterprise | Supervisa red completa y analytics |
| SkyCampus Enterprise | Superadministrador de red | Resumen de red y analytics |
| SkyCampus Enterprise | API Meteorológica | Solicita condiciones meteorológicas |
| API Meteorológica | SkyCampus Enterprise | Devuelve condiciones meteorológicas |
| SkyCampus Enterprise | Control Aéreo ECI | Registra vuelo y solicita autorización de ruta |
| Control Aéreo ECI | SkyCampus Enterprise | Devuelve autorización o rechazo |
| SkyCampus Enterprise | Sistema de Alertas | Envía alertas operativas |
| SkyCampus Enterprise | Aerocivil | Solicita autorización; consulta restricciones |
| Aerocivil | SkyCampus Enterprise | Devuelve autorización y restricciones inter-sede |
| SkyCampus Enterprise | ERP universitario | Solicita datos institucionales para operación |
| ERP universitario | SkyCampus Enterprise | Devuelve datos institucionales necesarios |
| SkyCampus Enterprise | Plataforma Analytics | Envía métricas agregadas de operación |

## Nivel 2 - Contenedores

Nueve contenedores dentro del límite SkyCampus Enterprise:

1. App Web Operadores.
2. Panel Superadmin.
3. API Gateway.
4. Servicio de Misiones.
5. Servicio de Flota.
6. Servicio de Rutas.
7. Servicio de Analytics.
8. BD Misiones.
9. BD Flota.

| Origen | Destino | Protocolo / responsabilidad objetivo |
| --- | --- | --- |
| App Web Operadores | API Gateway | HTTPS |
| Panel Superadmin | API Gateway | HTTPS |
| API Gateway | Los cuatro servicios | interno, sin fijar transporte aún |
| Servicio de Misiones | BD Misiones | JDBC |
| Servicio de Flota | BD Flota | JDBC |
| Servicio de Rutas | API Meteorológica | HTTP, consulta de condiciones |
| Servicio de Rutas | Aerocivil | HTTP, autorización y restricciones |
| Servicio de Analytics | Plataforma Analytics | REST, métricas agregadas |
| Servicio de Misiones | Servicio de Flota | interno, consulta candidatos |
| Servicio de Misiones | Servicio de Rutas | interno, planifica etapas |
| Servicio de Misiones | Servicio de Analytics | interno, publica resultados |

Las tres dependencias internas entre servicios son decisiones de diseño que concretan la colaboración, sin añadir microservicios. El nivel 2 enfoca los protocolos explícitos del material y muestra los tres sistemas externos asociados a ellos; los otros externos permanecen en el contexto completo y su contrato de transporte está por definir. No se atribuyen frameworks o proveedores al diseño.

## Comparación de evoluciones

| Aspecto | Chimchar | Monferno | Infernape |
| --- | --- | --- | --- |
| Sedes | ECI, campus único | ECI, campus único | ECI, UNAL, UNIANDES, EAFIT |
| Drones | 5, DJI Mini 3 | 20, MINI/CARGO/EXPRESS | 100 declarados; cinco perfiles, dos sin nombre oficial |
| Actores | 3 | 4, agrega Técnico | 6, agrega Coordinador y Superadmin |
| Sistemas externos | 0 | 3 | 6 |
| Asignación | Manual | Automática según aptitud/prioridad | Flota compartida segmentada por sede, políticas inyectables |
| Rutas | Representaciones simples del reto SOLID | Contexto con autorización de Control Aéreo ECI | Multi-etapa con estaciones y regulación inter-sede |
| Almacenamiento conceptual | Colecciones locales | Colecciones locales | BD Misiones y BD Flota como objetivo; código actual local |
| Analytics | Consultas de drones con Streams | Estadísticas de misiones | Cuatro métricas por sede en una pasada |
| Regulación | Sin sistema regulador externo | Control Aéreo ECI | Aerocivil y restricciones obligatorias, según el material académico |
| Complejidad arquitectónica | MVP como caja negra | Flota mayor, Strategy/Observer e integraciones | Contexto y contenedores diferenciados, puertos y tres capas |

**¿Qué creció?** La red, el número de drones, roles, dependencias externas, responsabilidades de rutas y analytics, y separación conceptual del almacenamiento.

**¿Qué se mantuvo?** El reparto de cargas, supervisión humana, identidad del sistema, conceptos de drone/misión y validaciones de aptitud; las evoluciones previas y sus pruebas se conservan.

**¿Cómo creció la complejidad sin perder coherencia?** El nivel 1 mantiene un límite único comprensible; el nivel 2 distribuye responsabilidades conocidas y contratos explícitos. Los puertos y patrones del código permiten extender políticas sin mezclar infraestructura con dominio. La arquitectura objetivo se distingue del alcance implementado.

## Archivos y comparación visual

- [C4 Chimchar](../../chimchar/reto5-c4/contexto-chimchar.svg).
- [C4 Monferno](../../monferno/reto5-c4/contexto-monferno.svg).
- [Enterprise Nivel 1 SVG](contexto-enterprise.svg) · [Draw.io editable](contexto-enterprise.drawio).
- [Enterprise Nivel 2 SVG](contenedores-enterprise.svg) · [Draw.io editable](contenedores-enterprise.drawio).

Los Draw.io son mxfile/diagram/mxGraphModel con nodos y relaciones editables. Sus entidades, textos y direcciones se generan desde los mismos datos de los SVG.
