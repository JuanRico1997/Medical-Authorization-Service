package com.meditrack.authorization.infrastructure.adapters.out.external.adapter;

import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.exceptions.ExternalServiceException;
import com.meditrack.authorization.domain.ports.out.InsuranceValidationServicePort;
import com.meditrack.authorization.infrastructure.adapters.out.external.dto.InsuranceValidationRequest;
import com.meditrack.authorization.infrastructure.adapters.out.external.dto.InsuranceValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Adaptador: Comunicación con el servicio externo de validación de seguros
 */
@Component
public class InsuranceValidationServiceAdapter implements InsuranceValidationServicePort {

    private final RestTemplate restTemplate;
    private final String insuranceServiceUrl;

    public InsuranceValidationServiceAdapter(
            RestTemplate restTemplate,
            @Value("${insurance.validation.service.url}") String insuranceServiceUrl) {
        this.restTemplate = restTemplate;
        this.insuranceServiceUrl = insuranceServiceUrl;
    }

    @Override
    public InsuranceValidationResult validateCoverage(
            String patientDocumentNumber,
            AffiliationType affiliationType,
            ServiceType serviceType,
            BigDecimal estimatedCost) {

        try {
            // 1. Construir la URL del endpoint
            String url = insuranceServiceUrl + "/api/insurance/validate";

            // 2. Crear el request
            InsuranceValidationRequest request = new InsuranceValidationRequest(
                    patientDocumentNumber,
                    affiliationType.toString(),
                    serviceType.toString(),
                    estimatedCost
            );

            // 3. Llamar al servicio externo
            System.out.println("Llamando a Insurance Service: " + url);
            InsuranceValidationResponse response = restTemplate.postForObject(
                    url,
                    request,
                    InsuranceValidationResponse.class
            );

            // 4. Validar respuesta
            if (response == null) {
                throw new RuntimeException("No se recibió respuesta del servicio de seguros");
            }

            // 5. Log
            System.out.println("Respuesta recibida: Aprobado=" + response.isApproved() +
                    ", Cobertura=" + response.getCoveragePercentage() + "%");

            // 6. Convertir a resultado del dominio
            return new InsuranceValidationResult(
                    response.isApproved(),
                    response.getCoveragePercentage(),
                    response.getCopayAmount(),
                    response.getCoveredAmount(),
                    response.getAuthorizationCode(),
                    response.getMessage()
            );

        } catch (Exception e) {
            System.err.println("Error al llamar al servicio de seguros: " + e.getMessage());
            throw new ExternalServiceException("Insurance Validation Service", e);
        }
    }
}