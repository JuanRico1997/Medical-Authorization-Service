package com.meditrack.authorization.infrastructure.adapters.in.rest;

import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.command.CreateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.in.useCase.CreateMedicalAuthorizationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests básicos para validar lógica de comandos
 */
@DisplayName("MedicalAuthorization Command Tests")
class MedicalAuthorizationCommandTest {

    @Test
    @DisplayName("Debe crear CreateMedicalAuthorizationCommand válido")
    void shouldCreateValidCommand() {
        // Given
        UUID patientId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();
        ServiceType serviceType = ServiceType.CONSULTA;
        String description = "Consulta de seguimiento por dolor lumbar crónico";

        // When
        CreateMedicalAuthorizationCommand command = new CreateMedicalAuthorizationCommand(
                patientId,
                serviceType,
                description,
                requestedBy
        );

        // Then
        assertThat(command).isNotNull();
        assertThat(command.getPatientId()).isEqualTo(patientId);
        assertThat(command.getServiceType()).isEqualTo(serviceType);
        assertThat(command.getDescription()).isEqualTo(description);
        assertThat(command.getRequestedBy()).isEqualTo(requestedBy);
    }

    @Test
    @DisplayName("Debe fallar al crear comando sin patientId")
    void shouldFailWhenCreatingCommandWithoutPatientId() {
        // Given
        UUID requestedBy = UUID.randomUUID();
        ServiceType serviceType = ServiceType.CONSULTA;
        String description = "Consulta de seguimiento";

        // When - Then
        assertThatThrownBy(() -> new CreateMedicalAuthorizationCommand(
                null,
                serviceType,
                description,
                requestedBy
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("paciente");
    }

    @Test
    @DisplayName("Debe fallar al crear comando sin serviceType")
    void shouldFailWhenCreatingCommandWithoutServiceType() {
        // Given
        UUID patientId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();
        String description = "Consulta de seguimiento";

        // When - Then
        assertThatThrownBy(() -> new CreateMedicalAuthorizationCommand(
                patientId,
                null,
                description,
                requestedBy
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("servicio");
    }

    @Test
    @DisplayName("Debe fallar al crear comando con descripción vacía")
    void shouldFailWhenCreatingCommandWithEmptyDescription() {
        // Given
        UUID patientId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();
        ServiceType serviceType = ServiceType.CONSULTA;

        // When - Then
        assertThatThrownBy(() -> new CreateMedicalAuthorizationCommand(
                patientId,
                serviceType,
                "",
                requestedBy
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("descripción");
    }

    @Test
    @DisplayName("Debe fallar al crear comando con descripción muy corta")
    void shouldFailWhenCreatingCommandWithShortDescription() {
        // Given
        UUID patientId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();
        ServiceType serviceType = ServiceType.CONSULTA;

        // When - Then
        assertThatThrownBy(() -> new CreateMedicalAuthorizationCommand(
                patientId,
                serviceType,
                "Corta",
                requestedBy
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("10 caracteres");
    }

    @Test
    @DisplayName("Debe fallar al crear comando sin requestedBy")
    void shouldFailWhenCreatingCommandWithoutRequestedBy() {
        // Given
        UUID patientId = UUID.randomUUID();
        ServiceType serviceType = ServiceType.CONSULTA;
        String description = "Consulta de seguimiento";

        // When - Then
        assertThatThrownBy(() -> new CreateMedicalAuthorizationCommand(
                patientId,
                serviceType,
                description,
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("solicitante");
    }

    @Test
    @DisplayName("Debe crear MedicalAuthorization válida")
    void shouldCreateValidMedicalAuthorization() {
        // Given
        UUID patientId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();
        ServiceType serviceType = ServiceType.CONSULTA;
        String description = "Consulta de seguimiento por dolor lumbar crónico";

        // When
        MedicalAuthorization authorization = new MedicalAuthorization(
                patientId,
                serviceType,
                description,
                requestedBy
        );

        // Then
        assertThat(authorization).isNotNull();
        assertThat(authorization.getId()).isNotNull();
        assertThat(authorization.getPatientId()).isEqualTo(patientId);
        assertThat(authorization.getServiceType()).isEqualTo(serviceType);
        assertThat(authorization.getDescription()).isEqualTo(description);
        assertThat(authorization.getRequestedBy()).isEqualTo(requestedBy);
        assertThat(authorization.isDeleted()).isFalse();
    }
}
