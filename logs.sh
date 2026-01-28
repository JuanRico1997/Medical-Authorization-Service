#!/bin/bash

# Si se pasa un argumento, mostrar logs de ese servicio específico
if [ -n "$1" ]; then
    echo "📝 Mostrando logs de: $1"
    docker-compose logs -f $1
else
    echo "📝 Mostrando logs de todos los servicios..."
    docker-compose logs -f
fi