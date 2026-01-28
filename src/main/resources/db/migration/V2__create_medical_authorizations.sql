-- ==========================================
-- V2: Crear tabla medical_authorizations
-- ==========================================

CREATE TABLE medical_authorizations (
    id BINARY(16) PRIMARY KEY,
    patient_id BINARY(16) NOT NULL,
    service_type VARCHAR(20) NOT NULL,
    description VARCHAR(500) NOT NULL,
    request_date DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    requested_by BINARY(16) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT chk_service_type CHECK (service_type IN ('CONSULTA', 'PROCEDIMIENTO', 'CIRUGIA')),
    CONSTRAINT chk_status CHECK (status IN ('PENDIENTE', 'EN_REVISION', 'APROBADA', 'RECHAZADA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices para medical_authorizations
CREATE INDEX idx_authorizations_patient ON medical_authorizations(patient_id);
CREATE INDEX idx_authorizations_status ON medical_authorizations(status);
CREATE INDEX idx_authorizations_service_type ON medical_authorizations(service_type);
CREATE INDEX idx_authorizations_requested_by ON medical_authorizations(requested_by);
CREATE INDEX idx_authorizations_deleted ON medical_authorizations(deleted);
CREATE INDEX idx_authorizations_request_date ON medical_authorizations(request_date);