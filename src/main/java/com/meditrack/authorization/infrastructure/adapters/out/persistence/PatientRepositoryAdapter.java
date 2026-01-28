package com.meditrack.authorization.infrastructure.adapters.out.persistence;

import com.meditrack.authorization.domain.enums.AffiliationStatus;
import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.PatientEntity;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.repository.PatientJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para Patient
 * Implementa el puerto OUT PatientRepositoryPort
 * Traduce entre modelos de dominio y entidades JPA
 */
@Component
public class PatientRepositoryAdapter implements PatientRepositoryPort {

    private final PatientJpaRepository jpaRepository;

    public PatientRepositoryAdapter(PatientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = PatientEntity.fromDomain(patient);
        PatientEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(PatientEntity::toDomain);
    }

    @Override
    public Optional<Patient> findByIdAndNotDeleted(UUID id) {
        return jpaRepository.findByIdAndNotDeleted(id)
                .map(PatientEntity::toDomain);
    }

    @Override
    public Optional<Patient> findByDocumentNumber(String documentNumber) {
        return jpaRepository.findByDocumentNumber(documentNumber)
                .map(PatientEntity::toDomain);
    }

    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return jpaRepository.existsByDocumentNumber(documentNumber);
    }

    @Override
    public List<Patient> findByAffiliationStatus(AffiliationStatus status) {
        return jpaRepository.findByAffiliationStatus(status).stream()
                .map(PatientEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> findByAffiliationType(AffiliationType type) {
        return jpaRepository.findByAffiliationType(type).stream()
                .map(PatientEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(PatientEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> findByAffiliationStatusAndNotDeleted(AffiliationStatus status) {
        return jpaRepository.findByAffiliationStatusAndNotDeleted(status).stream()
                .map(PatientEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByAffiliationType(AffiliationType type) {
        return jpaRepository.countByAffiliationType(type);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}