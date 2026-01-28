package com.meditrack.authorization.domain.ports.out;

import java.util.UUID;

/**
 * Puerto de salida: Usuario Actual
 * Define la operación para obtener el usuario autenticado
 */
public interface CurrentUserPort {

    /**
     * Obtiene el ID del usuario actualmente autenticado
     *
     * @return ID del usuario autenticado
     * @throws IllegalStateException si no hay usuario autenticado
     */
    UUID getCurrentUserId();
}
