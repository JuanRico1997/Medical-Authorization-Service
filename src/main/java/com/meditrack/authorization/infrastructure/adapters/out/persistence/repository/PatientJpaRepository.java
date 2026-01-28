package com.meditrack.authorization.infrastructure.adapters.out.persistence.repository;

import com.meditrack.authorization.domain.enums.AffiliationStatus;
import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Patient
 */
@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, UUID> {

    /**
     * Busca un paciente por número de documento
     */
    Optional<PatientEntity> findByDocumentNumber(String documentNumber);

    /**
     * Verifica si existe un paciente con un número de documento específico
     */
    boolean existsByDocumentNumber(String documentNumber);

    /**
     * Busca pacientes por estado de afiliación
     */
    List<PatientEntity> findByAffiliationStatus(AffiliationStatus status);

    /**
     * Busca pacientes por tipo de afiliación
     */
    List<PatientEntity> findByAffiliationType(AffiliationType type);

    /**
     * Busca pacientes activos (no eliminados)
     */
    @Query("SELECT p FROM PatientEntity p WHERE p.deleted = false")
    List<PatientEntity> findAllActive();

    /**
     * Busca pacientes por estado de afiliación y no eliminados
     */
    @Query("SELECT p FROM PatientEntity p WHERE p.affiliationStatus = :status AND p.deleted = false")
    List<PatientEntity> findByAffiliationStatusAndNotDeleted(@Param("status") AffiliationStatus status);

    /**
     * Busca un paciente por ID y que no esté eliminado
     */
    @Query("SELECT p FROM PatientEntity p WHERE p.id = :id AND p.deleted = false")
    Optional<PatientEntity> findByIdAndNotDeleted(@Param("id") UUID id);

    /**
     * Busca pacientes por email
     */
    Optional<PatientEntity> findByEmail(String email);

    /**
     * Cuenta pacientes por tipo de afiliación
     */
    @Query("SELECT COUNT(p) FROM PatientEntity p WHERE p.affiliationType = :type AND p.deleted = false")
    long countByAffiliationType(@Param("type") AffiliationType type);
}