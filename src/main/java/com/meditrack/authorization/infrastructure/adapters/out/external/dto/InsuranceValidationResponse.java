package com.meditrack.authorization.infrastructure.adapters.out.external.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO: Response del servicio externo de validación de seguros
 */
public class InsuranceValidationResponse {

    private boolean approved;
    private int coveragePercentage;
    private BigDecimal copayAmount;
    private BigDecimal coveredAmount;
    private BigDecimal totalCost;
    private String message;
    private String authorizationCode;
    private LocalDateTime validationDate;

    // Constructores
    public InsuranceValidationResponse() {
    }

    public InsuranceValidationResponse(boolean approved, int coveragePercentage,
                                       BigDecimal copayAmount, BigDecimal coveredAmount,
                                       BigDecimal totalCost, String message,
                                       String authorizationCode, LocalDateTime validationDate) {
        this.approved = approved;
        this.coveragePercentage = coveragePercentage;
        this.copayAmount = copayAmount;
        this.coveredAmount = coveredAmount;
        this.totalCost = totalCost;
        this.message = message;
        this.authorizationCode = authorizationCode;
        this.validationDate = validationDate;
    }

    // Getters y Setters
    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public int getCoveragePercentage() {
        return coveragePercentage;
    }

    public void setCoveragePercentage(int coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
    }

    public BigDecimal getCopayAmount() {
        return copayAmount;
    }

    public void setCopayAmount(BigDecimal copayAmount) {
        this.copayAmount = copayAmount;
    }

    public BigDecimal getCoveredAmount() {
        return coveredAmount;
    }

    public void setCoveredAmount(BigDecimal coveredAmount) {
        this.coveredAmount = coveredAmount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public LocalDateTime getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(LocalDateTime validationDate) {
        this.validationDate = validationDate;
    }
}