package com.meditrack.authorization.infrastructure.adapters.in.rest.controller;

import com.meditrack.authorization.domain.models.CoverageEvaluation;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.command.CreateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.in.useCase.CreateMedicalAuthorizationUseCase;
import com.meditrack.authorization.domain.ports.in.command.EvaluateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.in.useCase.EvaluateMedicalAuthorizationUseCase;
import com.meditrack.authorization.domain.ports.in.query.GetAuthorizationByIdQuery;
import com.meditrack.authorization.domain.ports.in.useCase.GetAuthorizationByIdUseCase;
import com.meditrack.authorization.domain.ports.in.query.ListAuthorizationsByPatientQuery;
import com.meditrack.authorization.domain.ports.in.useCase.ListAuthorizationsByPatientUseCase;
import com.meditrack.authorization.domain.ports.in.useCase.ListPendingAuthorizationsUseCase;
import com.meditrack.authorization.domain.ports.in.command.UpdateAuthorizationStatusCommand;
import com.meditrack.authorization.domain.ports.in.useCase.UpdateAuthorizationStatusUseCase;
import com.meditrack.authorization.domain.ports.in.command.CreateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.in.command.EvaluateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.in.command.UpdateAuthorizationStatusCommand;
import com.meditrack.authorization.domain.ports.in.query.GetAuthorizationByIdQuery;
import com.meditrack.authorization.domain.ports.in.query.ListAuthorizationsByPatientQuery;
import com.meditrack.authorization.domain.ports.in.useCase.*;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.infrastructure.adapters.in.rest.dto.*;
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
 * Controller REST: Gestión de Autorizaciones Médicas
 * Endpoints protegidos con JWT
 */
@RestController
@RequestMapping("/api/authorizations")
@Tag(name = "Medical Authorizations", description = "Endpoints de gestión de autorizaciones médicas")
@SecurityRequirement(name = "bearerAuth")
public class MedicalAuthorizationController {

    private final CreateMedicalAuthorizationUseCase createAuthorizationUseCase;
    private final EvaluateMedicalAuthorizationUseCase evaluateAuthorizationUseCase;
    private final UpdateAuthorizationStatusUseCase updateStatusUseCase;
    private final GetAuthorizationByIdUseCase getAuthorizationByIdUseCase;
    private final ListAuthorizationsByPatientUseCase listByPatientUseCase;
    private final ListPendingAuthorizationsUseCase listPendingUseCase;
    private final CurrentUserPort currentUserPort;

    public MedicalAuthorizationController(
            CreateMedicalAuthorizationUseCase createAuthorizationUseCase,
            EvaluateMedicalAuthorizationUseCase evaluateAuthorizationUseCase,
            UpdateAuthorizationStatusUseCase updateStatusUseCase,
            GetAuthorizationByIdUseCase getAuthorizationByIdUseCase,
            ListAuthorizationsByPatientUseCase listByPatientUseCase,
            ListPendingAuthorizationsUseCase listPendingUseCase,
            CurrentUserPort currentUserPort) {
        this.createAuthorizationUseCase = createAuthorizationUseCase;
        this.evaluateAuthorizationUseCase = evaluateAuthorizationUseCase;
        this.updateStatusUseCase = updateStatusUseCase;
        this.getAuthorizationByIdUseCase = getAuthorizationByIdUseCase;
        this.listByPatientUseCase = listByPatientUseCase;
        this.listPendingUseCase = listPendingUseCase;
        this.currentUserPort = currentUserPort;
    }

