-- ==========================================
-- V4: Agregar foreign keys (relaciones)
-- ==========================================

-- Relación: users -> patients (opcional, puede ser NULL)
ALTER TABLE users
    ADD CONSTRAINT fk_users_patient
    FOREIGN KEY (patient_id) REFERENCES patients(id)
    ON DELETE SET NULL;

-- Relación: medical_authorizations -> patients
ALTER TABLE medical_authorizations
    ADD CONSTRAINT fk_authorizations_patient
    FOREIGN KEY (patient_id) REFERENCES patients(id)
    ON DELETE CASCADE;

-- Relación: medical_authorizations -> users (requested_by)
ALTER TABLE medical_authorizations
    ADD CONSTRAINT fk_authorizations_user
    FOREIGN KEY (requested_by) REFERENCES users(id)
    ON DELETE RESTRICT;

-- Relación: coverage_evaluations -> medical_authorizations (1:1)
ALTER TABLE coverage_evaluations
    ADD CONSTRAINT fk_coverage_authorization
    FOREIGN KEY (authorization_id) REFERENCES medical_authorizations(id)
    ON DELETE CASCADE;