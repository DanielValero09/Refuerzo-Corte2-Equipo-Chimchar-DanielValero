# Reto 14 - SonarQube

## Estado real

**PENDIENTE TÉCNICO. Análisis SonarQube pendiente por limitación del entorno.**

El servidor sí se ejecutó y está disponible. La limitación es la autenticación de la instancia existente: las credenciales iniciales indicadas por el HTML no son válidas, no hay `SONAR_TOKEN` en el proceso y no se dispone de una sesión de navegador accesible mediante las herramientas conectadas. Se solicitó habilitar acceso local mientras continuaba la auditoría. No se cambió ninguna contraseña ni configuración de seguridad y no se creó otro contenedor.

No se obtuvo un análisis completado ni un listado de issues de SkyCampus. No se inventan métricas, code smells corregidos ni capturas antes/después.

## Entorno y comandos intentados

- Docker: `29.8.0`, build `88096ef`.
- Contenedor existente: `sonarqube`, imagen `sonarqube:26.9.0.129388-community`.
- SonarQube Community Build: `26.9.0.129388`.
- Maven SonarScanner: `5.5.0.6356`.
- Host local: `http://localhost:9000`.
- Proyecto previsto: `skycampus-chimchar`.

```powershell
docker --version
docker ps
docker ps -a
docker start sonarqube
Invoke-RestMethod http://localhost:9000/api/system/status
Invoke-RestMethod http://localhost:9000/api/server/version
```

Se comprobó `api/authentication/validate` con la autenticación inicial del material, sin almacenar ni imprimir credenciales. Respuesta real: `{"valid":false}`. Se verificó que `SONAR_TOKEN` y `SONAR_LOGIN` no estaban presentes.

Intento real del scanner, después de generar el reporte JaCoCo:

```powershell
mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356:sonar `
  "-Dsonar.projectKey=skycampus-chimchar" `
  "-Dsonar.projectName=SkyCampus Chimchar" `
  "-Dsonar.host.url=http://localhost:9000" `
  "-Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
```

## Evidencia textual real

Estado del servidor:

```json
{"id":"147B411E-AaCx5FxMB0pnj5OfwfCm","version":"26.9.0.129388","status":"UP"}
```

Resultado del scanner, código de salida 1:

```text
[ERROR] Failed to query server version: GET http://localhost:9000/api/v2/analysis/version failed with HTTP 401. Please check the property sonar.token or the environment variable SONAR_TOKEN.
[INFO] BUILD FAILURE
[INFO] Finished at: 2026-10-07T16:19:36-05:00
```

## Métricas antes y después

N/D significa no disponible porque el scanner no pudo autenticarse; no equivale a cero ni a un Quality Gate aprobado.

| Métrica | Antes | Después |
| --- | --- | --- |
| Quality Gate | N/D | N/D |
| Bugs | N/D | N/D |
| Vulnerabilities | N/D | N/D |
| Security Hotspots | N/D | N/D |
| Code Smells | N/D | N/D |
| Technical Debt | N/D | N/D |
| Duplications | N/D | N/D |
| Coverage reportado por Sonar | N/D | N/D |

La cobertura JaCoCo está medida separadamente en el [Reto 13](../reto13-jacoco.md); no se presenta como una métrica importada por Sonar.

## Issues y capturas

- Issues recuperados: ninguno, por falta de análisis autenticado; no significa que el proyecto tenga cero issues.
- Code smells corregidos y verificados por Sonar: ninguno.
- `sonar-antes.png` y `sonar-despues.png`: pendientes de una ejecución autenticada real; no se crearon imágenes sustitutivas.

## Comandos pendientes para finalizar

El propietario debe proporcionar acceso autorizado a esta instancia mediante un token válido. El token se introduce de forma oculta y se utiliza solo en el entorno temporal del proceso, sin guardarlo en el repositorio ni imprimirlo:

```powershell
mvn clean test jacoco:report
$taskToken = Read-Host 'Token temporal SonarQube' -AsSecureString
$taskPointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($taskToken)
try {
    $env:SONAR_TOKEN = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($taskPointer)
    mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356:sonar `
      "-Dsonar.projectKey=skycampus-chimchar" `
      "-Dsonar.projectName=SkyCampus Chimchar" `
      "-Dsonar.host.url=http://localhost:9000" `
      "-Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml" `
      "-Dsonar.qualitygate.wait=true"
    if ($LASTEXITCODE -ne 0) { throw 'Revisar el error del scanner o Quality Gate.' }
    $taskHeaders = @{ Authorization = 'Bearer ' + $env:SONAR_TOKEN }
    Invoke-RestMethod -Headers $taskHeaders -Uri 'http://localhost:9000/api/qualitygates/project_status?projectKey=skycampus-chimchar'
    Invoke-RestMethod -Headers $taskHeaders -Uri 'http://localhost:9000/api/measures/component?component=skycampus-chimchar&metricKeys=bugs,vulnerabilities,security_hotspots,code_smells,sqale_index,duplicated_lines_density,coverage'
    Invoke-RestMethod -Headers $taskHeaders -Uri 'http://localhost:9000/api/issues/search?componentKeys=skycampus-chimchar&resolved=false&ps=500'
} finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($taskPointer)
    Remove-Item Env:SONAR_TOKEN -ErrorAction SilentlyContinue
    $taskHeaders = $null
    $taskToken = $null
}
```

Después del primer análisis: conservar métricas y captura reales, corregir Bugs, Vulnerabilities, hotspots relevantes y Code Smells en ese orden; documentar regla, archivo, problema y corrección. Corregir al menos tres code smells si existen tres, intentar resolver todos y ejecutar las pruebas después de las correcciones importantes. Repetir el análisis, capturar el resultado y completar la tabla sin ocultar issues restantes. Mantener exclusivamente la rama `evolution/chimchar`.

## Fuentes

HTML oficial `DOSW_Equipo_Chimchar_fixed.html`, apartados 13 y 14 de Chimchar. El uso del scanner Maven y de `SONAR_TOKEN` como variable de entorno sigue la [documentación oficial de SonarSource](https://docs.sonarsource.com/sonarqube-community-build/analyzing-source-code/scanners/sonarscanner-for-maven).
