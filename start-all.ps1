param(
    [int]$SpringPort = 8081,
    [int]$PythonPort = 8000,
    [int]$MySqlPort = 3306,
    [string]$MySqlContainer = "gamesup-mysql",
    [string]$MySqlRootPassword = "root",
    [string]$DatabaseName = "GamesUP"
)

$ErrorActionPreference = "Stop"

$RootDir = $PSScriptRoot
$PythonDir = Join-Path $RootDir "CodeApiPython"
$SpringDir = Join-Path $RootDir "gamesUP"
$LogDir = Join-Path $RootDir "logs"

New-Item -ItemType Directory -Path $LogDir -Force | Out-Null

function Test-PortOpen {
    param([int]$Port)

    $connection = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
    return $null -ne $connection
}

function Wait-Http {
    param(
        [string]$Url,
        [int]$TimeoutSeconds = 60
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    do {
        try {
            Invoke-RestMethod -Uri $Url -TimeoutSec 3 | Out-Null
            return $true
        }
        catch {
            Start-Sleep -Seconds 2
        }
    } while ((Get-Date) -lt $deadline)

    return $false
}

function Wait-MySql {
    param([int]$TimeoutSeconds = 90)

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    do {
        try {
            docker exec -e "MYSQL_PWD=$MySqlRootPassword" $MySqlContainer mysqladmin ping -uroot --silent | Out-Null
            if ($LASTEXITCODE -eq 0) {
                return $true
            }
        }
        catch {
            Start-Sleep -Seconds 2
        }
    } while ((Get-Date) -lt $deadline)

    return $false
}

Write-Host "== GamesUP startup =="

if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    throw "Docker is not available. Start Docker Desktop or install Docker."
}

$existingContainer = docker ps -a --filter "name=^/$MySqlContainer$" --format "{{.Names}}"
if (-not $existingContainer) {
    Write-Host "Creating MySQL container '$MySqlContainer'..."
    docker run --name $MySqlContainer `
        -e "MYSQL_ROOT_PASSWORD=$MySqlRootPassword" `
        -e "MYSQL_DATABASE=$DatabaseName" `
        -p "${MySqlPort}:3306" `
        -d mysql:8.4 | Out-Null
}
else {
    Write-Host "Starting MySQL container '$MySqlContainer'..."
    docker start $MySqlContainer | Out-Null
}

Write-Host "Waiting for MySQL..."
if (-not (Wait-MySql)) {
    throw "MySQL did not become ready. Check: docker logs $MySqlContainer"
}

if (-not (Test-PortOpen -Port $PythonPort)) {
    Write-Host "Starting Python API on port $PythonPort..."
    Start-Process -WindowStyle Hidden `
        -FilePath "python" `
        -ArgumentList @("-m", "uvicorn", "main:app", "--host", "127.0.0.1", "--port", "$PythonPort") `
        -WorkingDirectory $PythonDir `
        -RedirectStandardOutput (Join-Path $LogDir "python-api.out.log") `
        -RedirectStandardError (Join-Path $LogDir "python-api.err.log") | Out-Null
}
else {
    Write-Host "Python port $PythonPort already listens; keeping existing process."
}

if (-not (Wait-Http -Url "http://localhost:$PythonPort/health" -TimeoutSeconds 45)) {
    throw "Python API did not answer on http://localhost:$PythonPort/health. Check logs/python-api.err.log"
}

if (-not (Test-PortOpen -Port $SpringPort)) {
    Write-Host "Starting Spring API on port $SpringPort..."
    $env:SERVER_PORT = "$SpringPort"
    $env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:$MySqlPort/$DatabaseName"
    $env:SPRING_DATASOURCE_USERNAME = "root"
    $env:SPRING_DATASOURCE_PASSWORD = $MySqlRootPassword
    $env:SPRING_DATASOURCE_DRIVER = "com.mysql.cj.jdbc.Driver"
    $env:JPA_DDL_AUTO = "update"
    $env:RECOMMENDATION_API_URL = "http://localhost:$PythonPort"

    Start-Process -WindowStyle Hidden `
        -FilePath (Join-Path $SpringDir "mvnw.cmd") `
        -ArgumentList @("spring-boot:run") `
        -WorkingDirectory $SpringDir `
        -RedirectStandardOutput (Join-Path $LogDir "spring-api.out.log") `
        -RedirectStandardError (Join-Path $LogDir "spring-api.err.log") | Out-Null
}
else {
    Write-Host "Spring port $SpringPort already listens; keeping existing process."
}

if (-not (Wait-Http -Url "http://localhost:$SpringPort/api/health" -TimeoutSeconds 90)) {
    throw "Spring API did not answer on http://localhost:$SpringPort/api/health. Check logs/spring-api.err.log"
}

Write-Host ""
Write-Host "Ready:"
Write-Host "- Python API: http://localhost:$PythonPort"
Write-Host "- Spring API: http://localhost:$SpringPort"
Write-Host "- MySQL: localhost:$MySqlPort / database $DatabaseName / root:$MySqlRootPassword"
Write-Host ""
Write-Host "Postman variables:"
Write-Host "- springBaseUrl = http://localhost:$SpringPort"
Write-Host "- pythonBaseUrl = http://localhost:$PythonPort"
