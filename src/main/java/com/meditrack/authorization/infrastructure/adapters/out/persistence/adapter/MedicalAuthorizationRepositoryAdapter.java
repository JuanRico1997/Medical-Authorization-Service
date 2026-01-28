package com.meditrack.authorization.infrastructure.adapters.out.persistence.adapter;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.out.MedicalAuthorizationRepositoryPort;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.MedicalAuthorizationEntity;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.repository.MedicalAuthorizationJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para MedicalAuthorization
 * Implementa el puerto OUT MedicalAuthorizationRepositoryPort
 * Traduce entre modelos de dominio y entidades JPA
 */
@Component
public class MedicalAuthorizationRepositoryAdapter implements MedicalAuthorizationRepositoryPort {

    private final MedicalAuthorizationJpaRepository jpaRepository;

    public MedicalAuthorizationRepositoryAdapter(MedicalAuthorizationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MedicalAuthorization save(MedicalAuthorization authorization) {
        MedicalAuthorizationEntity entity = MedicalAuthorizationEntity.fromDomain(authorization);
        MedicalAuthorizationEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<MedicalAuthorization> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(MedicalAuthorizationEntity::toDomain);
    }

    @Override
    public Optional<MedicalAuthorization> findByIdAndNotDeleted(UUID id) {
        return jpaRepository.findByIdAndDeletedFalse(id)
                .map(entity -> entity.toDomain());
    }

    @Override
    public List<MedicalAuthorization> findByPatientIdAndNotDeleted(UUID patientId) {
        return List.of();
    }

    @Override
    public List<MedicalAuthorization> findByStatusAndNotDeleted(AuthorizationStatus status) {
        return List.of();
    }

    @Override
    public List<MedicalAuthorization> findByPatientIdAndStatusAndNotDeleted(UUID patientId, AuthorizationStatus status) {
        return List.of();
    }

    @Override
    public List<MedicalAuthorization> findByServiceTypeAndNotDeleted(ServiceType serviceType) {
        return List.of();
    }

    @Override
    public List<MedicalAuthorization> findPendingByPatientId(UUID patientId) {
        return List.of();
    }

    @Override
    public long countActiveByPatientId(UUID patientId) {
        return 0;
    }

    @Override
    public List<MedicalAuthorization> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return List.of();
    }

    @Override
    public List<MedicalAuthorization> findByRequestedBy(UUID userId) {
        return List.of();
    }

    @Override
    public List<MedicalAuthorization> findAllActive() {
        return List.of();
    }

    @Override
    public long countByStatus(AuthorizationStatus status) {
        return 0;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public List<MedicalAuthorization> findByStatus(AuthorizationStatus status) {
        return jpaRepository.findByStatusAndDeletedFalse(status)
                .stream()
                .map(entity -> entity.toDomain())
                .toList();
    }

    @Override
    public List<MedicalAuthorization> findByPatientId(UUID patientId) {
        return jpaRepository.findByPatientIdAndDeletedFalse(patientId)
                .stream()
                .map(entity -> entity.toDomain())
                .toList();
    }
}