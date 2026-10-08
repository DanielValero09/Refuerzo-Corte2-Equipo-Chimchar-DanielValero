# Monferno Reto 9 - Agilismo y Jira

## Planificación real

Consulta de solo lectura realizada el 2026-10-08 mediante el conector Atlassian; no se crearon ni modificaron issues, criterios ni sprints.

Proyecto: SCRUM — SkyCampus Chimchar. Épica: [SCRUM-13](https://sky-campus-chimchar.atlassian.net/browse/SCRUM-13), **SkyCampus v2 — Flota autónoma con prioridades**.

Sprint: **Sprint 1 — SkyCampus v2**, ID **3**, board **1**, estado **future**. La capacidad académica declarada es 20 story points; no se presenta como velocidad histórica medida.

| Key | Historia | Points | Sprint | ¿Cabe? |
| --- | --- | ---: | --- | --- |
| [SCRUM-14](https://sky-campus-chimchar.atlassian.net/browse/SCRUM-14) | HU-V2-01 — Asignar automáticamente drone a misión | 5 | Sprint 1 | Sí |
| [SCRUM-15](https://sky-campus-chimchar.atlassian.net/browse/SCRUM-15) | HU-V2-02 — Priorizar misiones urgentes con drone EXPRESS | 5 | Sprint 1 | Sí |
| [SCRUM-16](https://sky-campus-chimchar.atlassian.net/browse/SCRUM-16) | HU-V2-03 — Validar clima antes de asignar una misión | 3 | Sprint 1 | Sí |
| [SCRUM-17](https://sky-campus-chimchar.atlassian.net/browse/SCRUM-17) | HU-V2-04 — Alertar al técnico cuando un drone entra en FALLO | 3 | Sprint 1 | Sí |
| [SCRUM-18](https://sky-campus-chimchar.atlassian.net/browse/SCRUM-18) | HU-V2-05 — Registrar reparación y rehabilitar drone | 3 | Sprint 1 | Sí |

**Total: 19 / 20 points. Capacidad restante: 1 point.** Las cinco historias caben en conjunto sin superar 20 puntos; todas están vinculadas a SCRUM-13 y al sprint 3, según los campos reales consultados.

## Criterios Gherkin reales

Se transcriben los dos criterios existentes por HU, conservando su redacción. Total: **10 criterios**.

### SCRUM-14 — HU-V2-01 — Asignar automáticamente drone a misión

**CA-01**

```gherkin
DADO QUE existen drones disponibles con batería >=30% y capacidad compatible
CUANDO se procesa una misión NORMAL
ENTONCES el sistema selecciona automáticamente un candidato válido
Y prioriza la política estándar de mayor batería.
```

**CA-02**

```gherkin
DADO QUE no existe ningún drone apto
CUANDO el sistema intenta asignar la misión
ENTONCES la misión permanece PENDIENTE
Y se informa al operador que no hay candidato disponible.
```

### SCRUM-15 — HU-V2-02 — Priorizar misiones urgentes con drone EXPRESS

**CA-01**

```gherkin
DADO QUE existe un drone EXPRESS disponible, compatible y con batería >=30%
Y la misión es URGENTE
CUANDO se ejecuta la asignación
ENTONCES el sistema selecciona el EXPRESS aunque otro drone tenga mayor batería.
```

**CA-02**

```gherkin
DADO QUE no existe un EXPRESS apto
CUANDO se procesa una misión URGENTE
ENTONCES el sistema considera otro tipo compatible según la política definida
Y nunca ignora batería, disponibilidad ni capacidad.
```

### SCRUM-16 — HU-V2-03 — Validar clima antes de asignar una misión

**CA-01**

```gherkin
DADO QUE la API meteorológica reporta condiciones aptas
CUANDO se intenta asignar la misión
ENTONCES el flujo puede continuar con la selección del drone.
```

**CA-02**

```gherkin
DADO QUE la API reporta clima adverso o no responde dentro del límite definido
CUANDO se intenta iniciar la asignación
ENTONCES SkyCampus bloquea el vuelo de forma fail-safe
Y comunica la causa al operador.
```

### SCRUM-17 — HU-V2-04 — Alertar al técnico cuando un drone entra en FALLO

**CA-01**

```gherkin
DADO QUE un drone cambia su estado a FALLO
CUANDO GestorFlota publica el cambio
ENTONCES PanelOperador, SistemaLog y AlertaTecnico reciben la notificación correspondiente.
```

**CA-02**

```gherkin
DADO QUE un drone cambia a un estado distinto de FALLO
CUANDO se notifican los observadores
ENTONCES AlertaTecnico no genera una alerta de mantenimiento por fallo.
```

### SCRUM-18 — HU-V2-05 — Registrar reparación y rehabilitar drone

**CA-01**

```gherkin
DADO QUE un drone permanece en FALLO o MANTENIMIENTO
CUANDO el técnico registra su diagnóstico
ENTONCES el drone continúa excluido de la asignación automática.
```

**CA-02**

```gherkin
DADO QUE la reparación fue registrada correctamente
CUANDO el técnico cambia el estado del drone a DISPONIBLE
ENTONCES el drone vuelve a ser elegible para asignación si cumple batería y capacidad.
```

## Definition of Done registrado

Transcripción de la sección de SCRUM-13:

* Código revisado en PR por al menos otro miembro.
* Pruebas unitarias con cobertura JaCoCo >=80%.
* SonarQube: 0 bugs y 0 vulnerabilidades.
* Todos los criterios Gherkin de las historias pasan.
* Integración mediante flujo GitFlow correcto.
* Diagrama de contexto y plantilla DOSW actualizados si cambia un RF.

Estos son criterios del cierre final: no se afirma que ya se cumplan PR, cobertura o SonarQube. Este bloque no crea PR ni integra a develop; tampoco demuestra aún todos los criterios funcionales (por ejemplo timeout meteorológico o registro persistente de reparación).

## Evidencia real de Jira

**Jira configurado realmente; captura visual pendiente de incorporación manual.**

El conector autenticado confirmó la épica, las cinco HU, points, parentesco, pertenencia al sprint y los diez Gherkin. El navegador conectado no expuso sesiones y rechazó abrir Chrome con “Browser is not available: chrome”; no fue posible obtener una captura autenticada real. No se creó una imagen simulada.

Para incorporar la evidencia manual: abrir el backlog del board 1, desplegar Sprint 1 — SkyCampus v2 (ID 3) y capturar sus cinco historias. Guardar la captura auténtica como `sprint1-jira.png` en esta carpeta; el archivo no existe todavía y no se enlaza como evidencia disponible.

Fuente académica: HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, Monferno apartado 09. Fuente factual de la planificación: issues SCRUM-13 a SCRUM-18 y metadatos reales del sprint 3.
