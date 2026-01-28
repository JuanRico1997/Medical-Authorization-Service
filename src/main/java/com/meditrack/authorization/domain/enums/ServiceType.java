package com.meditrack.authorization.domain.enums;

/**
 * Tipo de servicio médico solicitado
 * Determina el porcentaje mínimo de cobertura requerido
 */
public enum ServiceType {
    /**
     * Consulta médica general o especializada
     * Cobertura mínima requerida: 70%
     */
    CONSULTA,

    /**
     * Procedimiento médico (exámenes, tratamientos)
     * Cobertura mínima requerida: 80%
     */
    PROCEDIMIENTO,

    /**
     * Cirugía (ambulatoria u hospitalaria)
     * Cobertura mínima requerida: 90%
     */
    CIRUGIA
}