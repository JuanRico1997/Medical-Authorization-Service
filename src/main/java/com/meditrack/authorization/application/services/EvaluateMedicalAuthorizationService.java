package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.models.CoverageEvaluation;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.command.EvaluateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.in.useCase.EvaluateMedicalAuthorizationUseCase;
import com.meditrack.authorization.domain.ports.out.CoverageEvaluationRepositoryPort;
import com.meditrack.authorization.domain.ports.out.InsuranceValidationServicePort;
import com.meditrack.authorization.domain.ports.out.MedicalAuthorizationRepositoryPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio: Evaluar Autorización Médica
 * Este servicio integra con el servicio externo de seguros
 */
@Service
public class EvaluateMedicalAuthorizationService implements EvaluateMedicalAuthorizationUseCase {

    private final MedicalAuthorizationRepositoryPort authorizationRepository;
    private final CoverageEvaluationRepositoryPort evaluationRepository;
    private final PatientRepositoryPort patientRepository;
    private final InsuranceValidationServicePort insuranceService;

    public EvaluateMedicalAuthorizationService(
            MedicalAuthorizationRepositoryPort authorizationRepository,
            CoverageEvaluationRepositoryPort evaluationRepository,
            PatientRepositoryPort patientRepository,
            InsuranceValidationServicePort insuranceService) {
        this.authorizationRepository = authorizationRepository;
        this.evaluationRepository = evaluationRepository;
        this.patientRepository = patientRepository;
        this.insuranceService = insuranceService;
    }

    @Override
    @Transactional
    public CoverageEvaluation execute(EvaluateMedicalAuthorizationCommand command) {

        // 1. Buscar la autorización
        MedicalAuthorization authorization = authorizationRepository.findByIdAndNotDeleted(
                command.getAuthorizationId()
        ).orElseThrow(() -> new IllegalArgumentException(
                "Autorización no encontrada: " + command.getAuthorizationId()
        ));

        // 2. Verificar que no haya sido evaluada
        if (evaluationRepository.existsByAuthorizationId(authorization.getId())) {
            throw new IllegalStateException(
                    "Esta autorización ya ha sido evaluada"
            );
        }

        // 3. Obtener el paciente
        Patient patient = patientRepository.findByIdAndNotDeleted(authorization.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Paciente no encontrado: " + authorization.getPatientId()
                ));

        // 4. Llamar al servicio externo de seguros
        System.out.println("Evaluando cobertura con servicio externo...");
        InsuranceValidationServicePort.InsuranceValidationResult validationResult =
                insuranceService.validateCoverage(
                        patient.getDocumentNumber(),
                        patient.getAffiliationType(),
                        authorization.getServiceType(),
                        command.getEstimatedCost()
                );

        // 5. Crear la evaluación de cobertura (dominio)
        String insuranceResponse = String.format(
                "{\"approved\":%b,\"coveragePercentage\":%d,\"coveredAmount\":%s,\"copayAmount\":%s,\"authorizationCode\":\"%s\",\"message\":\"%s\"}",
                validationResult.isApproved(),
                validationResult.getCoveragePercentage(),
                validationResult.getCoveredAmount(),
                validationResult.getCopayAmount(),
                validationResult.getAuthorizationCode() != null ? validationResult.getAuthorizationCode() : "N/A",
                validationResult.getMessage().replace("\"", "'")
        );

        CoverageEvaluation evaluation = new CoverageEvaluation(
                authorization.getId(),
                validationResult.getCoveragePercentage(),
                validationResult.getCopayAmount(),
                validationResult.isApproved(),
                insuranceResponse
        );

        // 6. Guardar la evaluación
        CoverageEvaluation savedEvaluation = evaluationRepository.save(evaluation);

        // 7. Actualizar el estado de la autorización según el resultado
        if (validationResult.isApproved()) {
            authorization.approve();
        } else {
            authorization.reject();
        }
        authorizationRepository.save(authorization);

        // 8. Log
        System.out.println("Evaluación completada: " + savedEvaluation.getId() +
                " - Aprobada: " + validationResult.isApproved() +
                " - Cobertura: " + validationResult.getCoveragePercentage() + "%" +
                " - Código: " + validationResult.getAuthorizationCode());

        return savedEvaluation;
    }
}