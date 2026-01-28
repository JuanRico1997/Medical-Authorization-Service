package com.meditrack.authorization.domain.exceptions;

/**
 * Excepción: Recurso duplicado
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceType, String field, Object value) {
        super(String.format("%s con %s '%s' ya existe", resourceType, field, value));
    }
}