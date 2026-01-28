package com.meditrack.authorization.domain.ports.out;

import com.meditrack.authorization.domain.enums.AffiliationStatus;
import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.models.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida: Repositorio de Pacientes
 * Define las operaciones de persistencia que necesita el dominio
 */
public interface PatientRepositoryPort {

    /**
     * Guarda un paciente en la base de datos
     */
    Patient save(Patient patient);

    /**
     * Busca un paciente por ID
     */
    Optional<Patient> findById(UUID id);

    /**
     * Busca un paciente por ID y que no esté eliminado
     */
    Optional<Patient> findByIdAndNotDeleted(UUID id);

    /**
     * Busca un paciente por número de documento
     */
    Optional<Patient> findByDocumentNumber(String documentNumber);

    /**
     * Verifica si existe un paciente con un número de documento específico
     */
    boolean existsByDocumentNumber(String documentNumber);

    /**
     * Busca pacientes por estado de afiliación
     */
    List<Patient> findByAffiliationStatus(AffiliationStatus status);

    /**
     * Busca pacientes por tipo de afiliación
     */
    List<Patient> findByAffiliationType(AffiliationType type);

    /**
     * Busca todos los pacientes activos (no eliminados)
     */
    List<Patient> findAllActive();

    /**
     * Busca pacientes por estado de afiliación y que no estén eliminados
     */
    List<Patient> findByAffiliationStatusAndNotDeleted(AffiliationStatus status);

    /**
     * Cuenta pacientes por tipo de afiliación
     */
    long countByAffiliationType(AffiliationType type);

    /**
     * Elimina un paciente por ID
     */
    void deleteById(UUID id);
}