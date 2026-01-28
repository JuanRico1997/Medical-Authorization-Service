package com.meditrack.authorization.infrastructure.adapters.in.rest.controller;

import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.command.RegisterPatientCommand;
import com.meditrack.authorization.domain.ports.in.command.UpdatePatientCommand;
import com.meditrack.authorization.domain.ports.in.query.GetPatientByIdQuery;
import com.meditrack.authorization.domain.ports.in.useCase.*;
import com.meditrack.authorization.infrastructure.adapters.in.rest.dto.PatientResponse;
import com.meditrack.authorization.infrastructure.adapters.in.rest.dto.RegisterPatientRequest;
import com.meditrack.authorization.infrastructure.adapters.in.rest.dto.UpdatePatientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST: Gestión de Pacientes
 * Endpoints protegidos con JWT
 */
@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Endpoints de gestión de pacientes")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {

    private final RegisterPatientUseCase registerPatientUseCase;
    private final UpdatePatientUseCase updatePatientUseCase;
    private final GetPatientByIdUseCase getPatientByIdUseCase;
    private final ListPatientsUseCase listPatientsUseCase;
    private final DeactivatePatientUseCase deactivatePatientUseCase;

    public PatientController(
            RegisterPatientUseCase registerPatientUseCase,
            UpdatePatientUseCase updatePatientUseCase,
            GetPatientByIdUseCase getPatientByIdUseCase,
            ListPatientsUseCase listPatientsUseCase,
            DeactivatePatientUseCase deactivatePatientUseCase) {
        this.registerPatientUseCase = registerPatientUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
        this.getPatientByIdUseCase = getPatientByIdUseCase;
        this.listPatientsUseCase = listPatientsUseCase;
        this.deactivatePatientUseCase = deactivatePatientUseCase;
    }

    /**
     * POST /api/patients
     * Registra un nuevo paciente (solo Admin y Médico)
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MEDICO')")
    @Operation(
            summary = "Registrar nuevo paciente",
            description = "Crea un nuevo paciente y automáticamente genera un usuario con ROLE_PACIENTE. Solo accesible para ADMIN y MEDICO."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Paciente registrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos (documento o username duplicado)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado (requiere rol ADMIN o MEDICO)",
                    content = @Content
            )
    })
    public ResponseEntity<PatientResponse> registerPatient(
            @Valid @RequestBody RegisterPatientRequest request) {

        // 🔥 DEBUG CLAVE
        System.out.println("AUTH => " +
                org.springframework.security.core.context.SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        // 1. Crear el command desde el request
        RegisterPatientCommand command = new RegisterPatientCommand(
                request.getDocumentNumber(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getAffiliationType(),
                request.getAffiliationDate(),
                request.getUsername(),
                request.getPassword()
        );

        // 2. Ejecutar el caso de uso
        Patient patient = registerPatientUseCase.execute(command);

        // 3. Crear la respuesta
        PatientResponse response = PatientResponse.fromDomain(patient);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/patients
     * Lista todos los pacientes
     */
    @GetMapping
    @Operation(
            summary = "Listar pacientes",
            description = "Lista todos los pacientes activos. ADMIN y MEDICO ven todos, PACIENTE solo ve su propia información."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pacientes obtenida exitosamente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado (requiere autenticación)",
                    content = @Content
            )
    })
    public ResponseEntity<List<PatientResponse>> listPatients() {

        // 1. Ejecutar el caso de uso
        List<Patient> patients = listPatientsUseCase.execute();

        // 2. Convertir a DTOs
        List<PatientResponse> response = patients.stream()
                .map(PatientResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/patients/{id}
     * Obtiene un paciente por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener paciente por ID",
            description = "Obtiene la información de un paciente específico. ADMIN y MEDICO pueden ver cualquier paciente, PACIENTE solo puede ver su propia información."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado (sin permisos para ver este paciente)",
                    content = @Content
            )
    })
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable UUID id) {

        // 1. Crear el query
        GetPatientByIdQuery query = new GetPatientByIdQuery(id);

        // 2. Ejecutar el caso de uso
        Patient patient = getPatientByIdUseCase.execute(query);

        // 3. Crear la respuesta
        PatientResponse response = PatientResponse.fromDomain(patient);

        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/patients/{id}
     * Actualiza un paciente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar paciente",
            description = "Actualiza la información de un paciente. ADMIN y MEDICO pueden actualizar cualquier paciente, PACIENTE solo puede actualizar su propia información."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado (sin permisos para actualizar este paciente)",
                    content = @Content
            )
    })
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePatientRequest request) {

        // 1. Crear el command desde el request
        UpdatePatientCommand command = new UpdatePatientCommand(
                id,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone()
        );

        // 2. Ejecutar el caso de uso
        Patient patient = updatePatientUseCase.execute(command);

        // 3. Crear la respuesta
        PatientResponse response = PatientResponse.fromDomain(patient);

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/patients/{id}
     * Desactiva un paciente (soft delete)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Desactivar paciente",
            description = "Desactiva un paciente (soft delete). Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Paciente desactivado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado (requiere rol ADMIN)",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deactivatePatient(@PathVariable UUID id) {

        // 1. Ejecutar el caso de uso
        deactivatePatientUseCase.execute(id);

        return ResponseEntity.noContent().build();
    }
}