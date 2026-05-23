#!/usr/bin/env bash
cd "$(dirname "$0")"

./gradlew -q classes

CP=build/classes/java/main

java -cp $CP ru.nsu.masolygin.worker.Worker 5001 &
java -cp $CP ru.nsu.masolygin.worker.Worker 5002 &
java -cp $CP ru.nsu.masolygin.worker.Worker 5003 &

sleep 2

java -cp $CP ru.nsu.masolygin.Main

kill %1 %2 %3 2>/dev/null
