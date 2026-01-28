package com.meditrack.authorization.infrastructure.adapters.out.external.dto;

import java.math.BigDecimal;

/**
 * DTO: Request para el servicio externo de validación de seguros
 */
public class InsuranceValidationRequest {

    private String patientDocumentNumber;
    private String affiliationType;
    private String serviceType;
    private BigDecimal estimatedCost;

    // Constructores
    public InsuranceValidationRequest() {
    }

    public InsuranceValidationRequest(String patientDocumentNumber, String affiliationType,
                                      String serviceType, BigDecimal estimatedCost) {
        this.patientDocumentNumber = patientDocumentNumber;
        this.affiliationType = affiliationType;
        this.serviceType = serviceType;
        this.estimatedCost = estimatedCost;
    }

    // Getters y Setters
    public String getPatientDocumentNumber() {
        return patientDocumentNumber;
    }

    public void setPatientDocumentNumber(String patientDocumentNumber) {
        this.patientDocumentNumber = patientDocumentNumber;
    }

    public String getAffiliationType() {
        return affiliationType;
    }

    public void setAffiliationType(String affiliationType) {
        this.affiliationType = affiliationType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
}