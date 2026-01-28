package com.meditrack.authorization.domain.ports.in.query;

import java.util.UUID;

/**
 * Query: Listar autorizaciones de un paciente
 */
public class ListAuthorizationsByPatientQuery {

    private final UUID patientId;

    public ListAuthorizationsByPatientQuery(UUID patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("El ID del paciente es obligatorio");
        }

        this.patientId = patientId;
    }

    public UUID getPatientId() {
        return patientId;
    }
}