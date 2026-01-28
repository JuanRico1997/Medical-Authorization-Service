package com.meditrack.authorization.infrastructure.adapters.out.persistence;

import com.meditrack.authorization.domain.enums.UserRole;
import com.meditrack.authorization.domain.models.User;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.entity.UserEntity;
import com.meditrack.authorization.infrastructure.adapters.out.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para User
 * Implementa el puerto OUT UserRepositoryPort
 * Traduce entre modelos de dominio y entidades JPA
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.fromDomain(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByPatientId(UUID patientId) {
        return jpaRepository.findByPatientId(patientId)
                .map(UserEntity::toDomain);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return jpaRepository.findByRole(role).stream()
                .map(UserEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllActive() {
        return jpaRepository.findByActiveTrue().stream()
                .map(UserEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}