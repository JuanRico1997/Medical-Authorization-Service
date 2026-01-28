package com.meditrack.authorization.domain.enums;

/**
 * Roles de usuario en el sistema
 * Determina los permisos y accesos
 */
public enum UserRole {
    /**
     * Rol de paciente
     * Puede: ver sus propias autorizaciones, crear solicitudes
     */
    ROLE_PACIENTE,

    /**
     * Rol de médico
     * Puede: ver autorizaciones pendientes, revisar solicitudes
     */
    ROLE_MEDICO,

    /**
     * Rol de administrador
     * Puede: acceso total al sistema, gestionar todos los recursos
     */
    ROLE_ADMIN
}
