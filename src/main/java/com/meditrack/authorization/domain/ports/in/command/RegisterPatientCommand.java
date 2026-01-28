package com.meditrack.authorization.domain.ports.in.command;

import com.meditrack.authorization.domain.enums.AffiliationType;

import java.time.LocalDate;

/**
 * Command: Datos para registrar un nuevo paciente
 */
public class RegisterPatientCommand {

    private final String documentNumber;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final AffiliationType affiliationType;
    private final LocalDate affiliationDate;
    private final String username;  // Para crear el usuario asociado
    private final String password;  // Para crear el usuario asociado

    public RegisterPatientCommand(
            String documentNumber,
            String firstName,
            String lastName,
            String email,
            String phone,
            AffiliationType affiliationType,
            LocalDate affiliationDate,
            String username,
            String password) {

        // Validaciones básicas
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }

        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (affiliationType == null) {
            throw new IllegalArgumentException("El tipo de afiliación es obligatorio");
        }

        if (affiliationDate == null) {
            throw new IllegalArgumentException("La fecha de afiliación es obligatoria");
        }

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        this.documentNumber = documentNumber.trim();
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.phone = phone != null ? phone.trim() : null;
        this.affiliationType = affiliationType;
        this.affiliationDate = affiliationDate;
        this.username = username.trim().toLowerCase();
        this.password = password;
    }

    // Getters
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

    public AffiliationType getAffiliationType() {
        return affiliationType;
    }

    public LocalDate getAffiliationDate() {
        return affiliationDate;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}