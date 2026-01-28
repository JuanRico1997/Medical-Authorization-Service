package com.meditrack.authorization.domain.ports.in.command;

import java.util.UUID;

/**
 * Command: Datos para actualizar un paciente
 */
public class UpdatePatientCommand {

    private final UUID patientId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;

    public UpdatePatientCommand(
            UUID patientId,
            String firstName,
            String lastName,
            String email,
            String phone) {

        if (patientId == null) {
            throw new IllegalArgumentException("El ID del paciente es obligatorio");
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

        this.patientId = patientId;
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.phone = phone != null ? phone.trim() : null;
    }

    // Getters
    public UUID getPatientId() {
        return patientId;
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
}