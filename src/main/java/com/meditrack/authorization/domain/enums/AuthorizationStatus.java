package com.meditrack.authorization.domain.enums;

/**
 * Estado de una solicitud de autorización médica
 */
public enum AuthorizationStatus {
    /**
     * Solicitud creada, esperando evaluación
     */
    PENDIENTE,

    /**
     * Solicitud en proceso de revisión médica
     */
    EN_REVISION,

    /**
     * Solicitud aprobada, puede proceder el servicio
     */
    APROBADA,

    /**
     * Solicitud rechazada, no cumple requisitos
     */
    RECHAZADA
}