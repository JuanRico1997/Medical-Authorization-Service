package com.meditrack.authorization.domain.ports.out;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.models.MedicalAuthorization;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida: Repositorio de Autorizaciones Médicas
 * Define las operaciones de persistencia que necesita el dominio
 */
public interface MedicalAuthorizationRepositoryPort {

    /**
     * Guarda una autorización médica en la base de datos
     */
    MedicalAuthorization save(MedicalAuthorization authorization);

    /**
     * Busca una autorización por ID
     */
    Optional<MedicalAuthorization> findById(UUID id);

    /**
     * Busca una autorización por ID y que no esté eliminada
     */
    Optional<MedicalAuthorization> findByIdAndNotDeleted(UUID id);

    /**
     * Busca autorizaciones por paciente (no eliminadas)
     */
    List<MedicalAuthorization> findByPatientIdAndNotDeleted(UUID patientId);

    /**
     * Busca autorizaciones por estado (no eliminadas)
     */
    List<MedicalAuthorization> findByStatusAndNotDeleted(AuthorizationStatus status);

    /**
     * Busca autorizaciones por paciente y estado (no eliminadas)
     */
    List<MedicalAuthorization> findByPatientIdAndStatusAndNotDeleted(
            UUID patientId,
            AuthorizationStatus status
    );

    /**
     * Busca autorizaciones por tipo de servicio (no eliminadas)
     */
    List<MedicalAuthorization> findByServiceTypeAndNotDeleted(ServiceType serviceType);

    /**
     * Busca autorizaciones pendientes para un paciente específico
     */
    List<MedicalAuthorization> findPendingByPatientId(UUID patientId);

    /**
     * Cuenta autorizaciones activas (no eliminadas) de un paciente
     */
    long countActiveByPatientId(UUID patientId);

    /**
     * Busca autorizaciones por rango de fechas
     */
    List<MedicalAuthorization> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca autorizaciones por quien las solicitó
     */
    List<MedicalAuthorization> findByRequestedBy(UUID userId);

    /**
     * Busca todas las autorizaciones activas (no eliminadas)
     */
    List<MedicalAuthorization> findAllActive();

    /**
     * Cuenta autorizaciones por estado (para métricas)
     */
    long countByStatus(AuthorizationStatus status);

    /**
     * Elimina una autorización por ID
     */
    void deleteById(UUID id);
}