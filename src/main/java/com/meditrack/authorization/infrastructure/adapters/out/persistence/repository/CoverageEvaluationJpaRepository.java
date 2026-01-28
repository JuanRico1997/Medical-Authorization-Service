package com.meditrack.authorization.infrastructure.adapters.out.persistence.repository;

import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.CoverageEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad CoverageEvaluation
 */
@Repository
public interface CoverageEvaluationJpaRepository extends JpaRepository<CoverageEvaluationEntity, UUID> {

    /**
     * Busca una evaluación por ID de autorización
     */
    Optional<CoverageEvaluationEntity> findByAuthorizationId(UUID authorizationId);

    /**
     * Verifica si existe una evaluación para una autorización
     */
    boolean existsByAuthorizationId(UUID authorizationId);

    /**
     * Busca evaluaciones aprobadas
     */
    @Query("SELECT ce FROM CoverageEvaluationEntity ce WHERE ce.isApproved = true")
    List<CoverageEvaluationEntity> findAllApproved();

    /**
     * Busca evaluaciones rechazadas
     */
    @Query("SELECT ce FROM CoverageEvaluationEntity ce WHERE ce.isApproved = false")
    List<CoverageEvaluationEntity> findAllRejected();

    /**
     * Busca evaluaciones con cobertura mayor o igual a un porcentaje
     */
    @Query("SELECT ce FROM CoverageEvaluationEntity ce WHERE ce.coveragePercentage >= :minPercentage")
    List<CoverageEvaluationEntity> findByCoveragePercentageGreaterThanEqual(
            @Param("minPercentage") int minPercentage
    );

    /**
     * Busca evaluaciones por rango de fechas
     */
    @Query("SELECT ce FROM CoverageEvaluationEntity ce " +
            "WHERE ce.evaluationDate BETWEEN :startDate AND :endDate " +
            "ORDER BY ce.evaluationDate DESC")
    List<CoverageEvaluationEntity> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Calcula el promedio de cobertura de todas las evaluaciones
     */
    @Query("SELECT AVG(ce.coveragePercentage) FROM CoverageEvaluationEntity ce")
    Double calculateAverageCoverage();

    /**
     * Cuenta evaluaciones aprobadas
     */
    @Query("SELECT COUNT(ce) FROM CoverageEvaluationEntity ce WHERE ce.isApproved = true")
    long countApproved();

    /**
     * Cuenta evaluaciones rechazadas
     */
    @Query("SELECT COUNT(ce) FROM CoverageEvaluationEntity ce WHERE ce.isApproved = false")
    long countRejected();
}