#!/bin/bash

echo "🚀 Iniciando MediTrack System..."
echo ""

# Detener contenedores existentes
echo "🛑 Deteniendo contenedores existentes..."
docker-compose down

# Limpiar volúmenes (opcional, comentar si no quieres borrar la BD)
# docker volume rm meditrack-mysql-data

# Construir imágenes
echo "🔨 Construyendo imágenes Docker..."
docker-compose build --no-cache

# Iniciar servicios
echo "🚀 Iniciando servicios..."
docker-compose up -d

# Esperar a que los servicios estén listos
echo ""
echo "⏳ Esperando a que los servicios estén listos..."
sleep 30

# Verificar estado
echo ""
echo "✅ Verificando estado de los servicios..."
docker-compose ps

echo ""
echo "🎉 MediTrack System está listo!"
echo ""
echo "📊 Servicios disponibles:"
echo "   - Authorization Service: http://localhost:8080"
echo "   - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   - Insurance Mock Service: http://localhost:8081"
echo "   - Actuator Health: http://localhost:8080/actuator/health"
echo ""
echo "📝 Para ver logs en tiempo real:"
echo "   docker-compose logs -f"
echo ""
echo "🛑 Para detener el sistema:"
echo "   docker-compose down"