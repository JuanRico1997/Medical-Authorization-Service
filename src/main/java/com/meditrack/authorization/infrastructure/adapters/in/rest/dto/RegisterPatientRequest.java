package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import com.meditrack.authorization.domain.enums.AffiliationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO: Request para registrar un paciente
 */
@Schema(description = "Datos para registrar un nuevo paciente")
public class RegisterPatientRequest {

    @Schema(description = "Número de documento del paciente", example = "1234567890", required = true)
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(min = 5, max = 20, message = "El número de documento debe tener entre 5 y 20 caracteres")
    private String documentNumber;

    @Schema(description = "Nombre del paciente", example = "Carlos", required = true)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String firstName;

    @Schema(description = "Apellido del paciente", example = "Gómez", required = true)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String lastName;

    @Schema(description = "Email del paciente", example = "carlos.gomez@example.com", required = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Schema(description = "Teléfono del paciente", example = "3001234567")
    private String phone;

    @Schema(description = "Tipo de afiliación", example = "CONTRIBUTIVO", required = true)
    @NotNull(message = "El tipo de afiliación es obligatorio")
    private AffiliationType affiliationType;

    @Schema(description = "Fecha de afiliación", example = "2024-01-15", required = true)
    @NotNull(message = "La fecha de afiliación es obligatoria")
    private LocalDate affiliationDate;

    @Schema(description = "Nombre de usuario para login", example = "carlos_gomez", required = true)
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;

    @Schema(description = "Contraseña (mínimo 6 caracteres)", example = "password123", required = true)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // Constructores
    public RegisterPatientRequest() {
    }

    public RegisterPatientRequest(String documentNumber, String firstName, String lastName,
                                  String email, String phone, AffiliationType affiliationType,
                                  LocalDate affiliationDate, String username, String password) {
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.affiliationType = affiliationType;
        this.affiliationDate = affiliationDate;
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}