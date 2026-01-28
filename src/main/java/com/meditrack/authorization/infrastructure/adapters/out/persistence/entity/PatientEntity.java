package com.meditrack.authorization.infrastructure.adapters.out.persistence.entity;

import com.meditrack.authorization.domain.enums.AffiliationStatus;
import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.models.Patient;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA: Paciente
 * Mapea la tabla 'patients' en la base de datos
 */
@Entity
@Table(name = "patients")
public class PatientEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    private String documentNumber;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "affiliation_status", nullable = false, length = 20)
    private AffiliationStatus affiliationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "affiliation_type", nullable = false, length = 20)
    private AffiliationType affiliationType;

    @Column(name = "affiliation_date", nullable = false)
    private LocalDate affiliationDate;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    // Relación OneToMany con MedicalAuthorization
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<MedicalAuthorizationEntity> authorizations = new ArrayList<>();

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    public PatientEntity() {
    }

    public PatientEntity(
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

    // ==========================================
    // CONVERSIÓN: DOMAIN <-> ENTITY
    // ==========================================

    /**
     * Convierte de modelo de dominio a entidad JPA
     */
    public static PatientEntity fromDomain(Patient patient) {
        return new PatientEntity(
                patient.getId(),
                patient.getDocumentNumber(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getAffiliationStatus(),
                patient.getAffiliationType(),
                patient.getAffiliationDate(),
                patient.isDeleted()
        );
    }

    /**
     * Convierte de entidad JPA a modelo de dominio
     */
    public Patient toDomain() {
        return new Patient(
                this.id,
                this.documentNumber,
                this.firstName,
                this.lastName,
                this.email,
                this.phone,
                this.affiliationStatus,
                this.affiliationType,
                this.affiliationDate,
                this.deleted
        );
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<MedicalAuthorizationEntity> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<MedicalAuthorizationEntity> authorizations) {
        this.authorizations = authorizations;
    }
}