    /**
     * POST /api/authorizations
     * Crea una nueva autorización médica (Admin y Médico)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Operation(
            summary = "Crear autorización médica",
            description = "Crea una nueva solicitud de autorización para un servicio médico. Solo accesible para ADMIN y MEDICO."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Autorización creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorizationResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado (requiere rol ADMIN o MEDICO)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<AuthorizationResponse> createAuthorization(
            @Valid @RequestBody CreateAuthorizationRequest request) {

        // 1. Obtener el usuario actual (quien está solicitando)
        UUID requestedBy = currentUserPort.getCurrentUserId();

        // 2. Crear el command
        CreateMedicalAuthorizationCommand command = new CreateMedicalAuthorizationCommand(
                request.getPatientId(),
                request.getServiceType(),
                request.getDescription(),
                requestedBy
        );

        // 3. Ejecutar el caso de uso
        MedicalAuthorization authorization = createAuthorizationUseCase.execute(command);

        // 4. Crear la respuesta
        AuthorizationResponse response = AuthorizationResponse.fromDomain(authorization);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/authorizations/{id}/evaluate
     * Evalúa una autorización con el servicio de seguros (Admin y Médico)
     */
    @PostMapping("/{id}/evaluate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Operation(
            summary = "Evaluar autorización con servicio de seguros",
            description = "Consulta al servicio externo de seguros para evaluar la cobertura y genera el copago. Solo accesible para ADMIN y MEDICO."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluación completada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EvaluationResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Autorización ya evaluada o datos inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Autorización no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error al comunicarse con el servicio de seguros",
                    content = @Content
            )
    })
    public ResponseEntity<EvaluationResponse> evaluateAuthorization(
            @PathVariable UUID id,
            @Valid @RequestBody EvaluateAuthorizationRequest request) {

        // 1. Crear el command
        EvaluateMedicalAuthorizationCommand command = new EvaluateMedicalAuthorizationCommand(
                id,
                request.getEstimatedCost()
        );

        // 2. Ejecutar el caso de uso (llama al servicio externo)
        CoverageEvaluation evaluation = evaluateAuthorizationUseCase.execute(command);

        // 3. Crear la respuesta
        EvaluationResponse response = EvaluationResponse.fromDomain(evaluation);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/authorizations
     * Lista autorizaciones pendientes (Admin y Médico)
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Operation(
            summary = "Listar autorizaciones pendientes",
            description = "Lista todas las autorizaciones en estado PENDIENTE. Solo accesible para ADMIN y MEDICO."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de autorizaciones pendientes obtenida exitosamente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content
            )
    })
    public ResponseEntity<List<AuthorizationResponse>> listPendingAuthorizations() {

        // 1. Ejecutar el caso de uso
        List<MedicalAuthorization> authorizations = listPendingUseCase.execute();

        // 2. Convertir a DTOs
        List<AuthorizationResponse> response = authorizations.stream()
                .map(AuthorizationResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/authorizations/{id}
     * Obtiene una autorización por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener autorización por ID",
            description = "Obtiene la información de una autorización específica. ADMIN y MEDICO pueden ver cualquiera, PACIENTE solo las suyas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autorización encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorizationResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Autorización no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado (sin permisos para ver esta autorización)",
                    content = @Content
            )
    })
    public ResponseEntity<AuthorizationResponse> getAuthorizationById(@PathVariable UUID id) {

        // 1. Crear el query
        GetAuthorizationByIdQuery query = new GetAuthorizationByIdQuery(id);

        // 2. Ejecutar el caso de uso
        MedicalAuthorization authorization = getAuthorizationByIdUseCase.execute(query);

        // 3. Crear la respuesta
        AuthorizationResponse response = AuthorizationResponse.fromDomain(authorization);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/authorizations/patient/{patientId}
     * Lista autorizaciones de un paciente
     */
    @GetMapping("/patient/{patientId}")
    @Operation(
            summary = "Listar autorizaciones de un paciente",
            description = "Lista todas las autorizaciones de un paciente específico. ADMIN y MEDICO pueden ver cualquier paciente, PACIENTE solo sus propias autorizaciones."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de autorizaciones obtenida exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content
            )
    })
    public ResponseEntity<List<AuthorizationResponse>> listAuthorizationsByPatient(
            @PathVariable UUID patientId) {

        // 1. Crear el query
        ListAuthorizationsByPatientQuery query = new ListAuthorizationsByPatientQuery(patientId);

        // 2. Ejecutar el caso de uso
        List<MedicalAuthorization> authorizations = listByPatientUseCase.execute(query);

        // 3. Convertir a DTOs
        List<AuthorizationResponse> response = authorizations.stream()
                .map(AuthorizationResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/authorizations/{id}/status
     * Actualiza el estado de una autorización (Solo Admin)
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Actualizar estado de autorización",
            description = "Cambia manualmente el estado de una autorización. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorizationResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cambio de estado inválido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Autorización no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado (requiere rol ADMIN)",
                    content = @Content
            )
    })
    public ResponseEntity<AuthorizationResponse> updateAuthorizationStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAuthorizationStatusRequest request) {

        // 1. Crear el command
        UpdateAuthorizationStatusCommand command = new UpdateAuthorizationStatusCommand(
                id,
                request.getNewStatus()
        );

        // 2. Ejecutar el caso de uso
        MedicalAuthorization authorization = updateStatusUseCase.execute(command);

        // 3. Crear la respuesta
        AuthorizationResponse response = AuthorizationResponse.fromDomain(authorization);

        return ResponseEntity.ok(response);
    }
}