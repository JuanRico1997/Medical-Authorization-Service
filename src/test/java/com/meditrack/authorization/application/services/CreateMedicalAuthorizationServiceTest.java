package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.exceptions.BusinessRuleException;
import com.meditrack.authorization.domain.exceptions.ResourceNotFoundException;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.models.User;
import com.meditrack.authorization.domain.ports.in.command.CreateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.out.MedicalAuthorizationRepositoryPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private CreateMedicalAuthorizationService service;

    private UUID patientId;
    private UUID requestedBy;
    private Patient activePatient;
    private User mockUser;
    private CreateMedicalAuthorizationCommand validCommand;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
        requestedBy = UUID.randomUUID();

        activePatient = new Patient(
                "1000111222",
                "Carlos",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                AffiliationType.CONTRIBUTIVO,
                LocalDate.now()
        );

        mockUser = mock(User.class);

        validCommand = new CreateMedicalAuthorizationCommand(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento por dolor lumbar crónico",
                requestedBy
        );
    }

    @Test
    @DisplayName("Debe crear autorización exitosamente cuando todos los datos son válidos")
    void shouldCreateAuthorizationSuccessfully() {
        // Given
        MedicalAuthorization expectedAuthorization = new MedicalAuthorization(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento por dolor lumbar crónico",
                requestedBy
        );

        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.of(activePatient));
        when(userRepository.findById(requestedBy))
                .thenReturn(Optional.of(mockUser));
        when(authorizationRepository.save(any(MedicalAuthorization.class)))
                .thenReturn(expectedAuthorization);

        // When
        MedicalAuthorization result = service.execute(validCommand);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPatientId()).isEqualTo(patientId);
        assertThat(result.getServiceType()).isEqualTo(ServiceType.CONSULTA);
        assertThat(result.getDescription()).contains("dolor lumbar");
        assertThat(result.getRequestedBy()).isEqualTo(requestedBy);

        verify(patientRepository).findByIdAndNotDeleted(patientId);
        verify(userRepository).findById(requestedBy);
        verify(authorizationRepository).save(any(MedicalAuthorization.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el paciente no existe")
    void shouldThrowExceptionWhenPatientNotFound() {
        // Given
        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> service.execute(validCommand))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente");

        verify(patientRepository).findByIdAndNotDeleted(patientId);
        verify(userRepository, never()).findById(any());
        verify(authorizationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el usuario no existe")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.of(activePatient));
        when(userRepository.findById(requestedBy))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> service.execute(validCommand))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario");

        verify(patientRepository).findByIdAndNotDeleted(patientId);
        verify(userRepository).findById(requestedBy);
        verify(authorizationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar BusinessRuleException cuando el paciente está inactivo")
    void shouldThrowExceptionWhenPatientIsInactive() {
        // Given
        Patient inactivePatient = new Patient(
                "1000111222",
                "Carlos",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                AffiliationType.CONTRIBUTIVO,
                LocalDate.now()
        );
        inactivePatient.deactivate();

        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.of(inactivePatient));
        when(userRepository.findById(requestedBy))
                .thenReturn(Optional.of(mockUser));

        // When - Then
        assertThatThrownBy(() -> service.execute(validCommand))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("no está activo");  // Cambiar aquí

        verify(patientRepository).findByIdAndNotDeleted(patientId);
        verify(userRepository).findById(requestedBy);
        verify(authorizationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe crear autorización para paciente SUBSIDIADO")
    void shouldCreateAuthorizationForSubsidizedPatient() {
        // Given
        Patient subsidizedPatient = new Patient(
                "2000111222",
                "María",
                "López",
                "maria@example.com",
                "3009876543",
                AffiliationType.SUBSIDIADO,
                LocalDate.now()
        );

        MedicalAuthorization expectedAuthorization = new MedicalAuthorization(
                patientId,
                ServiceType.PROCEDIMIENTO,
                "Procedimiento médico urgente",
                requestedBy
        );

        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.of(subsidizedPatient));
        when(userRepository.findById(requestedBy))
                .thenReturn(Optional.of(mockUser));
        when(authorizationRepository.save(any(MedicalAuthorization.class)))
                .thenReturn(expectedAuthorization);

        CreateMedicalAuthorizationCommand command = new CreateMedicalAuthorizationCommand(
                patientId,
                ServiceType.PROCEDIMIENTO,
                "Procedimiento médico urgente",
                requestedBy
        );

        // When
        MedicalAuthorization result = service.execute(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getServiceType()).isEqualTo(ServiceType.PROCEDIMIENTO);

        verify(authorizationRepository).save(any(MedicalAuthorization.class));
    }

    @Test
    @DisplayName("Debe crear autorización para CIRUGÍA")
    void shouldCreateAuthorizationForSurgery() {
        // Given
        MedicalAuthorization expectedAuthorization = new MedicalAuthorization(
                patientId,
                ServiceType.CIRUGIA,
                "Cirugía de apendicitis",
                requestedBy
        );

        when(patientRepository.findByIdAndNotDeleted(patientId))
                .thenReturn(Optional.of(activePatient));
        when(userRepository.findById(requestedBy))
                .thenReturn(Optional.of(mockUser));
        when(authorizationRepository.save(any(MedicalAuthorization.class)))
                .thenReturn(expectedAuthorization);

        CreateMedicalAuthorizationCommand surgeryCommand = new CreateMedicalAuthorizationCommand(
                patientId,
                ServiceType.CIRUGIA,
                "Cirugía de apendicitis",
                requestedBy
        );

        // When
        MedicalAuthorization result = service.execute(surgeryCommand);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getServiceType()).isEqualTo(ServiceType.CIRUGIA);
        assertThat(result.getDescription()).contains("apendicitis");

        verify(authorizationRepository).save(any(MedicalAuthorization.class));
    }
}