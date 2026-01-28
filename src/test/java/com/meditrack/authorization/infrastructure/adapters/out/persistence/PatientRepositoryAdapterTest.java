package com.meditrack.authorization.infrastructure.adapters.out.persistence;

import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.enums.AffiliationStatus;
import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.adapter.PatientRepositoryAdapter;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.PatientEntity;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.repository.PatientJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de integración para PatientRepositoryAdapter
 */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("PatientRepositoryAdapter Integration Tests")
class PatientRepositoryAdapterTest {

    @Autowired
    private PatientJpaRepository jpaRepository;

    private PatientRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PatientRepositoryAdapter(jpaRepository);
    }

    @Test
    @DisplayName("Debe guardar un paciente correctamente")
    void shouldSavePatient() {
        // Given
        Patient patient = new Patient(
                "1000111222",
                "Carlos",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                AffiliationType.CONTRIBUTIVO,
                LocalDate.of(2024, 1, 15)
        );

        // When
        Patient savedPatient = adapter.save(patient);

        // Then
        assertThat(savedPatient).isNotNull();
        assertThat(savedPatient.getId()).isNotNull();
        assertThat(savedPatient.getDocumentNumber()).isEqualTo("1000111222");
        assertThat(savedPatient.getFirstName()).isEqualTo("Carlos");
        assertThat(savedPatient.getLastName()).isEqualTo("Ramírez");
        assertThat(savedPatient.getEmail()).isEqualTo("carlos@example.com");

        // Verificar que realmente se guardó en la base de datos
        Optional<PatientEntity> foundEntity = jpaRepository.findById(savedPatient.getId());
        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getDocumentNumber()).isEqualTo("1000111222");
    }

    @Test
    @DisplayName("Debe encontrar paciente por ID cuando no está eliminado")
    void shouldFindPatientById() {
        // Given
        PatientEntity entity = createPatientEntity(
                "1000111222",
                "Carlos",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                false
        );
        PatientEntity saved = jpaRepository.save(entity);

        // When
        Optional<Patient> found = adapter.findByIdAndNotDeleted(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getDocumentNumber()).isEqualTo("1000111222");
        assertThat(found.get().getFirstName()).isEqualTo("Carlos");
        assertThat(found.get().getLastName()).isEqualTo("Ramírez");
    }

    @Test
    @DisplayName("Debe verificar si existe por número de documento")
    void shouldCheckIfExistsByDocumentNumber() {
        // Given
        PatientEntity entity = createPatientEntity(
                "1000111222",
                "Carlos",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                false
        );
        jpaRepository.save(entity);

        // When
        boolean exists = adapter.existsByDocumentNumber("1000111222");
        boolean notExists = adapter.existsByDocumentNumber("9999999999");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("No debe encontrar pacientes eliminados")
    void shouldNotFindDeletedPatients() {
        // Given
        PatientEntity entity = createPatientEntity(
                "1000111222",
                "Carlos",
                "Ramírez",
                "carlos@example.com",
                "3001234567",
                true  // Marcado como eliminado
        );
        PatientEntity saved = jpaRepository.save(entity);

        // When
        Optional<Patient> found = adapter.findByIdAndNotDeleted(saved.getId());

        // Then
        assertThat(found).isEmpty();
    }

    // ========== Métodos auxiliares ==========

    /**
     * Método auxiliar para crear una entidad Patient de prueba
     */
    private PatientEntity createPatientEntity(
            String documentNumber,
            String firstName,
            String lastName,
            String email,
            String phone,
            boolean deleted
    ) {
        PatientEntity entity = new PatientEntity();
        entity.setId(UUID.randomUUID());
        entity.setDocumentNumber(documentNumber);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setPhone(phone);
        entity.setAffiliationType(AffiliationType.CONTRIBUTIVO);
        entity.setAffiliationStatus(AffiliationStatus.ACTIVE);
        entity.setAffiliationDate(LocalDate.of(2024, 1, 15));
        entity.setDeleted(deleted);
        return entity;
    }
}