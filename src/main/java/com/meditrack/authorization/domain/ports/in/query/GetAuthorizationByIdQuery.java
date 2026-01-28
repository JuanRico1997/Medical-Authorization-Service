package com.meditrack.authorization.domain.ports.in.query;

import java.util.UUID;

/**
 * Query: Obtener una autorización por ID
 */
public class GetAuthorizationByIdQuery {

    private final UUID authorizationId;

    public GetAuthorizationByIdQuery(UUID authorizationId) {
        if (authorizationId == null) {
            throw new IllegalArgumentException("El ID de la autorización es obligatorio");
        }

        this.authorizationId = authorizationId;
    }

    public UUID getAuthorizationId() {
        return authorizationId;
    }
}