package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO: Respuesta estándar de error
 */
@Schema(description = "Formato estándar de respuesta de error")
public class ErrorResponse {

    @Schema(description = "Timestamp del error", example = "2024-01-28T15:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Nombre del error", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje principal del error", example = "El paciente no existe")
    private String message;

    @Schema(description = "Ruta del endpoint donde ocurrió el error", example = "/api/authorizations")
    private String path;

    @Schema(description = "Lista de errores de validación (si aplica)")
    private List<ValidationError> validationErrors;

    // Constructores
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(int status, String error, String message, String path,
                         List<ValidationError> validationErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.validationErrors = validationErrors;
    }

    // Getters y Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    /**
     * Clase interna para errores de validación
     */
    @Schema(description = "Detalle de un error de validación")
    public static class ValidationError {

        @Schema(description = "Campo que falló la validación", example = "description")
        private String field;

        @Schema(description = "Mensaje de error", example = "La descripción debe tener al menos 10 caracteres")
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        // Getters y Setters
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}