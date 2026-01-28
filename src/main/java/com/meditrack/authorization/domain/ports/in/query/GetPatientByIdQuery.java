package com.meditrack.authorization.domain.ports.in.query;

import java.util.UUID;

/**
 * Query: Obtener un paciente por ID
 */
public class GetPatientByIdQuery {

    private final UUID patientId;

    public GetPatientByIdQuery(UUID patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("El ID del paciente es obligatorio");
        }

        this.patientId = patientId;
    }

    public UUID getPatientId() {
        return patientId;
    }
}