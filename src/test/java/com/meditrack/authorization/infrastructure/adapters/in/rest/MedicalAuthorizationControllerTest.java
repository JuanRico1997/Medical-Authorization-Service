package com.meditrack.authorization.infrastructure.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.*;
import com.meditrack.authorization.domain.ports.in.command.CreateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.in.query.GetAuthorizationByIdQuery;
import com.meditrack.authorization.domain.ports.in.query.ListAuthorizationsByPatientQuery;
import com.meditrack.authorization.domain.ports.in.useCase.*;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.infrastructure.adapters.in.rest.controller.MedicalAuthorizationController;
import com.meditrack.authorization.infrastructure.adapters.in.rest.dto.CreateAuthorizationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para MedicalAuthorizationController
 */
@WebMvcTest(MedicalAuthorizationController.class)
@AutoConfigureMockMvc
@DisplayName("MedicalAuthorizationController Integration Tests")
class MedicalAuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateMedicalAuthorizationUseCase createAuthorizationUseCase;

    @MockBean
    private EvaluateMedicalAuthorizationUseCase evaluateAuthorizationUseCase;

    @MockBean
    private UpdateAuthorizationStatusUseCase updateStatusUseCase;

    @MockBean
    private GetAuthorizationByIdUseCase getAuthorizationByIdUseCase;

    @MockBean
    private ListAuthorizationsByPatientUseCase listByPatientUseCase;

    @MockBean
    private ListPendingAuthorizationsUseCase listPendingUseCase;

    @MockBean
    private CurrentUserPort currentUserPort;

    private UUID patientId;
    private UUID authorizationId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
        authorizationId = UUID.randomUUID();
        userId = UUID.randomUUID();

        when(currentUserPort.getCurrentUserId()).thenReturn(userId);
    }

    @Test
    @DisplayName("POST /api/authorizations - Debe crear autorización como ADMIN")
    @WithMockUser(roles = "ADMIN")
    void shouldCreateAuthorizationAsAdmin() throws Exception {
        // Given
        CreateAuthorizationRequest request = new CreateAuthorizationRequest(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento por dolor lumbar crónico"
        );

        MedicalAuthorization mockAuthorization = new MedicalAuthorization(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento por dolor lumbar crónico",
                userId
        );

        when(createAuthorizationUseCase.execute(any(CreateMedicalAuthorizationCommand.class)))
                .thenReturn(mockAuthorization);

        // When - Then
        mockMvc.perform(post("/api/authorizations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(patientId.toString()))
                .andExpect(jsonPath("$.serviceType").value("CONSULTA"))
                .andExpect(jsonPath("$.description").value("Consulta de seguimiento por dolor lumbar crónico"));
    }

    @Test
    @DisplayName("POST /api/authorizations - Debe rechazar acceso como PACIENTE")
    @WithMockUser(roles = "PACIENTE")
    void shouldRejectAuthorizationCreationAsPatient() throws Exception {
        // Given
        CreateAuthorizationRequest request = new CreateAuthorizationRequest(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento"
        );

        // When - Then
        mockMvc.perform(post("/api/authorizations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/authorizations - Debe validar campos obligatorios")
    @WithMockUser(roles = "ADMIN")
    void shouldValidateRequiredFields() throws Exception {
        // Given - Request sin patientId
        CreateAuthorizationRequest request = new CreateAuthorizationRequest(
                null,
                ServiceType.CONSULTA,
                "Consulta"
        );

        // When - Then
        mockMvc.perform(post("/api/authorizations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    @DisplayName("POST /api/authorizations - Debe validar longitud de descripción")
    @WithMockUser(roles = "ADMIN")
    void shouldValidateDescriptionLength() throws Exception {
        // Given - Descripción muy corta (menos de 10 caracteres)
        CreateAuthorizationRequest request = new CreateAuthorizationRequest(
                patientId,
                ServiceType.CONSULTA,
                "Corta"
        );

        // When - Then
        mockMvc.perform(post("/api/authorizations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors").isArray());
    }

    @Test
    @DisplayName("GET /api/authorizations - Debe listar autorizaciones pendientes como MEDICO")
    @WithMockUser(roles = "MEDICO")
    void shouldListPendingAuthorizationsAsMedico() throws Exception {
        // Given
        when(listPendingUseCase.execute()).thenReturn(new ArrayList<>());

        // When - Then
        mockMvc.perform(get("/api/authorizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/authorizations/{id} - Debe obtener autorización por ID")
    @WithMockUser(roles = "ADMIN")
    void shouldGetAuthorizationById() throws Exception {
        // Given
        MedicalAuthorization mockAuthorization = new MedicalAuthorization(
                patientId,
                ServiceType.CONSULTA,
                "Consulta de seguimiento",
                userId
        );

        when(getAuthorizationByIdUseCase.execute(any(GetAuthorizationByIdQuery.class)))
                .thenReturn(mockAuthorization);

        // When - Then
        mockMvc.perform(get("/api/authorizations/" + authorizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(patientId.toString()))
                .andExpect(jsonPath("$.serviceType").value("CONSULTA"));
    }

    @Test
    @DisplayName("GET /api/authorizations/patient/{patientId} - Debe listar autorizaciones de un paciente")
    @WithMockUser(roles = "MEDICO")
    void shouldListAuthorizationsByPatient() throws Exception {
        // Given
        when(listByPatientUseCase.execute(any(ListAuthorizationsByPatientQuery.class)))
                .thenReturn(new ArrayList<>());

        // When - Then
        mockMvc.perform(get("/api/authorizations/patient/" + patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}