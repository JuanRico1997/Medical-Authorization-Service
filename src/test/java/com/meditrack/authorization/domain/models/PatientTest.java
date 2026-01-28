package com.meditrack.authorization.domain.models;

import com.meditrack.authorization.domain.enums.AffiliationStatus;
import com.meditrack.authorization.domain.enums.AffiliationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitarios para el modelo Patient
 */
@DisplayName("Patient Domain Model Tests")
class PatientTest {

    @Test
    @DisplayName("Debe crear un paciente válido")
    void shouldCreateValidPatient() {
        // Given
        String documentNumber = "1000111222";
        String firstName = "Carlos";
        String lastName = "Ramírez";
        String email = "carlos@example.com";
        String phone = "3001234567";
        AffiliationType affiliationType = AffiliationType.CONTRIBUTIVO;
        LocalDate affiliationDate = LocalDate.of(2024, 1, 15);

        // When
        Patient patient = new Patient(
                documentNumber,
                firstName,
                lastName,
                email,
                phone,
                affiliationType,
                affiliationDate
        );

        // Then
        assertThat(patient).isNotNull();
        assertThat(patient.getId()).isNotNull();
        assertThat(patient.getDocumentNumber()).isEqualTo(documentNumber);
        assertThat(patient.getFirstName()).isEqualTo(firstName);
        assertThat(patient.getLastName()).isEqualTo(lastName);
        assertThat(patient.getFullName()).isEqualTo("Carlos Ramírez");
        assertThat(patient.getEmail()).isEqualTo(email);
        assertThat(patient.getAffiliationType()).isEqualTo(affiliationType);
        assertThat(patient.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("Debe fallar al crear paciente sin documento")
    void shouldFailWhenCreatingPatientWithoutDocument() {
        // Given - When - Then
        assertThatThrownBy(() -> new Patient(
                null,
                "Carlos",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                AffiliationType.CONTRIBUTIVO,
                LocalDate.now()
        ))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Debe fallar al crear paciente con nombre muy corto")
    void shouldFailWhenCreatingPatientWithShortName() {
        // Given - When - Then
        assertThatThrownBy(() -> new Patient(
                "1000111222",
                "C",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                AffiliationType.CONTRIBUTIVO,
                LocalDate.now()
        ))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Debe desactivar paciente correctamente")
    void shouldDeactivatePatient() {
        // Given
        Patient patient = createValidPatient();

        // When
        patient.deactivate();

        // Then
        assertThat(patient.getAffiliationStatus()).isEqualTo(AffiliationStatus.INACTIVE);
    }

    @Test
    @DisplayName("Debe verificar si el paciente está activo")
    void shouldCheckIfPatientIsActive() {
        // Given
        Patient patient = createValidPatient();

        // When
        boolean isActive = patient.isActive();

        // Then
        assertThat(isActive).isTrue();
    }

    @Test
    @DisplayName("Debe calcular porcentaje máximo de copago según tipo de afiliación")
    void shouldCalculateMaxCopayPercentageByAffiliationType() {
        // Given
        Patient contributivo = new Patient(
                "1000111222", "Carlos", "Ramírez", "carlos@example.com", "3001234567",
                AffiliationType.CONTRIBUTIVO, LocalDate.now()
        );

        Patient subsidiado = new Patient(
                "2000222333", "María", "Torres", "maria@example.com", "3009876543",
                AffiliationType.SUBSIDIADO, LocalDate.now()
        );

        Patient especial = new Patient(
                "3000333444", "Luis", "Mendoza", "luis@example.com", "3005554433",
                AffiliationType.ESPECIAL, LocalDate.now()
        );

        // When - Then
        assertThat(contributivo.getMaxCopayPercentage()).isEqualTo(20);
        assertThat(subsidiado.getMaxCopayPercentage()).isEqualTo(5);
        assertThat(especial.getMaxCopayPercentage()).isEqualTo(10);
    }

    // Helper method
    private Patient createValidPatient() {
        return new Patient(
                "1000111222",
                "Carlos",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                AffiliationType.CONTRIBUTIVO,
                LocalDate.of(2024, 1, 15)
        );
    }
}