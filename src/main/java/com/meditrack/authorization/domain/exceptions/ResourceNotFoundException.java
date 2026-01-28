package com.meditrack.authorization.domain.exceptions;

/**
 * Excepción: Recurso no encontrado
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceType, Object id) {
        super(String.format("%s no encontrado con id: %s", resourceType, id));
    }
}