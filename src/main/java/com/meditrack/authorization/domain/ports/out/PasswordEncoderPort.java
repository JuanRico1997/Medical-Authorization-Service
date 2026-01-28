package com.meditrack.authorization.domain.ports.out;

/**
 * Puerto de salida: Encriptación de contraseñas
 * Define la operación de encriptación que necesita el dominio
 */
public interface PasswordEncoderPort {

    /**
     * Encripta una contraseña en texto plano
     *
     * @param rawPassword Contraseña en texto plano
     * @return Contraseña encriptada
     */
    String encode(String rawPassword);

    /**
     * Verifica si una contraseña en texto plano coincide con la encriptada
     *
     * @param rawPassword Contraseña en texto plano
     * @param encodedPassword Contraseña encriptada
     * @return true si coinciden
     */
    boolean matches(String rawPassword, String encodedPassword);
}