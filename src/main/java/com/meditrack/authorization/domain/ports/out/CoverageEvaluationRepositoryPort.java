package com.meditrack.authorization.domain.ports.out;

import com.meditrack.authorization.domain.models.CoverageEvaluation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida: Repositorio de Evaluaciones de Cobertura
 * Define las operaciones de persistencia que necesita el dominio
 */
public interface CoverageEvaluationRepositoryPort {

    /**
     * Guarda una evaluación de cobertura en la base de datos
     */
    CoverageEvaluation save(CoverageEvaluation evaluation);

    /**
     * Busca una evaluación por ID
     */
    Optional<CoverageEvaluation> findById(UUID id);

    /**
     * Busca una evaluación por ID de autorización
     */
    Optional<CoverageEvaluation> findByAuthorizationId(UUID authorizationId);

    /**
     * Verifica si existe una evaluación para una autorización
     */
    boolean existsByAuthorizationId(UUID authorizationId);

    /**
     * Busca todas las evaluaciones aprobadas
     */
    List<CoverageEvaluation> findAllApproved();

    /**
     * Busca todas las evaluaciones rechazadas
     */
    List<CoverageEvaluation> findAllRejected();

    /**
     * Busca evaluaciones con cobertura mayor o igual a un porcentaje
     */
    List<CoverageEvaluation> findByCoveragePercentageGreaterThanEqual(int minPercentage);

    /**
     * Busca evaluaciones por rango de fechas
     */
    List<CoverageEvaluation> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Calcula el promedio de cobertura de todas las evaluaciones
     */
    Double calculateAverageCoverage();

    /**
     * Cuenta evaluaciones aprobadas
     */
    long countApproved();

    /**
     * Cuenta evaluaciones rechazadas
     */
    long countRejected();

    /**
     * Elimina una evaluación por ID
     */
    void deleteById(UUID id);
}