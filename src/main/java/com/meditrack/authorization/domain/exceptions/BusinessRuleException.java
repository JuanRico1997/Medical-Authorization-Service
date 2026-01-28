package com.meditrack.authorization.domain.exceptions;

/**
 * Excepción: Violación de regla de negocio
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}