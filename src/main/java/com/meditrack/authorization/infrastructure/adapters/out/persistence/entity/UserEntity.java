package com.meditrack.authorization.infrastructure.adapters.out.persistence.entity;

import com.meditrack.authorization.domain.enums.UserRole;
import com.meditrack.authorization.domain.models.User;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Entidad JPA: Usuario
 * Mapea la tabla 'users' en la base de datos
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Column(name = "patient_id", columnDefinition = "BINARY(16)")
    private UUID patientId;

    @Column(name = "active", nullable = false)
    private boolean active;

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    public UserEntity() {
    }

    public UserEntity(
            UUID id,
            String username,
            String email,
            String password,
            UserRole role,
            UUID patientId,
            boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.patientId = patientId;
        this.active = active;
    }

    // ==========================================
    // CONVERSIÓN: DOMAIN <-> ENTITY
    // ==========================================

    /**
     * Convierte de modelo de dominio a entidad JPA
     */
    public static UserEntity fromDomain(User user) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getPatientId(),
                user.isActive()
        );
    }

    /**
     * Convierte de entidad JPA a modelo de dominio
     */
    public User toDomain() {
        return new User(
                this.id,
                this.username,
                this.email,
                this.password,
                this.role,
                this.patientId,
                this.active
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}