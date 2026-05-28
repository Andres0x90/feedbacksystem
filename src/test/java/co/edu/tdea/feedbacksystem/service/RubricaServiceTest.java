package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.dto.request.CreateCriterioRequest;
import co.edu.tdea.feedbacksystem.dto.request.CreateNivelDesempenoRequest;
import co.edu.tdea.feedbacksystem.dto.request.CreateRubricaRequest;
import co.edu.tdea.feedbacksystem.entity.Rubrica;
import co.edu.tdea.feedbacksystem.exception.BusinessValidationException;
import co.edu.tdea.feedbacksystem.exception.ResourceNotFoundException;
import co.edu.tdea.feedbacksystem.mapper.RubricaMapper;
import co.edu.tdea.feedbacksystem.repository.CriterioRepository;
import co.edu.tdea.feedbacksystem.repository.RubricaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RubricaService - Pruebas Unitarias")
class RubricaServiceTest {

    @Mock
    private RubricaRepository rubricaRepository;

    @Mock
    private CriterioRepository criterioRepository;

    @Mock
    private RubricaMapper mapper;

    @Mock
    private AuditoriaCambioService auditoriaCambioService;

    @InjectMocks
    private RubricaService rubricaService;

    private CreateRubricaRequest requestValido;

    @BeforeEach
    void setUp() {
        CreateNivelDesempenoRequest nivel = CreateNivelDesempenoRequest.builder()
                .nombre("Excelente")
                .puntajeMin(new BigDecimal("4.5"))
                .puntajeMax(new BigDecimal("5.0"))
                .build();

        CreateCriterioRequest criterio1 = CreateCriterioRequest.builder()
                .nombre("Criterio 1")
                .peso(new BigDecimal("60"))
                .orden(1)
                .niveles(List.of(nivel))
                .build();

        CreateCriterioRequest criterio2 = CreateCriterioRequest.builder()
                .nombre("Criterio 2")
                .peso(new BigDecimal("40"))
                .orden(2)
                .niveles(List.of(nivel))
                .build();

        requestValido = CreateRubricaRequest.builder()
                .nombre("Rúbrica Test")
                .descripcion("Descripción de prueba")
                .criterios(List.of(criterio1, criterio2))
                .build();
    }

    @Test
    @DisplayName("Crear rúbrica con suma de pesos = 100 debe tener éxito")
    void crearRubrica_conSumaPesosValida_debeCrearRubrica() {
        Rubrica rubricaMock = Rubrica.builder()
                .id(UUID.randomUUID())
                .nombre("Rúbrica Test")
                .version(1)
                .activa(true)
                .build();

        when(mapper.toEntity(any())).thenReturn(rubricaMock);
        when(rubricaRepository.save(any())).thenReturn(rubricaMock);
        doNothing().when(auditoriaCambioService).registrar(any(), any(), any(), any());

        Rubrica result = rubricaService.crearRubrica(requestValido);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Rúbrica Test");
        verify(rubricaRepository).save(any());
        verify(auditoriaCambioService).registrar(eq("Rubrica"), any(), eq("CREAR"), any());
    }

    @Test
    @DisplayName("Crear rúbrica con suma de pesos != 100 debe lanzar BusinessValidationException")
    void crearRubrica_conSumaPesosInvalida_debeLanzarExcepcion() {
        CreateCriterioRequest criterioMal = CreateCriterioRequest.builder()
                .nombre("Solo criterio")
                .peso(new BigDecimal("50"))
                .orden(1)
                .niveles(List.of())
                .build();

        CreateRubricaRequest requestInvalido = CreateRubricaRequest.builder()
                .nombre("Rúbrica Inválida")
                .criterios(List.of(criterioMal))
                .build();

        assertThatThrownBy(() -> rubricaService.crearRubrica(requestInvalido))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("100");

        verify(rubricaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Obtener rúbrica inexistente debe lanzar ResourceNotFoundException")
    void obtenerPorId_rubricaInexistente_debeLanzarExcepcion() {
        UUID id = UUID.randomUUID();
        when(rubricaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rubricaService.obtenerPorId(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Listar todas las rúbricas debe retornar lista")
    void listarTodas_debeRetornarLista() {
        when(rubricaRepository.findAll()).thenReturn(List.of(
                Rubrica.builder().id(UUID.randomUUID()).nombre("R1").build(),
                Rubrica.builder().id(UUID.randomUUID()).nombre("R2").build()
        ));

        List<Rubrica> result = rubricaService.listarTodas();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Crear versión de rúbrica debe incrementar número de versión")
    void crearVersion_debeIncrementarVersion() {
        UUID id = UUID.randomUUID();
        Rubrica original = Rubrica.builder()
                .id(id)
                .nombre("Rúbrica Original")
                .version(1)
                .activa(true)
                .build();

        Rubrica nuevaVersion = Rubrica.builder()
                .id(UUID.randomUUID())
                .nombre("Rúbrica Original")
                .version(2)
                .activa(false)
                .build();

        when(rubricaRepository.findById(id)).thenReturn(Optional.of(original));
        when(rubricaRepository.save(any())).thenReturn(nuevaVersion);
        doNothing().when(auditoriaCambioService).registrar(any(), any(), any(), any());

        Rubrica result = rubricaService.crearVersion(id);

        assertThat(result.getVersion()).isEqualTo(2);
        assertThat(result.getActiva()).isFalse();
    }
}
