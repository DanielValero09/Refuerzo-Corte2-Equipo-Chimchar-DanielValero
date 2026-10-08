# Monferno Reto 4 - SOLID

Fuente: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartado 04. La comparación parte del GestorDrone monolítico del material; Chimchar ya lo había separado en el Reto 4, y Monferno mantiene ese trabajo mediante packages propios.

| Principio | Diseño monolítico original: violación o limitación | Corrección en v2 | Evidencia concreta |
| --- | --- | --- | --- |
| SRP | GestorDrone asignaba, persistía, alertaba, reportaba y calculaba rutas: varias razones para cambiar. | Estadísticas, selección, validaciones, actualización de flota y recepción de avisos están separadas. La fachada AsignadorMision coordina la asignación y su notificación, delegando las políticas y el destino del aviso. | EstadisticasMisiones, GestorMisiones, ValidadorBateria/Disponibilidad/Carga, GestorFlota, PanelOperador, SistemaLog y AlertaTecnico. |
| OCP | Un if/else de rutas o criterios obligaba a editar el gestor al aparecer otro algoritmo. | Strategy → OCP: se inyecta otra EstrategiaAsignacion sin editar GestorMisiones. Chain/validadores → OCP + SRP: cada regla es pequeña y la cadena acepta nuevas implementaciones. | Tres Strategy, interfaz EstrategiaAsignacion, CadenaValidacion y prueba de un validador local adicional. |
| DIP | El gestor dependía directamente de DriverManager/MySQL y concentraba detalles concretos. | GestorMisiones depende de EstrategiaAsignacion; Observer → DIP: GestorFlota depende de ObservadorDrone, sin conocer sus tres clases concretas. La notificación de asignación se inyecta por Consumer<Mision>. | Constructores de GestorMisiones/AsignadorMision; suscripción de ObservadorDrone en GestorFlota. |
| ISP | Un consumidor que solo necesitaba asignar quedaba expuesto a persistencia, reportes y alertas; limitación potencial del diseño amplio. | Contratos pequeños con una operación por responsabilidad. | EstrategiaAsignacion, ObservadorDrone y ValidadorDrone. |
| LSP | No hay una violación directa demostrada: el fragmento original no presenta una jerarquía de herencia. | No se inventa una violación; las estrategias cumplen el mismo contrato de selección y los observadores el de recepción. | Pruebas de las tres estrategias y del cuarto observador. |

## Prueba obligatoria de intercambiabilidad

`monferno.reto4.GestorMisionesStrategyTest.mismaInstanciaFuncionaConLasTresEstrategiasSinCambiarSuCodigo` utiliza el mismo GestorMisiones y la misma misión de 100 g:

- MayorBateriaStrategy elige D-01, CARGO con 95%.
- MenorUsoAcumuladoStrategy elige D-02, EXPRESS con una misión completada.
- TipoCompatibleCargaStrategy elige D-03, MINI con la menor capacidad suficiente.

Se cambia la implementación mediante el contrato, sin modificar GestorMisiones ni inspeccionar tipos. Otra prueba pasa una implementación local de EstrategiaAsignacion y comprueba su resultado. Una tercera agrega un validador local a la cadena sin editar estrategias ni gestor.

## Extensión de Observer

`cuartoObservadorLambdaSeNotificaSinModificarGestorFlota` agrega un cuarto suscriptor y verifica el evento recibido. GestorFlota no importa PanelOperador, SistemaLog ni AlertaTecnico: depende exclusivamente de ObservadorDrone.

Las 34 pruebas Chimchar se conservan. No se incorpora persistencia, Mockito, consultas meteorológicas ni control externo en este bloque.
