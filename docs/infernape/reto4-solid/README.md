# Infernape Reto 4 - SOLID y arquitectura de tres capas

## Capas y contratos

| Capa | Clases/contratos | Responsabilidad |
| --- | --- | --- |
| Dominio | Sede, PerfilDrone, DroneEnterprise, MisionEnterprise, SolicitudAsignacion, EtapaRuta, EstacionCarga y puertos | Datos y reglas independientes de framework/red/persistencia |
| Aplicación | AsignadorMisionEnterprise, AsignacionMayorBateria | Coordinar asignación e implementar una política inyectable |
| Infraestructura | RepositorioFlotaMemoria, ServicioClimaConfigurable | Adaptar almacenamiento/clima local sin cambiar el caso de uso |

Puertos del dominio: RepositorioFlota.findDisponibles(Sede), ServicioClima.condicionesAptas(origen,destino), EstrategiaAsignacionEnterprise.seleccionar(flota,solicitud) y ObservadorAsignacion.onAsignada(solicitud,drone). Listas y Optional son contratos no nulos.

AsignadorMisionEnterprise recibe los cuatro por constructor y no instancia repositorios, clientes HTTP, contextos o service locators. Comprueba clima, consulta el origen, delega selección y valida pertenencia a la flota, sede y aptitud. Devuelve una copia DroneEnterprise no disponible y notifica exactamente una vez; no muta el repositorio ni afirma persistencia de la misión. El consumidor debe conservar el resultado operativo.

## SOLID aplicado

| Principio | Solución concreta |
| --- | --- |
| SRP | Records/reglas, coordinación, algoritmos y adaptadores tienen razones de cambio separadas. |
| OCP | Strategy, Factory Method y Observer agregan comportamientos mediante implementaciones nuevas, sin switches por clase ni modificar el caso de uso. |
| LSP | Adaptadores/políticas deben preservar listas y Optional no nulos, sede solicitada y restricciones de aptitud; la aplicación valida el candidato antes de notificar. No se inventa una violación histórica. |
| ISP | Cuatro puertos pequeños con una operación cada uno; clima no expone persistencia ni notificación. |
| DIP | La aplicación depende de interfaces del dominio; infraestructura implementa esos contratos y puede sustituirse mediante constructor injection. |

## Pruebas

AsignadorMisionEnterpriseTest usa Mockito para repositorio, clima, estrategia y observador. Sus seis casos demuestran selección inyectada de un candidato con menos batería, consulta de la sede correcta y aviso único; clima adverso sin consultar flota; sin candidato; candidato ajeno al repositorio; sede equivocada y batería insuficiente. Todos AAA y con verify/verifyNoInteractions relevantes, sin BD ni HTTP reales.

AdaptadoresLocalesTest comprueba filtrado por sede/disponibilidad, snapshot inmutable, IDs duplicados y ejecución integrada con datos locales, además de capacidad y desempate de la política.

ArquitecturaDominioTest recorre los fuentes y verifica que dominio solo importe JDK/dominio propio, sin Spring, persistencia, red, SQL, Mockito u otras librerías; verifica también que aplicación no importe adaptadores concretos. No se agregó ArchUnit.

## Diagrama y alcance

[Arquitectura de tres capas](arquitectura-3-capas.svg). Las dependencias de aplicación e infraestructura apuntan a los contratos del dominio; los patrones del Reto 3 son ejemplos puros JDK, sin dependencias externas del dominio.

En producción podrían añadirse adaptadores HTTP/JPA para estos puertos; este ejercicio conserva implementaciones locales. Aerocivil permanece como contexto Enterprise, sin puerto adicional innecesario ni autorización real. La regla de batería 30% y las capacidades conocidas se conservan de Monferno; no se inventan capacidades de los dos perfiles no especificados.

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, Infernape apartado 04. [Índice](../README.md).
