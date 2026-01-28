-- ==========================================
-- V5: Insertar datos iniciales
-- ==========================================

-- Insertar usuario administrador
-- Password: admin123 (encriptada con BCrypt)
-- Nota: Esta contraseña debe cambiarse en producción
INSERT INTO users (id, username, email, password, role, patient_id, active)
VALUES (
    UNHEX(REPLACE(UUID(), '-', '')),
    'admin',
    'admin@meditrack.com',
    '$2a$10$rZ8WqK5nX.9qYf5P6vXxBOKhZx3eqL3JGqQ5X0Z7vL8qY5P6vXxBO',
    'ROLE_ADMIN',
    NULL,
    TRUE
);

-- Insertar usuario médico de prueba
-- Password: medico123
INSERT INTO users (id, username, email, password, role, patient_id, active)
VALUES (
    UNHEX(REPLACE(UUID(), '-', '')),
    'dra_martinez',
    'martinez@meditrack.com',
    '$2a$10$rZ8WqK5nX.9qYf5P6vXxBOKhZx3eqL3JGqQ5X0Z7vL8qY5P6vXxBO',
    'ROLE_MEDICO',
    NULL,
    TRUE
);

-- Insertar paciente de prueba
SET @patient_id = UNHEX(REPLACE(UUID(), '-', ''));

INSERT INTO patients (id, document_number, first_name, last_name, email, phone, affiliation_status, affiliation_type, affiliation_date, deleted)
VALUES (
    @patient_id,
    '1234567890',
    'Juan',
    'Pérez',
    'juan.perez@example.com',
    '3001234567',
    'ACTIVE',
    'CONTRIBUTIVO',
    '2023-01-15',
    FALSE
);

-- Insertar usuario paciente vinculado al paciente de prueba
INSERT INTO users (id, username, email, password, role, patient_id, active)
VALUES (
    UNHEX(REPLACE(UUID(), '-', '')),
    'juan_perez',
    'juan.perez@example.com',
    '$2a$10$rZ8WqK5nX.9qYf5P6vXxBOKhZx3eqL3JGqQ5X0Z7vL8qY5P6vXxBO',
    'ROLE_PACIENTE',
    @patient_id,
    TRUE
);

-- Insertar otro paciente de prueba
SET @patient_id2 = UNHEX(REPLACE(UUID(), '-', ''));

INSERT INTO patients (id, document_number, first_name, last_name, email, phone, affiliation_status, affiliation_type, affiliation_date, deleted)
VALUES (
    @patient_id2,
    '9876543210',
    'María',
    'García',
    'maria.garcia@example.com',
    '3009876543',
    'ACTIVE',
    'SUBSIDIADO',
    '2023-06-20',
    FALSE
);

-- Insertar usuario paciente vinculado al segundo paciente
INSERT INTO users (id, username, email, password, role, patient_id, active)
VALUES (
    UNHEX(REPLACE(UUID(), '-', '')),
    'maria_garcia',
    'maria.garcia@example.com',
    '$2a$10$rZ8WqK5nX.9qYf5P6vXxBOKhZx3eqL3JGqQ5X0Z7vL8qY5P6vXxBO',
    'ROLE_PACIENTE',
    @patient_id2,
    TRUE
);
