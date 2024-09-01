#!/bin/bash

# Definir variables
JAR_PATH="./target/almacenes-0.0.1-SNAPSHOT.jar --spring.profiles.active=production"
LOG_PATH="./almacenes.log"

# Comando para ejecutar la aplicación
start_app() {
    echo "Iniciando aplicación Almacenes..."
    nohup java -jar $JAR_PATH >> $LOG_PATH 2>&1 &
    echo "Aplicación iniciada con PID $!"
}

# Función para detener la aplicación (opcional)
stop_app() {
    echo "Deteniendo aplicación Almacenes..."
    pkill -f $JAR_PATH
    echo "Aplicación detenida."
}

# Verifica si la aplicación ya está corriendo
if pgrep -f $JAR_PATH > /dev/null
then
#    echo "La aplicación ya está corriendo."
    stop_app
    start_app
else
    start_app
fi