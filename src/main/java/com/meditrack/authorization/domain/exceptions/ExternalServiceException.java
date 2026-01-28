package com.meditrack.authorization.domain.exceptions;

/**
 * Excepción: Error en servicio externo
 */
public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String serviceName, Throwable cause) {
        super(String.format("Error al comunicarse con el servicio %s: %s",
                serviceName, cause.getMessage()), cause);
    }
}