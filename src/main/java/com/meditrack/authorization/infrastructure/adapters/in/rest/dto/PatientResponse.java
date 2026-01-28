package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import com.meditrack.authorization.domain.enums.AffiliationStatus;
import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.models.Patient;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO: Response con información del paciente
 */
@Schema(description = "Información del paciente")
public class PatientResponse {

    @Schema(description = "ID único del paciente", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Número de documento", example = "1234567890")
    private String documentNumber;

    @Schema(description = "Nombre completo", example = "Carlos Gómez")
    private String fullName;

    @Schema(description = "Nombre", example = "Carlos")
    private String firstName;

    @Schema(description = "Apellido", example = "Gómez")
    private String lastName;

    @Schema(description = "Email", example = "carlos.gomez@example.com")
    private String email;

    @Schema(description = "Teléfono", example = "3001234567")
    private String phone;

    @Schema(description = "Estado de afiliación", example = "ACTIVE")
    private AffiliationStatus affiliationStatus;

    @Schema(description = "Tipo de afiliación", example = "CONTRIBUTIVO")
    private AffiliationType affiliationType;

    @Schema(description = "Fecha de afiliación", example = "2024-01-15")
    private LocalDate affiliationDate;

    @Schema(description = "Porcentaje máximo de copago", example = "20")
    private int maxCopayPercentage;

    // Constructores
    public PatientResponse() {
    }

    public PatientResponse(UUID id, String documentNumber, String fullName, String firstName,
                           String lastName, String email, String phone,
                           AffiliationStatus affiliationStatus, AffiliationType affiliationType,
                           LocalDate affiliationDate, int maxCopayPercentage) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.affiliationStatus = affiliationStatus;
        this.affiliationType = affiliationType;
        this.affiliationDate = affiliationDate;
        this.maxCopayPercentage = maxCopayPercentage;
    }

    /**
     * Crea un PatientResponse desde un Patient del dominio
     */
    public static PatientResponse fromDomain(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getDocumentNumber(),
                patient.getFullName(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getAffiliationStatus(),
                patient.getAffiliationType(),
                patient.getAffiliationDate(),
                patient.getMaxCopayPercentage()
        );
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AffiliationStatus getAffiliationStatus() {
        return affiliationStatus;
    }

    public void setAffiliationStatus(AffiliationStatus affiliationStatus) {
        this.affiliationStatus = affiliationStatus;
    }

    public AffiliationType getAffiliationType() {
        return affiliationType;
    }

    public void setAffiliationType(AffiliationType affiliationType) {
        this.affiliationType = affiliationType;
    }

    public LocalDate getAffiliationDate() {
        return affiliationDate;
    }

    public void setAffiliationDate(LocalDate affiliationDate) {
        this.affiliationDate = affiliationDate;
    }

    public int getMaxCopayPercentage() {
        return maxCopayPercentage;
    }

    public void setMaxCopayPercentage(int maxCopayPercentage) {
        this.maxCopayPercentage = maxCopayPercentage;
    }
}