package com.meditrack.authorization.domain.ports.out;

import com.meditrack.authorization.domain.enums.UserRole;
import com.meditrack.authorization.domain.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida: Repositorio de Usuarios
 * Define las operaciones de persistencia que necesita el dominio
 */
public interface UserRepositoryPort {

    /**
     * Guarda un usuario en la base de datos
     */
    User save(User user);

    /**
     * Busca un usuario por ID
     */
    Optional<User> findById(UUID id);

    /**
     * Busca un usuario por username
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario por email
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si existe un usuario con un username específico
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con un email específico
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por patientId
     */
    Optional<User> findByPatientId(UUID patientId);

    /**
     * Busca usuarios por rol
     */
    List<User> findByRole(UserRole role);

    /**
     * Busca todos los usuarios activos
     */
    List<User> findAllActive();

    /**
     * Elimina un usuario por ID
     */
    void deleteById(UUID id);
}