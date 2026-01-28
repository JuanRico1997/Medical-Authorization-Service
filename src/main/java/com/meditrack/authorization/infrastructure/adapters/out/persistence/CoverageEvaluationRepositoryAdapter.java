package com.meditrack.authorization.infrastructure.adapters.out.persistence;

import com.meditrack.authorization.domain.models.CoverageEvaluation;
import com.meditrack.authorization.domain.ports.out.CoverageEvaluationRepositoryPort;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.CoverageEvaluationEntity;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.repository.CoverageEvaluationJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para CoverageEvaluation
 * Implementa el puerto OUT CoverageEvaluationRepositoryPort
 * Traduce entre modelos de dominio y entidades JPA
 */
@Component
public class CoverageEvaluationRepositoryAdapter implements CoverageEvaluationRepositoryPort {

    private final CoverageEvaluationJpaRepository jpaRepository;

    public CoverageEvaluationRepositoryAdapter(CoverageEvaluationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CoverageEvaluation save(CoverageEvaluation evaluation) {
        CoverageEvaluationEntity entity = CoverageEvaluationEntity.fromDomain(evaluation);
        CoverageEvaluationEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<CoverageEvaluation> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(CoverageEvaluationEntity::toDomain);
    }

    @Override
    public Optional<CoverageEvaluation> findByAuthorizationId(UUID authorizationId) {
        return jpaRepository.findByAuthorizationId(authorizationId)
                .map(CoverageEvaluationEntity::toDomain);
    }

    @Override
    public boolean existsByAuthorizationId(UUID authorizationId) {
        return jpaRepository.existsByAuthorizationId(authorizationId);
    }

    @Override
    public List<CoverageEvaluation> findAllApproved() {
        return jpaRepository.findAllApproved().stream()
                .map(CoverageEvaluationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CoverageEvaluation> findAllRejected() {
        return jpaRepository.findAllRejected().stream()
                .map(CoverageEvaluationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CoverageEvaluation> findByCoveragePercentageGreaterThanEqual(int minPercentage) {
        return jpaRepository.findByCoveragePercentageGreaterThanEqual(minPercentage).stream()
                .map(CoverageEvaluationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CoverageEvaluation> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByDateRange(startDate, endDate).stream()
                .map(CoverageEvaluationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Double calculateAverageCoverage() {
        return jpaRepository.calculateAverageCoverage();
    }

    @Override
    public long countApproved() {
        return jpaRepository.countApproved();
    }

    @Override
    public long countRejected() {
        return jpaRepository.countRejected();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}