-- ==========================================
-- V1: Crear tablas users y patients
-- ==========================================

-- Tabla: users
CREATE TABLE users (
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    patient_id BINARY(16) NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_role CHECK (role IN ('ROLE_PACIENTE', 'ROLE_MEDICO', 'ROLE_ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices para users
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_patient_id ON users(patient_id);

-- Tabla: patients
CREATE TABLE patients (
    id BINARY(16) PRIMARY KEY,
    document_number VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NULL,
    affiliation_status VARCHAR(20) NOT NULL,
    affiliation_type VARCHAR(20) NOT NULL,
    affiliation_date DATE NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT chk_affiliation_status CHECK (affiliation_status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    CONSTRAINT chk_affiliation_type CHECK (affiliation_type IN ('CONTRIBUTIVO', 'SUBSIDIADO', 'ESPECIAL'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices para patients
CREATE INDEX idx_patients_document ON patients(document_number);
CREATE INDEX idx_patients_status ON patients(affiliation_status);
CREATE INDEX idx_patients_type ON patients(affiliation_type);
CREATE INDEX idx_patients_deleted ON patients(deleted);