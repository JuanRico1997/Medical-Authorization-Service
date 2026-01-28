package com.meditrack.authorization.domain.models;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import com.meditrack.authorization.domain.enums.ServiceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MedicalAuthorizationTest {

    @Test
    @DisplayName("Should create new authorization with valid data")
    void shouldCreateNewAuthorization() {
        // Arrange
        UUID patientId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();
        ServiceType serviceType = ServiceType.CIRUGIA;
        String description = "Cirugia de emergencia programada para el 15 de febrero";

        // Act
        MedicalAuthorization auth = new MedicalAuthorization(
                patientId,
                serviceType,
                description,
                requestedBy
        );

        // Assert
        assertThat(auth.getId()).isNotNull();
        assertThat(auth.getPatientId()).isEqualTo(patientId);
        assertThat(auth.getServiceType()).isEqualTo(ServiceType.CIRUGIA);
        assertThat(auth.getDescription()).isEqualTo(description);
        assertThat(auth.getStatus()).isEqualTo(AuthorizationStatus.PENDIENTE);
        assertThat(auth.getRequestedBy()).isEqualTo(requestedBy);
        assertThat(auth.isDeleted()).isFalse();
        assertThat(auth.getRequestDate()).isNotNull();
    }

    @Test
    @DisplayName("Should fail when patientId is null")
    void shouldFailWhenPatientIdIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new MedicalAuthorization(
                null,
                ServiceType.CONSULTA,
                "Descripcion valida de al menos 10 caracteres",
                UUID.randomUUID()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El ID del paciente es obligatorio");
    }

    @Test
    @DisplayName("Should fail when serviceType is null")
    void shouldFailWhenServiceTypeIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new MedicalAuthorization(
                UUID.randomUUID(),
                null,
                "Descripcion valida de al menos 10 caracteres",
                UUID.randomUUID()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El tipo de servicio es obligatorio");
    }

    @Test
    @DisplayName("Should fail when description is too short")
    void shouldFailWhenDescriptionTooShort() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Corto",
                UUID.randomUUID()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La descripción debe tener al menos 10 caracteres");
    }

    @Test
    @DisplayName("Should fail when description is too long")
    void shouldFailWhenDescriptionTooLong() {
        // Arrange
        String longDescription = "A".repeat(501);

        // Act & Assert
        assertThatThrownBy(() -> new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                longDescription,
                UUID.randomUUID()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La descripción no puede exceder 500 caracteres");
    }

    @Test
    @DisplayName("Should approve authorization successfully")
    void shouldApproveAuthorization() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Consulta general programada para revision",
                UUID.randomUUID()
        );

        // Act
        auth.approve();

        // Assert
        assertThat(auth.getStatus()).isEqualTo(AuthorizationStatus.APROBADA);
    }

    @Test
    @DisplayName("Should reject authorization successfully")
    void shouldRejectAuthorization() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.PROCEDIMIENTO,
                "Procedimiento que requiere autorizacion especial",
                UUID.randomUUID()
        );

        // Act
        auth.reject();

        // Assert
        assertThat(auth.getStatus()).isEqualTo(AuthorizationStatus.RECHAZADA);
    }

    @Test
    @DisplayName("Should fail when approving already approved authorization")
    void shouldFailWhenApprovingApprovedAuth() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Consulta general programada para revision",
                UUID.randomUUID()
        );
        auth.approve();

        // Act & Assert
        assertThatThrownBy(() -> auth.approve())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("La autorización ya está aprobada");
    }

    @Test
    @DisplayName("Should fail when rejecting already rejected authorization")
    void shouldFailWhenRejectingRejectedAuth() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Consulta general que no cumple requisitos",
                UUID.randomUUID()
        );
        auth.reject();

        // Act & Assert
        assertThatThrownBy(() -> auth.reject())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("La autorización ya está rechazada");
    }

    @Test
    @DisplayName("Should mark authorization as under review")
    void shouldMarkAsUnderReview() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CIRUGIA,
                "Cirugia compleja que requiere revision adicional",
                UUID.randomUUID()
        );

        // Act
        auth.markAsUnderReview();

        // Assert
        assertThat(auth.getStatus()).isEqualTo(AuthorizationStatus.EN_REVISION);
    }

    @Test
    @DisplayName("Should fail when marking non-pending auth as under review")
    void shouldFailWhenMarkingNonPendingAsUnderReview() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Consulta ya revisada y aprobada previamente",
                UUID.randomUUID()
        );
        auth.approve();

        // Act & Assert
        assertThatThrownBy(() -> auth.markAsUnderReview())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Solo se pueden revisar autorizaciones en estado PENDIENTE");
    }

    @Test
    @DisplayName("Should delete pending authorization")
    void shouldDeletePendingAuthorization() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Consulta que sera cancelada por el paciente",
                UUID.randomUUID()
        );

        // Act
        auth.delete();

        // Assert
        assertThat(auth.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Should fail when deleting approved authorization")
    void shouldFailWhenDeletingApprovedAuth() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CIRUGIA,
                "Cirugia ya aprobada y programada exitosamente",
                UUID.randomUUID()
        );
        auth.approve();

        // Act & Assert
        assertThatThrownBy(() -> auth.delete())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se puede eliminar una autorización aprobada");
    }

    @Test
    @DisplayName("Should verify authorization belongs to patient")
    void shouldVerifyBelongsToPatient() {
        // Arrange
        UUID patientId = UUID.randomUUID();
        MedicalAuthorization auth = new MedicalAuthorization(
                patientId,
                ServiceType.CONSULTA,
                "Consulta general para verificar pertenencia",
                UUID.randomUUID()
        );

        // Act & Assert
        assertThat(auth.belongsToPatient(patientId)).isTrue();
        assertThat(auth.belongsToPatient(UUID.randomUUID())).isFalse();
    }

    @Test
    @DisplayName("Should verify authorization is in final state")
    void shouldVerifyFinalState() {
        // Arrange
        MedicalAuthorization pendingAuth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Consulta en estado pendiente inicial",
                UUID.randomUUID()
        );

        MedicalAuthorization approvedAuth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.PROCEDIMIENTO,
                "Procedimiento aprobado y confirmado exitosamente",
                UUID.randomUUID()
        );
        approvedAuth.approve();

        // Act & Assert
        assertThat(pendingAuth.isFinalState()).isFalse();
        assertThat(approvedAuth.isFinalState()).isTrue();
    }

    @Test
    @DisplayName("Should verify authorization can be modified")
    void shouldVerifyCanBeModified() {
        // Arrange
        MedicalAuthorization pendingAuth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Consulta pendiente que puede ser modificada",
                UUID.randomUUID()
        );

        MedicalAuthorization approvedAuth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.PROCEDIMIENTO,
                "Procedimiento aprobado que no puede modificarse",
                UUID.randomUUID()
        );
        approvedAuth.approve();

        // Act & Assert
        assertThat(pendingAuth.canBeModified()).isTrue();
        assertThat(approvedAuth.canBeModified()).isFalse();
    }

    @Test
    @DisplayName("Should get minimum coverage required by service type")
    void shouldGetMinimumCoverageRequired() {
        // Arrange
        MedicalAuthorization consulta = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Consulta general para verificar cobertura minima",
                UUID.randomUUID()
        );

        MedicalAuthorization procedimiento = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.PROCEDIMIENTO,
                "Procedimiento que requiere cobertura mayor",
                UUID.randomUUID()
        );

        MedicalAuthorization cirugia = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CIRUGIA,
                "Cirugia que requiere maxima cobertura posible",
                UUID.randomUUID()
        );

        // Act & Assert
        assertThat(consulta.getMinimumCoverageRequired()).isEqualTo(70);
        assertThat(procedimiento.getMinimumCoverageRequired()).isEqualTo(80);
        assertThat(cirugia.getMinimumCoverageRequired()).isEqualTo(90);
    }

    @Test
    @DisplayName("Should update description when authorization can be modified")
    void shouldUpdateDescription() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Descripcion original que sera actualizada pronto",
                UUID.randomUUID()
        );
        String newDescription = "Nueva descripcion actualizada correctamente ahora";

        // Act
        auth.updateDescription(newDescription);

        // Assert
        assertThat(auth.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("Should fail when updating description of non-modifiable auth")
    void shouldFailWhenUpdatingNonModifiableAuth() {
        // Arrange
        MedicalAuthorization auth = new MedicalAuthorization(
                UUID.randomUUID(),
                ServiceType.CONSULTA,
                "Consulta aprobada que no puede modificarse ya",
                UUID.randomUUID()
        );
        auth.approve();

        // Act & Assert
        assertThatThrownBy(() -> auth.updateDescription("Nueva descripcion invalida por estado"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Solo se pueden actualizar autorizaciones en estado PENDIENTE");
    }

    @Test
    @DisplayName("Should reconstruct authorization from persistence")
    void shouldReconstructFromPersistence() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();
        LocalDateTime requestDate = LocalDateTime.now();

        // Act
        MedicalAuthorization auth = new MedicalAuthorization(
                id,
                patientId,
                ServiceType.CIRUGIA,
                "Cirugia reconstruida desde la base de datos",
                requestDate,
                AuthorizationStatus.APROBADA,
                requestedBy,
                false
        );

        // Assert
        assertThat(auth.getId()).isEqualTo(id);
        assertThat(auth.getPatientId()).isEqualTo(patientId);
        assertThat(auth.getStatus()).isEqualTo(AuthorizationStatus.APROBADA);
        assertThat(auth.getRequestDate()).isEqualTo(requestDate);
    }
}