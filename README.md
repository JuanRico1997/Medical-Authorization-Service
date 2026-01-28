# 🏥 MediTrack - Sistema de Gestión de Autorizaciones Médicas

Sistema backend completo para la gestión de autorizaciones médicas con validación automática de coberturas de seguros.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [API Endpoints](#-api-endpoints)
- [Base de Datos](#-base-de-datos)
- [Docker](#-docker)
- [Testing](#-testing)
- [Autor](#-autor)

---

## 🎯 Descripción

**MediTrack** es un sistema enterprise para la gestión de autorizaciones médicas que integra validación automática de coberturas con aseguradoras. El sistema permite a hospitales y clínicas gestionar solicitudes de autorizaciones médicas, calcular copagos automáticamente según el plan de salud del paciente, y mantener un registro completo de todas las transacciones.

### Problema que resuelve

Los hospitales enfrentan demoras significativas al validar manualmente las coberturas de seguros médicos para cada procedimiento. MediTrack automatiza este proceso, reduciendo tiempos de espera y mejorando la experiencia del paciente.

### Casos de uso principales

1. **Médicos y Administradores**: Crear y gestionar autorizaciones médicas
2. **Sistema**: Validar automáticamente coberturas con aseguradoras
3. **Pacientes**: Consultar sus autorizaciones y copagos
4. **Administradores**: Supervisar y aprobar casos especiales

---

## ✨ Características

### Funcionales

- ✅ **Autenticación y Autorización** con JWT
- ✅ **Gestión de Pacientes** con creación automática de usuarios
- ✅ **Autorizaciones Médicas** con workflow completo
- ✅ **Validación de Seguros** mediante integración con servicio externo
- ✅ **Cálculo Automático de Copagos** según tipo de plan
- ✅ **Control de Acceso por Roles** (Admin, Médico, Paciente)
- ✅ **Gestión de Estados** (Pendiente, En Revisión, Aprobada, Rechazada)

### Técnicas

- ✅ **Arquitectura Hexagonal** (Ports & Adapters)
- ✅ **Clean Architecture** con separación de capas
- ✅ **API RESTful** con documentación OpenAPI/Swagger
- ✅ **Microservicios** con comunicación HTTP
- ✅ **Manejo Global de Errores** estandarizado
- ✅ **Observabilidad** con Spring Boot Actuator
- ✅ **Migraciones de BD** con Flyway
- ✅ **Dockerización Completa** con Docker Compose

---

## 🏗️ Arquitectura

### Arquitectura de Sistema
```
┌─────────────────────────────────────────────────────────────┐
│                         CLIENTE                              │
│                    (Swagger UI / Postman)                    │
└──────────────────────┬──────────────────────────────────────┘
                       │ HTTP/REST
                       ▼
┌─────────────────────────────────────────────────────────────┐
│              AUTHORIZATION SERVICE (Puerto 8080)             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Controllers (REST API - Adapters IN)               │   │
│  │  - AuthController                                   │   │
│  │  - PatientController                                │   │
│  │  - MedicalAuthorizationController                   │   │
│  └──────────────────┬──────────────────────────────────┘   │
│                     │                                        │
│  ┌──────────────────▼─────────────────────────────────┐   │
│  │  Application Layer (Use Cases)                      │   │
│  │  - RegisterPatient                                  │   │
│  │  - CreateAuthorization                              │   │
│  │  - EvaluateAuthorization                            │   │
│  └──────────────────┬──────────────────────────────────┘   │
│                     │                                        │
│  ┌──────────────────▼─────────────────────────────────┐   │
│  │  Domain Layer (Business Logic)                      │   │
│  │  - Patient, User, MedicalAuthorization              │   │
│  │  - Business Rules & Validations                     │   │
│  └──────────────────┬──────────────────────────────────┘   │
│                     │                                        │
│  ┌──────────────────▼─────────────────────────────────┐   │
│  │  Adapters OUT (Infrastructure)                      │   │
│  │  - JPA Repositories (MySQL)                         │   │
│  │  - External Service Adapters                        │   │
│  │  - Security (JWT)                                   │   │
│  └──────────────────┬──────────────────────────────────┘   │
└────────────────────┬┴──────────────────────────────────────┘
                     │                    │
           ┌─────────▼────────┐  ┌───────▼──────────┐
           │   MySQL DB       │  │ Insurance Mock   │
           │  (Puerto 3306)   │  │  Service (8081)  │
           └──────────────────┘  └──────────────────┘
```

### Arquitectura Hexagonal
```
┌───────────────────────────────────────────────────────────┐
│                    DOMAIN (Core)                          │
│  ┌─────────────────────────────────────────────────┐     │
│  │  Models: Patient, User, MedicalAuthorization    │     │
│  │  Enums: Roles, Status, ServiceType              │     │
│  │  Business Rules & Validations                   │     │
│  └─────────────────────────────────────────────────┘     │
│                                                           │
│  ┌─────────────────────────────────────────────────┐     │
│  │  Ports IN (Use Cases - Interfaces)              │     │
│  │  - RegisterPatientUseCase                        │     │
│  │  - CreateAuthorizationUseCase                    │     │
│  │  - EvaluateAuthorizationUseCase                  │     │
│  └─────────────────────────────────────────────────┘     │
│                                                           │
│  ┌─────────────────────────────────────────────────┐     │
│  │  Ports OUT (Interfaces)                          │     │
│  │  - PatientRepositoryPort                         │     │
│  │  - InsuranceValidationServicePort                │     │
│  └─────────────────────────────────────────────────┘     │
└───────────────────────────────────────────────────────────┘
                            ▲
                            │
┌───────────────────────────┼───────────────────────────────┐
│                    APPLICATION                            │
│  ┌─────────────────────────────────────────────────┐     │
│  │  Services (Use Case Implementations)            │     │
│  │  - RegisterPatientService                        │     │
│  │  - CreateAuthorizationService                    │     │
│  │  - EvaluateAuthorizationService                  │     │
│  └─────────────────────────────────────────────────┘     │
└───────────────────────────────────────────────────────────┘
                            ▲
                            │
┌───────────────────────────┼───────────────────────────────┐
│                   INFRASTRUCTURE                          │
│  ┌─────────────────────────────────────────────────┐     │
│  │  Adapters IN (Controllers REST)                 │     │
│  │  Adapters OUT (JPA, External Services)          │     │
│  │  Configuration (Security, Swagger, etc.)        │     │
│  └─────────────────────────────────────────────────┘     │
└───────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tecnologías

### Backend

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.5.10 | Framework backend |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Persistencia de datos |
| MySQL | 8.0 | Base de datos |
| Flyway | 9.x | Migraciones de BD |
| JWT | 0.11.5 | Tokens de autenticación |
| Swagger/OpenAPI | 3.0 | Documentación API |
| Lombok | 1.18.30 | Reducción de código boilerplate |
| Maven | 3.9+ | Gestión de dependencias |

### Herramientas

- **Docker & Docker Compose**: Containerización
- **Git**: Control de versiones
- **Postman**: Testing de APIs
- **IntelliJ IDEA**: IDE recomendado

---

## 📦 Requisitos

### Para desarrollo local

- Java 17 o superior
- Maven 3.9+
- MySQL 8.0+
- IDE (IntelliJ IDEA recomendado)
- Postman (opcional)

### Para Docker

- Docker 20.10+
- Docker Compose 2.0+
- 4GB RAM mínimo
- 10GB espacio en disco

---

## 🚀 Instalación

### Opción 1: Desarrollo Local

#### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/meditrack-system.git
cd meditrack-system
```

#### 2. Configurar MySQL
```sql
-- Crear base de datos
CREATE DATABASE meditrack_db;

-- Crear usuario
CREATE USER 'meditrack'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON meditrack_db.* TO 'meditrack'@'localhost';
FLUSH PRIVILEGES;
```

#### 3. Compilar y ejecutar Insurance Mock Service
```bash
cd insurance-validation-mock-service
mvn clean install
mvn spring-boot:run
```

El servicio estará disponible en `http://localhost:8081`

#### 4. Compilar y ejecutar Authorization Service
```bash
cd authorization-service
mvn clean install
mvn spring-boot:run
```

El servicio estará disponible en `http://localhost:8080`

#### 5. Verificar instalación
```bash
# Health checks
curl http://localhost:8080/actuator/health
curl http://localhost:8081/api/insurance/health

# Swagger UI
open http://localhost:8080/swagger-ui.html
```

---

### Opción 2: Docker (Recomendado)
```bash
# Iniciar todo el sistema
./start.sh

# O manualmente
docker-compose up -d

# Ver logs
docker-compose logs -f
```

Ver [DOCKER.md](DOCKER.md) para más detalles.

---

## ⚙️ Configuración

### application.properties

Archivo principal: `authorization-service/src/main/resources/application.properties`
```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/meditrack_db
spring.datasource.username=meditrack
spring.datasource.password=password

# JWT
jwt.secret=tu-secreto-super-seguro-aqui
jwt.expiration=86400000

# Servicio externo
insurance.validation.service.url=http://localhost:8081
```

### Variables de entorno
```bash
# JWT Secret
export JWT_SECRET=tu-secreto-super-seguro

# Database
export DB_URL=jdbc:mysql://localhost:3306/meditrack_db
export DB_USERNAME=meditrack
export DB_PASSWORD=password

# External Service
export INSURANCE_SERVICE_URL=http://localhost:8081
```

---

## 📖 Uso

### 1. Registrar un usuario Admin

**POST** `/api/auth/register`
```json
{
  "username": "admin_juan",
  "email": "admin@meditrack.com",
  "password": "admin123",
  "role": "ROLE_ADMIN"
}
```

### 2. Hacer login

**POST** `/api/auth/login`
```json
{
  "username": "admin_juan",
  "password": "admin123"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "...",
  "username": "admin_juan",
  "role": "ROLE_ADMIN"
}
```

### 3. Autorizar en Swagger

1. Copiar el token
2. Click en "Authorize" en Swagger
3. Pegar: `Bearer {token}`

### 4. Registrar un paciente

**POST** `/api/patients`
```json
{
  "documentNumber": "1000111222",
  "firstName": "Carlos",
  "lastName": "Ramírez",
  "email": "carlos@example.com",
  "phone": "3001234567",
  "affiliationType": "CONTRIBUTIVO",
  "affiliationDate": "2024-01-15",
  "username": "carlos_ramirez",
  "password": "paciente123"
}
```

### 5. Crear una autorización médica

**POST** `/api/authorizations`
```json
{
  "patientId": "{id-del-paciente}",
  "serviceType": "CONSULTA",
  "description": "Consulta de seguimiento por dolor lumbar crónico"
}
```

### 6. Evaluar la autorización

**POST** `/api/authorizations/{id}/evaluate`
```json
{
  "estimatedCost": 150000
}
```

Respuesta:
```json
{
  "id": "...",
  "authorizationId": "...",
  "coveragePercentage": 80,
  "copayAmount": 30000,
  "approved": true,
  "evaluationDate": "2024-01-28T15:30:00"
}
```

---

## 🔌 API Endpoints

### Autenticación

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Registrar usuario | No |
| POST | `/api/auth/login` | Iniciar sesión | No |

### Pacientes

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| POST | `/api/patients` | Crear paciente | Admin, Médico |
| GET | `/api/patients` | Listar pacientes | Todos |
| GET | `/api/patients/{id}` | Obtener paciente | Todos |
| PUT | `/api/patients/{id}` | Actualizar paciente | Admin, Médico, Paciente (propio) |
| DELETE | `/api/patients/{id}` | Desactivar paciente | Admin |

### Autorizaciones Médicas

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| POST | `/api/authorizations` | Crear autorización | Admin, Médico |
| POST | `/api/authorizations/{id}/evaluate` | Evaluar con seguro | Admin, Médico |
| GET | `/api/authorizations` | Listar pendientes | Admin, Médico |
| GET | `/api/authorizations/{id}` | Obtener por ID | Todos |
| GET | `/api/authorizations/patient/{id}` | Listar por paciente | Todos |
| PATCH | `/api/authorizations/{id}/status` | Cambiar estado | Admin |

### Observabilidad

| Endpoint | Descripción |
|----------|-------------|
| `/actuator/health` | Estado de salud |
| `/actuator/info` | Información de la app |
| `/actuator/metrics` | Métricas del sistema |

### Documentación

| Endpoint | Descripción |
|----------|-------------|
| `/swagger-ui.html` | Interfaz Swagger |
| `/v3/api-docs` | OpenAPI JSON |

---

## 🗄️ Base de Datos

### Modelo de Datos
```
┌─────────────┐       ┌──────────────────┐
│    users    │──1:1──│    patients      │
├─────────────┤       ├──────────────────┤
│ id (PK)     │       │ id (PK)          │
│ username    │       │ document_number  │
│ email       │       │ first_name       │
│ password    │       │ last_name        │
│ role        │       │ affiliation_type │
│ patient_id  │       │ user_id (FK)     │
└─────────────┘       └──────────────────┘
                               │
                               │ 1:N
                               ▼
                  ┌──────────────────────────┐
                  │ medical_authorizations   │
                  ├──────────────────────────┤
                  │ id (PK)                  │
                  │ patient_id (FK)          │
                  │ service_type             │
                  │ description              │
                  │ status                   │
                  │ requested_by (FK)        │
                  └──────────────────────────┘
                               │
                               │ 1:1
                               ▼
                  ┌──────────────────────────┐
                  │ coverage_evaluations     │
                  ├──────────────────────────┤
                  │ id (PK)                  │
                  │ authorization_id (FK)    │
                  │ coverage_percentage      │
                  │ copay_amount             │
                  │ is_approved              │
                  └──────────────────────────┘
```

### Migraciones Flyway

Las migraciones se encuentran en `src/main/resources/db/migration/`:

- `V1__create_users_table.sql` - Tabla de usuarios
- `V2__create_patients_table.sql` - Tabla de pacientes
- `V3__create_medical_authorizations_table.sql` - Tabla de autorizaciones
- `V4__create_coverage_evaluations_table.sql` - Tabla de evaluaciones
- `V5__add_sample_data.sql` - Datos de prueba (opcional)

---

## 🐳 Docker

### Servicios

- **mysql**: Base de datos (Puerto 3306)
- **insurance-service**: Mock de seguros (Puerto 8081)
- **authorization-service**: Servicio principal (Puerto 8080)

### Comandos rápidos
```bash
# Iniciar todo
./start.sh

# Ver logs
./logs.sh

# Detener todo
./stop.sh
```

Ver documentación completa en [DOCKER.md](DOCKER.md)

---

## 🧪 Testing

### Ejecutar tests
```bash
# Todos los tests
mvn test

# Tests de un módulo específico
mvn test -Dtest=CreateAuthorizationServiceTest

# Con cobertura
mvn test jacoco:report
```

### Colección de Postman

Importar el archivo `MediTrack.postman_collection.json` en Postman para probar todos los endpoints.

---

## 📊 Métricas y Observabilidad

### Health Checks
```bash
# Sistema principal
curl http://localhost:8080/actuator/health

# Servicio de seguros
curl http://localhost:8081/api/insurance/health
```

### Métricas personalizadas

- `meditrack.authorizations.created` - Total de autorizaciones creadas
- `meditrack.authorizations.evaluated` - Total evaluadas
- `meditrack.authorizations.approved` - Total aprobadas
- `meditrack.authorizations.rejected` - Total rechazadas
- `meditrack.patients.registered` - Total de pacientes registrados

---

## 🔐 Seguridad

### Autenticación

- JWT (JSON Web Tokens)
- Tokens con expiración de 24 horas
- Encriptación de contraseñas con BCrypt

### Autorización

| Rol | Permisos |
|-----|----------|
| **ADMIN** | Acceso completo al sistema |
| **MEDICO** | Crear y evaluar autorizaciones, ver pacientes |
| **PACIENTE** | Ver solo su propia información |

### Buenas prácticas

- ✅ Contraseñas encriptadas
- ✅ Tokens JWT firmados
- ✅ Validación de entrada
- ✅ SQL Injection prevention (JPA)
- ✅ XSS prevention
- ✅ CORS configurado

---

## 📝 Roadmap

### Version 2.0 (Planeado)

- [ ] Notificaciones por email
- [ ] Reportes en PDF/Excel
- [ ] Dashboard administrativo
- [ ] Integración con seguros reales
- [ ] Historial de auditoría
- [ ] Paginación en listados
- [ ] Filtros avanzados de búsqueda
- [ ] Frontend en Angular/React

---

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

---

## 👨‍💻 Autor

**Juan Pablo Rico Yepes**

- GitHub: [@tu-usuario](https://github.com/tu-usuario)
- Email: juan.rico@riwi.io
- LinkedIn: [Juan Pablo Rico Yepes](https://linkedin.com/in/tu-perfil)

### Proyecto Académico

Este proyecto fue desarrollado como parte del **Módulo 6 - Java Complementos 1** en **RIWI Medellín**.

**Instructor:** [Nombre del instructor]  
**Cohorte:** [Número de cohorte]  
**Fecha:** Enero 2026

---

## 🙏 Agradecimientos

- RIWI por la formación en desarrollo backend
- Spring Boot community
- Todos los que contribuyeron con feedback

---

## 📞 Soporte

Si tienes preguntas o problemas:

1. Revisa la [documentación](#-tabla-de-contenidos)
2. Busca en [Issues](https://github.com/tu-usuario/meditrack-system/issues)
3. Crea un nuevo Issue si es necesario

---

<div align="center">

**⭐ Si te gusta este proyecto, dale una estrella en GitHub ⭐**

Hecho con ❤️ por Juan Pablo Rico Yepes

</div>
```

---

## 🎉 FEATURE 13 COMPLETA

Has creado un README profesional con:

- ✅ Descripción clara del proyecto
- ✅ Arquitectura explicada con diagramas
- ✅ Instrucciones de instalación paso a paso
- ✅ Documentación de API completa
- ✅ Ejemplos de uso
- ✅ Diagramas de base de datos
- ✅ Información de Docker
- ✅ Badges y formato profesional
- ✅ Secciones de seguridad y roadmap

---

## 📊 Estado Final del Proyecto
```
✅ FEATURE 1-11: Sistema completo funcionando
✅ FEATURE 12: Docker (pendiente de probar)
✅ FEATURE 13: README Profesional