package com.meditrack.authorization.infrastructure.adapters.out.persistence.repository;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.MedicalAuthorizationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad MedicalAuthorization
 */
@Repository
public interface MedicalAuthorizationJpaRepository extends JpaRepository<MedicalAuthorizationEntity, UUID> {

    /**
     * Busca autorizaciones por paciente (no eliminadas)
     * Usa EntityGraph para evitar N+1 queries
     */
    @EntityGraph(attributePaths = {"patient", "coverageEvaluation"})
    @Query("SELECT ma FROM MedicalAuthorizationEntity ma WHERE ma.patientId = :patientId AND ma.deleted = false")
    List<MedicalAuthorizationEntity> findByPatientIdAndNotDeleted(@Param("patientId") UUID patientId);

    /**
     * Busca autorizaciones por estado (no eliminadas)
     */
    @Query("SELECT ma FROM MedicalAuthorizationEntity ma WHERE ma.status = :status AND ma.deleted = false")
    List<MedicalAuthorizationEntity> findByStatusAndNotDeleted(@Param("status") AuthorizationStatus status);

    /**
     * Busca autorizaciones por paciente y estado (no eliminadas)
     */
    @Query("SELECT ma FROM MedicalAuthorizationEntity ma " +
            "WHERE ma.patientId = :patientId AND ma.status = :status AND ma.deleted = false")
    List<MedicalAuthorizationEntity> findByPatientIdAndStatusAndNotDeleted(
            @Param("patientId") UUID patientId,
            @Param("status") AuthorizationStatus status
    );

    /**
     * Busca una autorización por ID y que no esté eliminada
     */
    @EntityGraph(attributePaths = {"patient", "coverageEvaluation"})
    @Query("SELECT ma FROM MedicalAuthorizationEntity ma WHERE ma.id = :id AND ma.deleted = false")
    Optional<MedicalAuthorizationEntity> findByIdAndNotDeleted(@Param("id") UUID id);

    /**
     * Busca autorizaciones por tipo de servicio (no eliminadas)
     */
    @Query("SELECT ma FROM MedicalAuthorizationEntity ma " +
            "WHERE ma.serviceType = :serviceType AND ma.deleted = false")
    List<MedicalAuthorizationEntity> findByServiceTypeAndNotDeleted(@Param("serviceType") ServiceType serviceType);

    /**
     * Busca autorizaciones pendientes para un paciente específico
     */
    @Query("SELECT ma FROM MedicalAuthorizationEntity ma " +
            "WHERE ma.patientId = :patientId " +
            "AND ma.status = 'PENDIENTE' " +
            "AND ma.deleted = false")
    List<MedicalAuthorizationEntity> findPendingByPatientId(@Param("patientId") UUID patientId);

    /**
     * Cuenta autorizaciones activas (no eliminadas) de un paciente
     */
    @Query("SELECT COUNT(ma) FROM MedicalAuthorizationEntity ma " +
            "WHERE ma.patientId = :patientId AND ma.deleted = false")
    long countActiveByPatientId(@Param("patientId") UUID patientId);

    /**
     * Busca autorizaciones por rango de fechas
     */
    @Query("SELECT ma FROM MedicalAuthorizationEntity ma " +
            "WHERE ma.requestDate BETWEEN :startDate AND :endDate " +
            "AND ma.deleted = false " +
            "ORDER BY ma.requestDate DESC")
    List<MedicalAuthorizationEntity> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Busca autorizaciones por quien las solicitó
     */
    @Query("SELECT ma FROM MedicalAuthorizationEntity ma " +
            "WHERE ma.requestedBy = :userId AND ma.deleted = false " +
            "ORDER BY ma.requestDate DESC")
    List<MedicalAuthorizationEntity> findByRequestedBy(@Param("userId") UUID userId);

    /**
     * Busca todas las autorizaciones activas (no eliminadas) con paginación implícita
     */
    @EntityGraph(attributePaths = {"patient"})
    @Query("SELECT ma FROM MedicalAuthorizationEntity ma WHERE ma.deleted = false ORDER BY ma.requestDate DESC")
    List<MedicalAuthorizationEntity> findAllActive();

    /**
     * Cuenta autorizaciones por estado (para métricas)
     */
    @Query("SELECT COUNT(ma) FROM MedicalAuthorizationEntity ma " +
            "WHERE ma.status = :status AND ma.deleted = false")
    long countByStatus(@Param("status") AuthorizationStatus status);

    /**
     * Busca una autorización por ID que no esté eliminada
     */
    Optional<MedicalAuthorizationEntity> findByIdAndDeletedFalse(UUID id);

    /**
     * Busca todas las autorizaciones por estado que no estén eliminadas
     */
    List<MedicalAuthorizationEntity> findByStatusAndDeletedFalse(AuthorizationStatus status);

    /**
     * Busca todas las autorizaciones de un paciente que no estén eliminadas
     */
    List<MedicalAuthorizationEntity> findByPatientIdAndDeletedFalse(UUID patientId);
}