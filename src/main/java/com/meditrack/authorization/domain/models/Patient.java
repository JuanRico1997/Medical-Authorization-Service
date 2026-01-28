package com.meditrack.authorization.domain.models;

import com.meditrack.authorization.domain.enums.AffiliationStatus;
import com.meditrack.authorization.domain.enums.AffiliationType;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidad de dominio: Paciente
 * Representa a un paciente afiliado al sistema de salud
 *
 * POJO puro sin dependencias de frameworks
 */
public class Patient {

    private UUID id;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AffiliationStatus affiliationStatus;
    private AffiliationType affiliationType;
    private LocalDate affiliationDate;
    private boolean deleted;

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    /**
     * Constructor para crear un nuevo paciente
     */
    public Patient(
            String documentNumber,
            String firstName,
            String lastName,
            String email,
            String phone,
            AffiliationType affiliationType,
            LocalDate affiliationDate) {

        // Validaciones de negocio
        validateDocumentNumber(documentNumber);
        validateName(firstName, "firstName");
        validateName(lastName, "lastName");
        validateEmail(email);
        validateAffiliationDate(affiliationDate);

        this.id = UUID.randomUUID();
        this.documentNumber = documentNumber.trim();
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.phone = phone != null ? phone.trim() : null;
        this.affiliationType = affiliationType;
        this.affiliationDate = affiliationDate;
        this.affiliationStatus = AffiliationStatus.ACTIVE; // Por defecto ACTIVE
        this.deleted = false;
    }

    /**
     * Constructor para reconstruir desde persistencia
     */
    public Patient(
            UUID id,
            String documentNumber,
            String firstName,
            String lastName,
            String email,
            String phone,
            AffiliationStatus affiliationStatus,
            AffiliationType affiliationType,
            LocalDate affiliationDate,
            boolean deleted) {

        this.id = id;
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.affiliationStatus = affiliationStatus;
        this.affiliationType = affiliationType;
        this.affiliationDate = affiliationDate;
        this.deleted = deleted;
    }

    /**
     * Constructor sin argumentos (requerido por algunos frameworks)
     */
    protected Patient() {
    }

    // ==========================================
    // MÉTODOS DE NEGOCIO
    // ==========================================

    /**
     * Verifica si el paciente puede solicitar una autorización médica
     * Solo pacientes ACTIVOS pueden solicitar autorizaciones
     */
    public boolean canRequestAuthorization() {
        return this.affiliationStatus == AffiliationStatus.ACTIVE && !this.deleted;
    }

    /**
     * Verifica si el paciente está activo (afiliación activa y no eliminado)
     */
    public boolean isActive() {
        return this.affiliationStatus == AffiliationStatus.ACTIVE && !this.deleted;
    }

    /**
     * Desactiva el paciente (soft delete)
     * Un paciente desactivado no puede solicitar autorizaciones
     */
    public void deactivate() {
        if (this.deleted) {
            throw new IllegalStateException(
                    "El paciente ya está eliminado: " + this.id
            );
        }

        this.affiliationStatus = AffiliationStatus.INACTIVE;
    }

    /**
     * Suspende temporalmente al paciente
     */
    public void suspend() {
        if (this.deleted) {
            throw new IllegalStateException(
                    "No se puede suspender un paciente eliminado: " + this.id
            );
        }

        if (this.affiliationStatus == AffiliationStatus.SUSPENDED) {
            throw new IllegalStateException(
                    "El paciente ya está suspendido: " + this.id
            );
        }

        this.affiliationStatus = AffiliationStatus.SUSPENDED;
    }

    /**
     * Reactiva un paciente suspendido o inactivo
     */
    public void activate() {
        if (this.deleted) {
            throw new IllegalStateException(
                    "No se puede activar un paciente eliminado: " + this.id
            );
        }

        if (this.affiliationStatus == AffiliationStatus.ACTIVE) {
            throw new IllegalStateException(
                    "El paciente ya está activo: " + this.id
            );
        }

        this.affiliationStatus = AffiliationStatus.ACTIVE;
    }

    /**
     * Marca el paciente como eliminado (soft delete)
     */
    public void delete() {
        if (this.deleted) {
            throw new IllegalStateException(
                    "El paciente ya está eliminado: " + this.id
            );
        }

        this.deleted = true;
        this.affiliationStatus = AffiliationStatus.INACTIVE;
    }

    /**
     * Actualiza la información del paciente
     */
    public void update(
            String firstName,
            String lastName,
            String email,
            String phone) {

        if (this.deleted) {
            throw new IllegalStateException(
                    "No se puede actualizar un paciente eliminado: " + this.id
            );
        }

        validateName(firstName, "firstName");
        validateName(lastName, "lastName");
        validateEmail(email);

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.phone = phone != null ? phone.trim() : null;
    }

    /**
     * Obtiene el nombre completo del paciente
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Obtiene el copago máximo permitido según el tipo de afiliación
     *
     * @return Porcentaje de copago máximo (0-100)
     */
    public int getMaxCopayPercentage() {
        return switch (this.affiliationType) {
            case CONTRIBUTIVO -> 20;  // 20%
            case SUBSIDIADO -> 5;      // 5%
            case ESPECIAL -> 10;       // 10%
        };
    }

    // ==========================================
    // VALIDACIONES PRIVADAS
    // ==========================================

    private void validateDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }

        if (documentNumber.trim().length() < 5) {
            throw new IllegalArgumentException(
                    "El número de documento debe tener al menos 5 caracteres"
            );
        }
    }

    private void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " es obligatorio");
        }

        if (name.trim().length() < 2) {
            throw new IllegalArgumentException(
                    fieldName + " debe tener al menos 2 caracteres"
            );
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        // Validación básica de email
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
    }

    private void validateAffiliationDate(LocalDate affiliationDate) {
        if (affiliationDate == null) {
            throw new IllegalArgumentException("La fecha de afiliación es obligatoria");
        }

        // La fecha de afiliación no puede ser futura
        if (affiliationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La fecha de afiliación no puede ser futura"
            );
        }
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public UUID getId() {
        return id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public AffiliationStatus getAffiliationStatus() {
        return affiliationStatus;
    }

    public AffiliationType getAffiliationType() {
        return affiliationType;
    }

    public LocalDate getAffiliationDate() {
        return affiliationDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;
        return id != null && id.equals(patient.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", documentNumber='" + documentNumber + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", affiliationStatus=" + affiliationStatus +
                ", affiliationType=" + affiliationType +
                ", deleted=" + deleted +
                '}';
    }
}