-- ==========================================
-- V3: Crear tabla coverage_evaluations
-- ==========================================

CREATE TABLE coverage_evaluations (
    id BINARY(16) PRIMARY KEY,
    authorization_id BINARY(16) NOT NULL UNIQUE,
    coverage_percentage INT NOT NULL,
    copay_amount DECIMAL(10, 2) NOT NULL,
    is_approved BOOLEAN NOT NULL,
    evaluation_date DATETIME NOT NULL,
    insurance_response TEXT NULL,
    CONSTRAINT chk_coverage_percentage CHECK (coverage_percentage >= 0 AND coverage_percentage <= 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices para coverage_evaluations
CREATE INDEX idx_coverage_authorization ON coverage_evaluations(authorization_id);
CREATE INDEX idx_coverage_approved ON coverage_evaluations(is_approved);
CREATE INDEX idx_coverage_percentage ON coverage_evaluations(coverage_percentage);
CREATE INDEX idx_coverage_date ON coverage_evaluations(evaluation_date);