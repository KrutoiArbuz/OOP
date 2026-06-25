@echo off
call gradlew.bat -q classes

set CP=build\classes\java\main

start "worker-5001" java -cp %CP% ru.nsu.masolygin.worker.Worker 5001
start "worker-5002" java -cp %CP% ru.nsu.masolygin.worker.Worker 5002
start "worker-5003" java -cp %CP% ru.nsu.masolygin.worker.Worker 5003

timeout /t 2 >nul

java -cp %CP% ru.nsu.masolygin.Main

pause
