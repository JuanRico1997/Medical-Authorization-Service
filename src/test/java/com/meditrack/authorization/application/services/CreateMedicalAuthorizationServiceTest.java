package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.exceptions.ResourceNotFoundException;
import com.meditrack.authorization.domain.exceptions.BusinessRuleException;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.models.User;
import com.meditrack.authorization.domain.ports.in.CreateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.out.MedicalAuthorizationRepositoryPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import com.meditrack.authorization.infrastructure.config.metrics.CustomMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CreateMedicalAuthorizationService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateMedicalAuthorizationService Tests")
class CreateMedicalAuthorizationServiceTest {

    @Mock
    private MedicalAuthorizationRepositoryPort authorizationRepository;

    @Mock
    private PatientRepositoryPort patientRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private CustomMetrics customMetrics;

    private CreateMedicalAuthorizationService service;

    private UUID patientId;
    private UUID requestedBy;
    private Patient mockPatient;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Crear el servicio con los mocks
        service = new CreateMedicalAuthorizationService(
                authorizationRepository,
                patientRepository,
                userRepository,
                customMetrics
        );

        patientId = UUID.randomUUID();
        requestedBy = UUID.randomUUID();

        // Mock Patient
        mockPatient = new Patient(
                "1000111222",
                "Carlos",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                AffiliationType.CONTRIBUTIVO,
                LocalDate.now()
        );

        // Mock User
        mockUser = mock(User.class);
    }

    @Test
    @DisplayName("Debe crear autorización exitosamente")
    void shouldCreateAuthorizationSuccessfully() {
        // Given
        CreateMedicalAuthorizationCommand command = new CreateMedicalAuthorizationCommand(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento por dolor lumbar crónico",
                requestedBy
        );

        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.of(mockPatient));
        when(userRepository.findById(requestedBy))
                .thenReturn(Optional.of(mockUser));
        when(authorizationRepository.save(any(MedicalAuthorization.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        MedicalAuthorization result = service.execute(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPatientId()).isEqualTo(patientId);
        assertThat(result.getServiceType()).isEqualTo(ServiceType.CONSULTA);
        assertThat(result.getDescription()).contains("dolor lumbar");
        assertThat(result.getRequestedBy()).isEqualTo(requestedBy);

        verify(patientRepository).findByIdAndNotDeleted(patientId);
        verify(userRepository).findById(requestedBy);
        verify(authorizationRepository).save(any(MedicalAuthorization.class));
        verify(customMetrics).incrementAuthorizationsCreated();
    }

    @Test
    @DisplayName("Debe fallar cuando el paciente no existe")
    void shouldFailWhenPatientNotFound() {
        // Given
        CreateMedicalAuthorizationCommand command = new CreateMedicalAuthorizationCommand(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento por dolor lumbar crónico",
                requestedBy
        );

        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(patientRepository).findByIdAndNotDeleted(patientId);
        verify(authorizationRepository, never()).save(any());
        verify(customMetrics, never()).incrementAuthorizationsCreated();
    }

    @Test
    @DisplayName("Debe fallar cuando el usuario solicitante no existe")
    void shouldFailWhenRequesterNotFound() {
        // Given
        CreateMedicalAuthorizationCommand command = new CreateMedicalAuthorizationCommand(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento por dolor lumbar crónico",
                requestedBy
        );

        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.of(mockPatient));
        when(userRepository.findById(requestedBy))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository).findById(requestedBy);
        verify(authorizationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe fallar cuando el paciente está inactivo")
    void shouldFailWhenPatientIsInactive() {
        // Given
        mockPatient.deactivate(); // Desactivar paciente

        CreateMedicalAuthorizationCommand command = new CreateMedicalAuthorizationCommand(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento",
                requestedBy
        );

        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.of(mockPatient));
        when(userRepository.findById(requestedBy))
                .thenReturn(Optional.of(mockUser));

        // When - Then
        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("no está activo");

        verify(authorizationRepository, never()).save(any());
    }
}