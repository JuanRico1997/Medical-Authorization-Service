package com.meditrack.authorization.infrastructure.adapters.in.rest.controller;

import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.ports.out.InsuranceValidationServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller de prueba para verificar la integración con el servicio de seguros
 * NOTA: Este controller es temporal, solo para testing
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "Test Integration", description = "Endpoints de prueba de integración (temporal)")
public class TestIntegrationController {

    private final InsuranceValidationServicePort insuranceValidationService;

    public TestIntegrationController(InsuranceValidationServicePort insuranceValidationService) {
        this.insuranceValidationService = insuranceValidationService;
    }

    /**
     * GET /api/test/insurance-integration
     * Prueba la integración con el servicio de seguros
     */
    @GetMapping("/insurance-integration")
    @Operation(
            summary = "Probar integración con servicio de seguros",
            description = "Endpoint de prueba para verificar que la comunicación con el mock service funciona correctamente"
    )
    public ResponseEntity<Map<String, Object>> testInsuranceIntegration() {

        Map<String, Object> response = new HashMap<>();

        try {
            // Datos de prueba
            String testDocument = "1234567890";
            AffiliationType testAffiliation = AffiliationType.CONTRIBUTIVO;
            ServiceType testService = ServiceType.CONSULTA;
            BigDecimal testCost = new BigDecimal("150000");

            response.put("status", "Iniciando prueba de integración...");
            response.put("testData", Map.of(
                    "documentNumber", testDocument,
                    "affiliationType", testAffiliation,
                    "serviceType", testService,
                    "estimatedCost", testCost
            ));

            // Llamar al servicio de seguros
            InsuranceValidationServicePort.InsuranceValidationResult result =
                    insuranceValidationService.validateCoverage(
                            testDocument,
                            testAffiliation,
                            testService,
                            testCost
                    );

            // Construir respuesta exitosa
            response.put("integrationStatus", "SUCCESS");
            response.put("validationResult", Map.of(
                    "approved", result.isApproved(),
                    "coveragePercentage", result.getCoveragePercentage(),
                    "copayAmount", result.getCopayAmount(),
                    "coveredAmount", result.getCoveredAmount(),
                    "authorizationCode", result.getAuthorizationCode(),
                    "message", result.getMessage()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("integrationStatus", "FAILED");
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("suggestion", "Asegúrate de que el Insurance Mock Service esté ejecutándose en http://localhost:8081");

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * GET /api/test/insurance-integration/detailed
     * Prueba detallada con múltiples escenarios
     */
    @GetMapping("/insurance-integration/detailed")
    @Operation(
            summary = "Prueba detallada con múltiples escenarios",
            description = "Ejecuta múltiples pruebas con diferentes tipos de afiliación y servicios"
    )
    public ResponseEntity<Map<String, Object>> testDetailedIntegration() {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> tests = new HashMap<>();

        try {
            // Test 1: CONTRIBUTIVO + CONSULTA
            tests.put("test1_contributivo_consulta", runTest(
                    "1234567890",
                    AffiliationType.CONTRIBUTIVO,
                    ServiceType.CONSULTA,
                    new BigDecimal("150000")
            ));

            // Test 2: SUBSIDIADO + CIRUGIA
            tests.put("test2_subsidiado_cirugia", runTest(
                    "9876543210",
                    AffiliationType.SUBSIDIADO,
                    ServiceType.CIRUGIA,
                    new BigDecimal("5000000")
            ));

            // Test 3: ESPECIAL + PROCEDIMIENTO
            tests.put("test3_especial_procedimiento", runTest(
                    "5555555555",
                    AffiliationType.ESPECIAL,
                    ServiceType.PROCEDIMIENTO,
                    new BigDecimal("800000")
            ));

            response.put("integrationStatus", "SUCCESS");
            response.put("totalTests", tests.size());
            response.put("tests", tests);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("integrationStatus", "FAILED");
            response.put("error", e.getMessage());
            response.put("tests", tests);

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Helper para ejecutar un test individual
     */
    private Map<String, Object> runTest(String document, AffiliationType affiliation,
                                        ServiceType service, BigDecimal cost) {
        Map<String, Object> testResult = new HashMap<>();

        try {
            InsuranceValidationServicePort.InsuranceValidationResult result =
                    insuranceValidationService.validateCoverage(document, affiliation, service, cost);

            testResult.put("status", "SUCCESS");
            testResult.put("input", Map.of(
                    "document", document,
                    "affiliation", affiliation,
                    "service", service,
                    "cost", cost
            ));
            testResult.put("output", Map.of(
                    "approved", result.isApproved(),
                    "coverage", result.getCoveragePercentage() + "%",
                    "copay", result.getCopayAmount(),
                    "authCode", result.getAuthorizationCode()
            ));

        } catch (Exception e) {
            testResult.put("status", "FAILED");
            testResult.put("error", e.getMessage());
        }

        return testResult;
    }
}