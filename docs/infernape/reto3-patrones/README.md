# Infernape Reto 3 - Cuatro patrones combinados

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, Infernape apartado 03. El reto concreto pide Composite, Strategy, Observer y Factory Method; no se implementan familias de reportes Abstract Factory que no necesita este escenario.

## Composite — Rutas multi-etapa

- Problema: un cliente debe sumar y ejecutar tanto una etapa como una ruta de rutas anidadas.
- Patrón/participantes: Ruta (Component), RutaSimple (Leaf), RutaCompuesta (Composite) y EtapaRuta (dato del dominio).
- Por qué aplica: las hojas y los composites exponen distanciaKm/etapas por la misma interfaz, sin instanceof. RutaCompuesta conserva orden y verifica continuidad; su lista es inmutable.
- Alternativa descartada: una lista plana basta si solo existen dos niveles; aquí la prueba anida una ruta ECI → carga → UNAL dentro de otra hasta UNIANDES. Herencia con ramas por tipo complicaría el cliente.
- Prueba: RutasYStrategyTest.compositeAnidadoConservaOrdenDistanciaYRecargas.
- [Diagrama Composite](composite.svg).

EstacionCarga contiene ID, nombre/ubicación descriptiva y disponibilidad. PuntoRuta distingue campus y carga mediante identidad prefijada; mismoLugar no depende de snapshots de disponibilidad. Recargas cuenta llegadas a una estación, no valores inventados de autonomía.

## Strategy — Optimización

- Problema: menor distancia y menor número de recargas pueden elegir recorridos diferentes.
- Patrón/participantes: EstrategiaOptimizacionRuta, MenorDistanciaStrategy, MenorNumeroRecargasStrategy y PlanificadorRuta.
- Por qué aplica: PlanificadorRuta recibe el contrato por constructor; usar otra política solo requiere inyectarla en otro planificador, sin editar su código ni inspeccionar la clase.
- Alternativa descartada: un switch por nombre de algoritmo obliga a modificar el gestor con cada criterio nuevo; una factory de familias completas sería excesiva para una sola selección.
- Regla: menor distancia desempata por recargas y clave; menor recargas desempata por distancia y clave. La clave describe el recorrido de IDs; ausencia de candidatas devuelve Optional.empty.
- Pruebas: mismaInterfazAdmiteOptimizacionesDiferentes, empateEsDeterministaYNoDependeDelOrdenDeCandidatos y ausenciaDeRutasNoDevuelveNull.
- [Diagrama Strategy](strategy.svg).

## Observer — Eventos de etapa

- Problema: monitor e historial necesitan cambios de etapa sin acoplar sus responsabilidades al publicador.
- Patrón/participantes: ObservadorEtapa, GestorEjecucionRuta, MonitorRed, RegistroEventos y EventoEtapa.
- Por qué aplica: el gestor solo conoce el contrato y publica INICIADA, EN_CARGA, COMPLETADA o FALLIDA; las vistas conservan datos independientes.
- Alternativa descartada: llamadas directas a cada receptor impiden extender suscriptores; un singleton con flota mutable agrega estado global innecesario.
- Prueba: tercerObservadorLambdaSeAgregaSinModificarElGestor. También se comprueban duplicados, desuscripción e historial inmutable.
- [Diagrama Observer](observer.svg).

Publicar describe un evento académico solicitado por el cliente; no demuestra hardware, consumo de batería ni una máquina de estados de vuelo real.

## Factory Method — Drone de una etapa

- Problema: variar el perfil del producto sin un switch central que crezca.
- Patrón/participantes: CreadorDroneEtapa (Creator), crearDrone abstracto (Factory Method), CreadorMini/CreadorExpress/CreadorCargo (Concrete Creators) y DroneEnterprise (Product).
- Por qué aplica: cada creador sobreescribe el método que construye un DroneEnterprise; el template crear verifica batería y capacidad y devuelve Optional.
- Alternativa descartada: Abstract Factory sirve a familias de varios productos coherentes; aquí solo se crea un drone. Una factory con switch exige editar el mismo bloque para cada nuevo perfil.
- Pruebas: FactoryMethodTest, siete casos de perfiles conocidos, batería 30/29, capacidad y CARGO <100 g.
- [Diagrama Factory Method](factory-method.svg).

Los creadores conocidos no inventan nombres para los dos tipos Enterprise no nombrados por el enunciado. PerfilDrone admite datos adicionales y el creador concreto decide el perfil; no se afirma una selección automática por distancia/autonomía no modelada.

## Escenario combinado

ObserverYCombinacionTest.estrategiaCompositeFactoryYObserverSeCombinanSinGodObject: Strategy elige la ruta de 6 km vía carga frente a una directa de 10 km; Composite proporciona sus dos etapas; CreadorExpress genera un drone apto para la primera etapa de 300 g; GestorEjecucionRuta notifica INICIADA a monitor e historial. El test coordina piezas pequeñas; ninguna clase central concentra los cuatro algoritmos.

Distancias ilustrativas y perfiles heredados; sin HTTP, hardware ni BD. [Índice Enterprise](../README.md).
