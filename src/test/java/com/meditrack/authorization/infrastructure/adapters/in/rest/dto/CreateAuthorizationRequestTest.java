package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import com.meditrack.authorization.domain.enums.ServiceType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateAuthorizationRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Should create valid request with all fields")
    void shouldCreateValidRequest() {
        // Arrange & Act
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.CIRUGIA)
                .description("Cirugia de emergencia programada para el dia 15 de febrero")                .build();

        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
        assertThat(request.getPatientId()).isNotNull();
        assertThat(request.getServiceType()).isEqualTo(ServiceType.CIRUGIA);

    }

    @Test
    @DisplayName("Should use setters and getters correctly")
    void shouldUseSettersAndGetters() {
        // Arrange
        CreateAuthorizationRequest request = new CreateAuthorizationRequest();
        UUID patientId = UUID.randomUUID();

        // Act
        request.setPatientId(patientId);
        request.setServiceType(ServiceType.CONSULTA);
        request.setDescription("Consulta general programada para revision medica");

        // Assert
        assertThat(request.getPatientId()).isEqualTo(patientId);
        assertThat(request.getServiceType()).isEqualTo(ServiceType.CONSULTA);
        assertThat(request.getDescription()).isEqualTo("Consulta general programada para revision medica");
    }

    @Test
    @DisplayName("Should create with constructor")
    void shouldCreateWithConstructor() {
        // Arrange
        UUID patientId = UUID.randomUUID();

        // Act
        CreateAuthorizationRequest request = new CreateAuthorizationRequest(
                patientId,
                ServiceType.PROCEDIMIENTO,
                "Procedimiento especializado de ortopedia para paciente"
        );

        // Assert
        assertThat(request.getPatientId()).isEqualTo(patientId);
        assertThat(request.getServiceType()).isEqualTo(ServiceType.PROCEDIMIENTO);
        assertThat(request.getDescription()).contains("Procedimiento");
    }

    @Test
    @DisplayName("Should fail validation when patientId is null")
    void shouldFailWhenPatientIdIsNull() {
        // Arrange & Act
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .serviceType(ServiceType.CIRUGIA)
                .description("Cirugia programada con todos los detalles necesarios")
                .build();

        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("patientId") &&
                        v.getMessage().equals("El ID del paciente es obligatorio")
        );
    }

    @Test
    @DisplayName("Should fail validation when serviceType is null")
    void shouldFailWhenServiceTypeIsNull() {
        // Arrange & Act
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .description("Descripcion valida con suficiente longitud para pasar")
                .build();

        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("serviceType") &&
                        v.getMessage().equals("El tipo de servicio es obligatorio")
        );
    }

    @Test
    @DisplayName("Should fail validation when description is null")
    void shouldFailWhenDescriptionIsNull() {
        // Arrange & Act
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.CONSULTA)
                .build();

        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("description")
        );
    }

    @Test
    @DisplayName("Should fail validation when description is blank")
    void shouldFailWhenDescriptionIsBlank() {
        // Arrange & Act
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.PROCEDIMIENTO)
                .description("   ")
                .build();

        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("description") &&
                        v.getMessage().equals("La descripción es obligatoria")
        );
    }

    @Test
    @DisplayName("Should fail validation when description is too short")
    void shouldFailWhenDescriptionTooShort() {
        // Arrange & Act
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.CIRUGIA)
                .description("Corto")
                .build();

        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("description") &&
                        v.getMessage().equals("La descripción debe tener entre 10 y 500 caracteres")
        );
    }

    @Test
    @DisplayName("Should fail validation when description is too long")
    void shouldFailWhenDescriptionTooLong() {
        // Arrange
        String longDescription = "A".repeat(501);

        // Act
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.CIRUGIA)
                .description(longDescription)
                .build();

        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("description") &&
                        v.getMessage().equals("La descripción debe tener entre 10 y 500 caracteres")
        );
    }

    @Test
    @DisplayName("Should accept minimum valid description length")
    void shouldAcceptMinimumValidLength() {
        // Arrange - Exactamente 10 caracteres
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.CONSULTA)
                .description("1234567890")
                .build();

        // Act
        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should accept maximum valid description length")
    void shouldAcceptMaximumValidLength() {
        // Arrange - Exactamente 500 caracteres
        String maxDescription = "A".repeat(500);
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.PROCEDIMIENTO)
                .description(maxDescription)
                .build();

        // Act
        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should accept all service types")
    void shouldAcceptAllServiceTypes() {
        // Arrange & Act
        CreateAuthorizationRequest consulta = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.CONSULTA)
                .description("Consulta general de seguimiento medico ambulatorio")
                .build();

        CreateAuthorizationRequest procedimiento = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.PROCEDIMIENTO)
                .description("Procedimiento especializado de cardiologia interventiva")
                .build();

        CreateAuthorizationRequest cirugia = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.CIRUGIA)
                .description("Cirugia mayor programada con hospitalizacion requerida")
                .build();

        // Assert
        assertThat(validator.validate(consulta)).isEmpty();
        assertThat(validator.validate(procedimiento)).isEmpty();
        assertThat(validator.validate(cirugia)).isEmpty();
    }

    @Test
    @DisplayName("Should handle valid description with special characters")
    void shouldHandleSpecialCharacters() {
        // Arrange & Act
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .patientId(UUID.randomUUID())
                .serviceType(ServiceType.CONSULTA)
                .description("Consulta: revisión (control) - seguimiento médico #123")
                .build();

        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should validate multiple fields at once when all are invalid")
    void shouldValidateMultipleFieldsAtOnce() {
        // Arrange & Act
        CreateAuthorizationRequest request = CreateAuthorizationRequest.builder()
                .build();

        Set<ConstraintViolation<CreateAuthorizationRequest>> violations =
                validator.validate(request);

        // Assert
        assertThat(violations).hasSize(3); // patientId, serviceType, description
    }
}