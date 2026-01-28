package com.meditrack.authorization.domain.ports.out;

import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.enums.ServiceType;

import java.math.BigDecimal;

/**
 * Puerto de salida: Servicio de Validación de Seguros
 * Define las operaciones para comunicarse con el servicio externo
 */
public interface InsuranceValidationServicePort {

    /**
     * Valida la cobertura de un servicio médico con la aseguradora
     *
     * @param patientDocumentNumber Número de documento del paciente
     * @param affiliationType Tipo de afiliación del paciente
     * @param serviceType Tipo de servicio médico
     * @param estimatedCost Costo estimado del servicio
     * @return Resultado de la validación con cobertura, copago y código de autorización
     */
    InsuranceValidationResult validateCoverage(
            String patientDocumentNumber,
            AffiliationType affiliationType,
            ServiceType serviceType,
            BigDecimal estimatedCost
    );

    /**
     * Resultado de la validación de seguros
     */
    class InsuranceValidationResult {
        private final boolean approved;
        private final int coveragePercentage;
        private final BigDecimal copayAmount;
        private final BigDecimal coveredAmount;
        private final String authorizationCode;
        private final String message;

        public InsuranceValidationResult(boolean approved, int coveragePercentage,
                                         BigDecimal copayAmount, BigDecimal coveredAmount,
                                         String authorizationCode, String message) {
            this.approved = approved;
            this.coveragePercentage = coveragePercentage;
            this.copayAmount = copayAmount;
            this.coveredAmount = coveredAmount;
            this.authorizationCode = authorizationCode;
            this.message = message;
        }

        public boolean isApproved() {
            return approved;
        }

        public int getCoveragePercentage() {
            return coveragePercentage;
        }

        public BigDecimal getCopayAmount() {
            return copayAmount;
        }

        public BigDecimal getCoveredAmount() {
            return coveredAmount;
        }

        public String getAuthorizationCode() {
            return authorizationCode;
        }

        public String getMessage() {
            return message;
        }
    }
}