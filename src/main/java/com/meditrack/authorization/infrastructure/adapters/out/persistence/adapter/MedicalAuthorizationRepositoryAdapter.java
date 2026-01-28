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
import java.util.stream.Collectors;

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
        return jpaRepository.findByIdAndNotDeleted(id)
                .map(MedicalAuthorizationEntity::toDomain);
    }

    @Override
    public List<MedicalAuthorization> findByPatientIdAndNotDeleted(UUID patientId) {
        return jpaRepository.findByPatientIdAndNotDeleted(patientId).stream()
                .map(MedicalAuthorizationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAuthorization> findByStatusAndNotDeleted(AuthorizationStatus status) {
        return jpaRepository.findByStatusAndNotDeleted(status).stream()
                .map(MedicalAuthorizationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAuthorization> findByPatientIdAndStatusAndNotDeleted(
            UUID patientId,
            AuthorizationStatus status) {
        return jpaRepository.findByPatientIdAndStatusAndNotDeleted(patientId, status).stream()
                .map(MedicalAuthorizationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAuthorization> findByServiceTypeAndNotDeleted(ServiceType serviceType) {
        return jpaRepository.findByServiceTypeAndNotDeleted(serviceType).stream()
                .map(MedicalAuthorizationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAuthorization> findPendingByPatientId(UUID patientId) {
        return jpaRepository.findPendingByPatientId(patientId).stream()
                .map(MedicalAuthorizationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countActiveByPatientId(UUID patientId) {
        return jpaRepository.countActiveByPatientId(patientId);
    }

    @Override
    public List<MedicalAuthorization> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByDateRange(startDate, endDate).stream()
                .map(MedicalAuthorizationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAuthorization> findByRequestedBy(UUID userId) {
        return jpaRepository.findByRequestedBy(userId).stream()
                .map(MedicalAuthorizationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAuthorization> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(MedicalAuthorizationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByStatus(AuthorizationStatus status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}