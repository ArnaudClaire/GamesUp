param(
    [int]$SpringPort = 8081,
    [int]$PythonPort = 8000,
    [string]$MySqlContainer = "gamesup-mysql",
    [switch]$KeepMySql
)

$ErrorActionPreference = "Continue"

function Stop-ProcessOnPort {
    param([int]$Port)

    $connections = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
    foreach ($connection in $connections) {
        $processId = $connection.OwningProcess
        if ($processId -and $processId -ne 0) {
            $process = Get-Process -Id $processId -ErrorAction SilentlyContinue
            if ($process) {
                Write-Host "Stopping process $($process.ProcessName) ($processId) on port $Port..."
                Stop-Process -Id $processId -Force
            }
        }
    }
}

Write-Host "== GamesUP shutdown =="

Stop-ProcessOnPort -Port $SpringPort
Stop-ProcessOnPort -Port $PythonPort

if (-not $KeepMySql) {
    if (Get-Command docker -ErrorAction SilentlyContinue) {
        $runningContainer = docker ps --filter "name=^/$MySqlContainer$" --format "{{.Names}}"
        if ($runningContainer) {
            Write-Host "Stopping MySQL container '$MySqlContainer'..."
            docker stop $MySqlContainer | Out-Null
        }
    }
}

Write-Host "Done."
