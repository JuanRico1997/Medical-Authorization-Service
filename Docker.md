# 🐳 MediTrack - Docker Setup

## Requisitos

- Docker 20.10+
- Docker Compose 2.0+
- 4GB RAM mínimo
- 10GB espacio en disco

## Estructura
```
meditrack-system/
├── authorization-service/
│   ├── Dockerfile
│   └── .dockerignore
├── insurance-validation-mock-service/
│   ├── Dockerfile
│   └── .dockerignore
├── docker-compose.yml
├── start.sh
├── stop.sh
└── logs.sh
```

## Servicios

### 1. MySQL Database (Puerto 3306)
- Imagen: `mysql:8.0`
- Base de datos: `meditrack_db`
- Usuario: `meditrack`
- Contraseña: `meditrack_password`

### 2. Insurance Mock Service (Puerto 8081)
- Spring Boot microservice
- Valida coberturas de seguros
- Healthcheck: `/api/insurance/health`

### 3. Authorization Service (Puerto 8080)
- Spring Boot microservice principal
- Gestión de pacientes y autorizaciones
- Healthcheck: `/actuator/health`
- Swagger: `/swagger-ui.html`

## Inicio Rápido

### Opción 1: Usando scripts (Recomendado)
```bash
# Iniciar todo el sistema
./start.sh

# Ver logs
./logs.sh

# Ver logs de un servicio específico
./logs.sh authorization-service

# Detener todo
./stop.sh
```

### Opción 2: Comandos Docker Compose
```bash
# Construir imágenes
docker-compose build

# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes (borra la BD)
docker-compose down -v
```

## Verificar que todo funciona

### 1. Verificar estado de los contenedores
```bash
docker-compose ps
```

Deberías ver 3 servicios en estado `Up (healthy)`.

### 2. Probar los servicios
```bash
# Authorization Service Health
curl http://localhost:8080/actuator/health

# Insurance Service Health
curl http://localhost:8081/api/insurance/health

# Swagger UI
open http://localhost:8080/swagger-ui.html
```

## Troubleshooting

### Los servicios no inician
```bash
# Ver logs de todos los servicios
docker-compose logs

# Ver logs de un servicio específico
docker-compose logs authorization-service
```

### MySQL no está listo

Espera 30-60 segundos después de iniciar. El Authorization Service esperará a que MySQL esté saludable.

### Reconstruir imágenes
```bash
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### Limpiar todo y empezar de cero
```bash
docker-compose down -v
docker system prune -a
./start.sh
```

## Variables de Entorno

Puedes personalizar las variables en `docker-compose.yml`:
```yaml
environment:
  - SPRING_PROFILES_ACTIVE=docker
  - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/meditrack_db
  - SPRING_DATASOURCE_USERNAME=meditrack
  - SPRING_DATASOURCE_PASSWORD=meditrack_password
```

## Comandos Útiles
```bash
# Ver contenedores corriendo
docker ps

# Entrar a un contenedor
docker exec -it meditrack-authorization-service sh

# Ver uso de recursos
docker stats

# Reiniciar un servicio específico
docker-compose restart authorization-service

# Escalar un servicio (crear múltiples instancias)
docker-compose up -d --scale insurance-service=2
```

## Producción

Para producción, considera:

1. Usar variables de entorno desde archivo `.env`
2. Cambiar contraseñas por defecto
3. Configurar HTTPS
4. Usar Docker Secrets para información sensible
5. Configurar límites de recursos
6. Agregar logging centralizado
7. Configurar backups automáticos de la BD