package co.edu.tdea.feedbacksystem.controller;

import co.edu.tdea.feedbacksystem.dto.request.CreateCriterioRequest;
import co.edu.tdea.feedbacksystem.dto.request.CreateNivelDesempenoRequest;
import co.edu.tdea.feedbacksystem.dto.request.CreateRubricaRequest;
import co.edu.tdea.feedbacksystem.dto.response.CriterioResponse;
import co.edu.tdea.feedbacksystem.dto.response.RubricaResponse;
import co.edu.tdea.feedbacksystem.entity.Rubrica;
import co.edu.tdea.feedbacksystem.exception.BusinessValidationException;
import co.edu.tdea.feedbacksystem.exception.GlobalExceptionHandler;
import co.edu.tdea.feedbacksystem.mapper.RubricaMapper;
import co.edu.tdea.feedbacksystem.service.RubricaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RubricaController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("RubricaController - Pruebas de Integración Web")
class RubricaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RubricaService rubricaService;

    @MockBean
    private RubricaMapper rubricaMapper;

    @Test
    @DisplayName("POST /api/v1/rubricas - Crear rúbrica con datos válidos retorna 201")
    void crear_datosValidos_retorna201() throws Exception {
        CreateRubricaRequest request = buildRequestValido();
        Rubrica rubrica = Rubrica.builder().id(UUID.randomUUID()).nombre("Test").version(1).activa(true).build();
        RubricaResponse response = RubricaResponse.builder()
                .id(rubrica.getId())
                .nombre("Test")
                .version(1)
                .activa(true)
                .criterios(List.of())
                .createdAt(LocalDateTime.now())
                .build();

        when(rubricaService.crearRubrica(any())).thenReturn(rubrica);
        when(rubricaMapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/rubricas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Test"))
                .andExpect(jsonPath("$.version").value(1))
                .andExpect(jsonPath("$.activa").value(true));
    }

    @Test
    @DisplayName("POST /api/v1/rubricas - Sin criterios retorna 400")
    void crear_sinCriterios_retorna400() throws Exception {
        CreateRubricaRequest request = CreateRubricaRequest.builder()
                .nombre("Rúbrica sin criterios")
                .criterios(List.of())
                .build();

        mockMvc.perform(post("/api/v1/rubricas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/rubricas - Suma de pesos != 100 retorna 422")
    void crear_sumaPesosInvalida_retorna422() throws Exception {
        CreateRubricaRequest request = buildRequestValido();

        when(rubricaService.crearRubrica(any()))
                .thenThrow(new BusinessValidationException("La suma de ponderaciones debe ser 100"));

        mockMvc.perform(post("/api/v1/rubricas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("La suma de ponderaciones debe ser 100"));
    }

    @Test
    @DisplayName("GET /api/v1/rubricas - Listar retorna 200 con lista")
    void listar_retorna200() throws Exception {
        UUID id = UUID.randomUUID();
        RubricaResponse response = RubricaResponse.builder()
                .id(id).nombre("R1").version(1).activa(true).criterios(List.of()).build();

        when(rubricaService.listarTodas()).thenReturn(List.of(Rubrica.builder().id(id).build()));
        when(rubricaMapper.toResponseList(any())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/rubricas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("R1"));
    }

    private CreateRubricaRequest buildRequestValido() {
        CreateNivelDesempenoRequest nivel = CreateNivelDesempenoRequest.builder()
                .nombre("Excelente").puntajeMin(new BigDecimal("4.5")).puntajeMax(new BigDecimal("5.0")).build();

        CreateCriterioRequest c1 = CreateCriterioRequest.builder()
                .nombre("C1").peso(new BigDecimal("60")).orden(1).niveles(List.of(nivel)).build();
        CreateCriterioRequest c2 = CreateCriterioRequest.builder()
                .nombre("C2").peso(new BigDecimal("40")).orden(2).niveles(List.of(nivel)).build();

        return CreateRubricaRequest.builder()
                .nombre("Test").criterios(List.of(c1, c2)).build();
    }
}
