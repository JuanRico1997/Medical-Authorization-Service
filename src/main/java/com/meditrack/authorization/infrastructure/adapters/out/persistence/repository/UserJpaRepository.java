package com.meditrack.authorization.infrastructure.adapters.out.persistence.repository;

import com.meditrack.authorization.domain.enums.UserRole;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad User
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Busca un usuario por username
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Busca un usuario por email
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Verifica si existe un usuario con un username específico
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con un email específico
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios por rol
     */
    List<UserEntity> findByRole(UserRole role);

    /**
     * Busca usuarios activos
     */
    List<UserEntity> findByActiveTrue();

    /**
     * Busca un usuario por patientId
     */
    Optional<UserEntity> findByPatientId(UUID patientId);

    /**
     * Busca usuarios por rol y estado activo
     */
    @Query("SELECT u FROM UserEntity u WHERE u.role = :role AND u.active = :active")
    List<UserEntity> findByRoleAndActive(
            @Param("role") UserRole role,
            @Param("active") boolean active
    );
}