package com.meditrack.authorization.domain.models;

import com.meditrack.authorization.domain.enums.UserRole;

import java.util.UUID;

/**
 * Entidad de dominio: Usuario
 * Representa a un usuario del sistema con credenciales y roles
 *
 * POJO puro sin dependencias de frameworks
 */
public class User {

    private UUID id;
    private String username;
    private String email;
    private String password;  // Encriptada
    private UserRole role;
    private UUID patientId;   // Nullable - solo si el usuario es un paciente
    private boolean active;

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    /**
     * Constructor para crear un nuevo usuario
     */
    public User(
            String username,
            String email,
            String password,
            UserRole role,
            UUID patientId) {

        // Validaciones de negocio
        validateUsername(username);
        validateEmail(email);
        validatePassword(password);
        validateRole(role);
        validatePatientIdForRole(role, patientId);

        this.id = UUID.randomUUID();
        this.username = username.trim().toLowerCase();
        this.email = email.trim().toLowerCase();
        this.password = password;  // Ya debe venir encriptada
        this.role = role;
        this.patientId = patientId;
        this.active = true;  // Por defecto activo
    }

    /**
     * Constructor para reconstruir desde persistencia
     */
    public User(
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

    /**
     * Constructor sin argumentos (requerido por algunos frameworks)
     */
    protected User() {
    }

    // ==========================================
    // MÉTODOS DE NEGOCIO
    // ==========================================

    /**
     * Verifica si el usuario es un paciente
     */
    public boolean isPatient() {
        return this.role == UserRole.ROLE_PACIENTE;
    }

    /**
     * Verifica si el usuario es un médico
     */
    public boolean isDoctor() {
        return this.role == UserRole.ROLE_MEDICO;
    }

    /**
     * Verifica si el usuario es un administrador
     */
    public boolean isAdmin() {
        return this.role == UserRole.ROLE_ADMIN;
    }

    /**
     * Verifica si el usuario tiene un paciente asociado
     */
    public boolean hasPatient() {
        return this.patientId != null;
    }

    /**
     * Verifica si el usuario puede acceder a información de un paciente específico
     *
     * Reglas:
     * - Admin: acceso a todos los pacientes
     * - Médico: acceso a todos los pacientes
     * - Paciente: solo acceso a su propia información
     */
    public boolean canAccessPatient(UUID patientId) {
        if (patientId == null) {
            return false;
        }

        // Admin y médico tienen acceso a todos
        if (isAdmin() || isDoctor()) {
            return true;
        }

        // Paciente solo puede acceder a su propia información
        if (isPatient()) {
            return this.patientId != null && this.patientId.equals(patientId);
        }

        return false;
    }

    /**
     * Verifica si el usuario puede modificar una autorización médica
     *
     * Reglas:
     * - Admin: puede modificar cualquier autorización
     * - Médico: puede modificar autorizaciones en estado PENDIENTE
     * - Paciente: no puede modificar autorizaciones directamente
     */
    public boolean canModifyAuthorization() {
        return isAdmin() || isDoctor();
    }

    /**
     * Verifica si el usuario puede crear autorizaciones para un paciente
     *
     * Reglas:
     * - Admin: puede crear para cualquier paciente
     * - Médico: puede crear para cualquier paciente
     * - Paciente: solo puede crear para sí mismo
     */
    public boolean canCreateAuthorizationForPatient(UUID patientId) {
        if (patientId == null) {
            return false;
        }

        // Admin y médico pueden crear para cualquier paciente
        if (isAdmin() || isDoctor()) {
            return true;
        }

        // Paciente solo puede crear para sí mismo
        if (isPatient()) {
            return this.patientId != null && this.patientId.equals(patientId);
        }

        return false;
    }

    /**
     * Desactiva el usuario
     */
    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException(
                    "El usuario ya está desactivado: " + this.id
            );
        }

        this.active = false;
    }

    /**
     * Activa el usuario
     */
    public void activate() {
        if (this.active) {
            throw new IllegalStateException(
                    "El usuario ya está activo: " + this.id
            );
        }

        this.active = true;
    }

    /**
     * Actualiza la contraseña del usuario
     * La contraseña debe venir ya encriptada
     */
    public void updatePassword(String newEncryptedPassword) {
        if (newEncryptedPassword == null || newEncryptedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (!this.active) {
            throw new IllegalStateException(
                    "No se puede actualizar la contraseña de un usuario inactivo: " + this.id
            );
        }

        this.password = newEncryptedPassword;
    }

    /**
     * Actualiza el email del usuario
     */
    public void updateEmail(String newEmail) {
        if (!this.active) {
            throw new IllegalStateException(
                    "No se puede actualizar el email de un usuario inactivo: " + this.id
            );
        }

        validateEmail(newEmail);
        this.email = newEmail.trim().toLowerCase();
    }

    // ==========================================
    // VALIDACIONES PRIVADAS
    // ==========================================

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }

        if (username.trim().length() < 3) {
            throw new IllegalArgumentException(
                    "El username debe tener al menos 3 caracteres"
            );
        }

        if (username.trim().length() > 50) {
            throw new IllegalArgumentException(
                    "El username no puede exceder 50 caracteres"
            );
        }

        // Solo letras, números y guiones bajos
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException(
                    "El username solo puede contener letras, números y guiones bajos"
            );
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        // Validación básica de email
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }

        if (email.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "El email no puede exceder 100 caracteres"
            );
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Nota: Aquí validamos que la contraseña encriptada no esté vacía
        // La validación de la contraseña en texto plano se hace antes de encriptar
    }

    private void validateRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }
    }

    private void validatePatientIdForRole(UserRole role, UUID patientId) {
        // Si el rol es PACIENTE, debe tener patientId
        if (role == UserRole.ROLE_PACIENTE && patientId == null) {
            throw new IllegalArgumentException(
                    "Un usuario con rol PACIENTE debe tener un patientId asociado"
            );
        }

        // Si el rol es MEDICO o ADMIN, no debe tener patientId
        if ((role == UserRole.ROLE_MEDICO || role == UserRole.ROLE_ADMIN)
                && patientId != null) {
            throw new IllegalArgumentException(
                    "Un usuario con rol " + role + " no debe tener un patientId asociado"
            );
        }
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public boolean isActive() {
        return active;
    }

    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", patientId=" + patientId +
                ", active=" + active +
                '}';
    }
}