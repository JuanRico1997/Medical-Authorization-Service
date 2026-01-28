package com.meditrack.authorization.infrastructure.adapters.out.persistence;

import com.meditrack.authorization.domain.enums.AffiliationType;
import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.adapter.PatientRepositoryAdapter;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.PatientEntity;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.repository.PatientJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
@DisplayName("PatientRepositoryAdapter Integration Tests")
class PatientRepositoryAdapterTest {

    @Autowired
    private TestEntityManager entityManager;

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

        // Verificar en la base de datos
        PatientEntity found = entityManager.find(PatientEntity.class, savedPatient.getId());
        assertThat(found).isNotNull();
        assertThat(found.getDocumentNumber()).isEqualTo("1000111222");
    }

    @Test
    @DisplayName("Debe encontrar paciente por ID")
    void shouldFindPatientById() {
        // Given
        PatientEntity entity = new PatientEntity();
        entity.setId(UUID.randomUUID());
        entity.setDocumentNumber("1000111222");
        entity.setFirstName("Carlos");
        entity.setLastName("Ramírez");
        entity.setEmail("carlos@example.com");
        entity.setPhone("3001234567");
        entity.setAffiliationType(AffiliationType.CONTRIBUTIVO);
        entity.setAffiliationDate(LocalDate.of(2024, 1, 15));
        entity.setDeleted(false);

        entityManager.persist(entity);
        entityManager.flush();

        // When
        Optional<Patient> found = adapter.findByIdAndNotDeleted(entity.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getDocumentNumber()).isEqualTo("1000111222");
        assertThat(found.get().getFirstName()).isEqualTo("Carlos");
    }

    @Test
    @DisplayName("Debe verificar si existe por número de documento")
    void shouldCheckIfExistsByDocumentNumber() {
        // Given
        PatientEntity entity = new PatientEntity();
        entity.setId(UUID.randomUUID());
        entity.setDocumentNumber("1000111222");
        entity.setFirstName("Carlos");
        entity.setLastName("Ramírez");
        entity.setEmail("carlos@example.com");
        entity.setAffiliationType(AffiliationType.CONTRIBUTIVO);
        entity.setAffiliationDate(LocalDate.now());
        entity.setDeleted(false);

        entityManager.persist(entity);
        entityManager.flush();

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
        PatientEntity entity = new PatientEntity();
        entity.setId(UUID.randomUUID());
        entity.setDocumentNumber("1000111222");
        entity.setFirstName("Carlos");
        entity.setLastName("Ramírez");
        entity.setEmail("carlos@example.com");
        entity.setAffiliationType(AffiliationType.CONTRIBUTIVO);
        entity.setAffiliationDate(LocalDate.now());
        entity.setDeleted(true); // Marcado como eliminado

        entityManager.persist(entity);
        entityManager.flush();

        // When
        Optional<Patient> found = adapter.findByIdAndNotDeleted(entity.getId());

        // Then
        assertThat(found).isEmpty();
    }
}