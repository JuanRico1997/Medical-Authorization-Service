package com.meditrack.authorization.domain.enums;

/**
 * Estado de afiliación de un paciente al sistema de salud
 */
public enum AffiliationStatus {
    /**
     * Paciente activo - puede solicitar autorizaciones
     */
    ACTIVE,

    /**
     * Paciente inactivo - no puede solicitar autorizaciones
     */
    INACTIVE,

    /**
     * Paciente suspendido - temporalmente sin acceso
     */
    SUSPENDED
}