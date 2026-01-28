package com.meditrack.authorization.domain.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoverageEvaluationTest {

    @Test
    @DisplayName("Should create new coverage evaluation with valid data")
    void shouldCreateNewCoverageEvaluation() {
        // Arrange
        UUID authorizationId = UUID.randomUUID();
        int coveragePercentage = 80;
        BigDecimal copayAmount = new BigDecimal("50000.00");
        String insuranceResponse = "{\"status\":\"approved\",\"coverage\":80}";

        // Act
        CoverageEvaluation coverage = new CoverageEvaluation(
                authorizationId,
                coveragePercentage,
                copayAmount,
                true,
                insuranceResponse
        );

        // Assert
        assertThat(coverage.getId()).isNotNull();
        assertThat(coverage.getAuthorizationId()).isEqualTo(authorizationId);
        assertThat(coverage.getCoveragePercentage()).isEqualTo(80);
        assertThat(coverage.getCopayAmount()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(coverage.isApproved()).isTrue();
        assertThat(coverage.getEvaluationDate()).isNotNull();
        assertThat(coverage.getInsuranceResponse()).isEqualTo(insuranceResponse);
    }

    @Test
    @DisplayName("Should fail when authorizationId is null")
    void shouldFailWhenAuthorizationIdIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new CoverageEvaluation(
                null,
                80,
                new BigDecimal("50000.00"),
                true,
                "{}"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El ID de la autorización es obligatorio");
    }

    @Test
    @DisplayName("Should fail when coverage percentage is negative")
    void shouldFailWhenCoveragePercentageIsNegative() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new CoverageEvaluation(
                UUID.randomUUID(),
                -10,
                new BigDecimal("50000.00"),
                false,
                "{}"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El porcentaje de cobertura debe estar entre 0 y 100");
    }

    @Test
    @DisplayName("Should fail when coverage percentage exceeds 100")
    void shouldFailWhenCoveragePercentageExceeds100() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new CoverageEvaluation(
                UUID.randomUUID(),
                150,
                new BigDecimal("50000.00"),
                false,
                "{}"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El porcentaje de cobertura debe estar entre 0 y 100");
    }

    @Test
    @DisplayName("Should fail when copay amount is null")
    void shouldFailWhenCopayAmountIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new CoverageEvaluation(
                UUID.randomUUID(),
                80,
                null,
                true,
                "{}"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El monto de copago es obligatorio");
    }

    @Test
    @DisplayName("Should fail when copay amount is negative")
    void shouldFailWhenCopayAmountIsNegative() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new CoverageEvaluation(
                UUID.randomUUID(),
                80,
                new BigDecimal("-1000.00"),
                true,
                "{}"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El monto de copago no puede ser negativo");
    }

    @Test
    @DisplayName("Should verify coverage meets minimum requirement")
    void shouldVerifyCoverageMeetsRequirement() {
        // Arrange
        CoverageEvaluation coverage = new CoverageEvaluation(
                UUID.randomUUID(),
                80,
                new BigDecimal("50000.00"),
                true,
                "{}"
        );

        // Act & Assert
        assertThat(coverage.meetsCoverageRequirement(70)).isTrue();
        assertThat(coverage.meetsCoverageRequirement(80)).isTrue();
        assertThat(coverage.meetsCoverageRequirement(90)).isFalse();
    }

    @Test
    @DisplayName("Should fail when checking invalid minimum requirement")
    void shouldFailWhenCheckingInvalidMinimumRequirement() {
        // Arrange
        CoverageEvaluation coverage = new CoverageEvaluation(
                UUID.randomUUID(),
                80,
                new BigDecimal("50000.00"),
                true,
                "{}"
        );

        // Act & Assert
        assertThatThrownBy(() -> coverage.meetsCoverageRequirement(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El porcentaje mínimo debe estar entre 0 y 100");

        assertThatThrownBy(() -> coverage.meetsCoverageRequirement(150))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El porcentaje mínimo debe estar entre 0 y 100");
    }

    @Test
    @DisplayName("Should verify if exceeds max copay")
    void shouldVerifyExceedsMaxCopay() {
        // Arrange
        CoverageEvaluation coverage = new CoverageEvaluation(
                UUID.randomUUID(),
                70, // 30% copay
                new BigDecimal("150000.00"),
                false,
                "{}"
        );

        // Act & Assert
        assertThat(coverage.exceedsMaxCopay(20)).isTrue(); // 30% > 20%
        assertThat(coverage.exceedsMaxCopay(30)).isFalse(); // 30% = 30%
        assertThat(coverage.exceedsMaxCopay(40)).isFalse(); // 30% < 40%
    }

    @Test
    @DisplayName("Should calculate copay percentage correctly")
    void shouldCalculateCopayPercentage() {
        // Arrange
        CoverageEvaluation coverage80 = new CoverageEvaluation(
                UUID.randomUUID(),
                80,
                new BigDecimal("50000.00"),
                true,
                "{}"
        );

        CoverageEvaluation coverage100 = new CoverageEvaluation(
                UUID.randomUUID(),
                100,
                BigDecimal.ZERO,
                true,
                "{}"
        );

        CoverageEvaluation coverage0 = new CoverageEvaluation(
                UUID.randomUUID(),
                0,
                new BigDecimal("500000.00"),
                false,
                "{}"
        );

        // Act & Assert
        assertThat(coverage80.getCopayPercentage()).isEqualTo(20);
        assertThat(coverage100.getCopayPercentage()).isEqualTo(0);
        assertThat(coverage0.getCopayPercentage()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should verify belongs to authorization")
    void shouldVerifyBelongsToAuthorization() {
        // Arrange
        UUID authorizationId = UUID.randomUUID();
        CoverageEvaluation coverage = new CoverageEvaluation(
                authorizationId,
                80,
                new BigDecimal("50000.00"),
                true,
                "{}"
        );

        // Act & Assert
        assertThat(coverage.belongsToAuthorization(authorizationId)).isTrue();
        assertThat(coverage.belongsToAuthorization(UUID.randomUUID())).isFalse();
    }

    @Test
    @DisplayName("Should get readable summary")
    void shouldGetReadableSummary() {
        // Arrange
        CoverageEvaluation approved = new CoverageEvaluation(
                UUID.randomUUID(),
                80,
                new BigDecimal("50000.50"),
                true,
                "{}"
        );

        CoverageEvaluation rejected = new CoverageEvaluation(
                UUID.randomUUID(),
                40,
                new BigDecimal("300000.00"),
                false,
                "{}"
        );

        // Act
        String approvedSummary = approved.getSummary();
        String rejectedSummary = rejected.getSummary();

        // Assert
        assertThat(approvedSummary).contains("80%", "20%", "50000.50", "APROBADO");
        assertThat(rejectedSummary).contains("40%", "60%", "300000.00", "RECHAZADO");
    }

    @Test
    @DisplayName("Should handle zero copay amount")
    void shouldHandleZeroCopayAmount() {
        // Arrange & Act
        CoverageEvaluation coverage = new CoverageEvaluation(
                UUID.randomUUID(),
                100,
                BigDecimal.ZERO,
                true,
                "{}"
        );

        // Assert
        assertThat(coverage.getCopayAmount()).isEqualTo(BigDecimal.ZERO);
        assertThat(coverage.isApproved()).isTrue();
    }

    @Test
    @DisplayName("Should reconstruct from persistence")
    void shouldReconstructFromPersistence() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID authorizationId = UUID.randomUUID();
        LocalDateTime evaluationDate = LocalDateTime.now();

        // Act
        CoverageEvaluation coverage = new CoverageEvaluation(
                id,
                authorizationId,
                85,
                new BigDecimal("75000.00"),
                true,
                evaluationDate,
                "{\"status\":\"approved\"}"
        );

        // Assert
        assertThat(coverage.getId()).isEqualTo(id);
        assertThat(coverage.getAuthorizationId()).isEqualTo(authorizationId);
        assertThat(coverage.getEvaluationDate()).isEqualTo(evaluationDate);
    }
}