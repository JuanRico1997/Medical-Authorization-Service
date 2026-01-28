package com.meditrack.authorization.domain.enums;

/**
 * Tipo de afiliación del paciente al sistema de salud
 * Determina los porcentajes de copago permitidos
 */
public enum AffiliationType {
    /**
     * Régimen contributivo - trabajadores formales
     * Copago máximo: 20%
     */
    CONTRIBUTIVO,

    /**
     * Régimen subsidiado - población vulnerable
     * Copago máximo: 5%
     */
    SUBSIDIADO,

    /**
     * Régimen especial - fuerzas militares, maestros, etc
     * Copago máximo: 10%
     */
    ESPECIAL
}