package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.dto.request.CreateFeedbackRequest;
import co.edu.tdea.feedbacksystem.entity.*;
import co.edu.tdea.feedbacksystem.exception.BusinessValidationException;
import co.edu.tdea.feedbacksystem.exception.ResourceNotFoundException;
import co.edu.tdea.feedbacksystem.repository.*;
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
@DisplayName("FeedbackService - Pruebas Unitarias")
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private CalificacionReferenciaRepository calificacionRepository;

    @Mock
    private CriterioRepository criterioRepository;

    @Mock
    private AuditoriaCambioService auditoriaCambioService;

    @InjectMocks
    private FeedbackService feedbackService;

    private CalificacionReferencia calificacion;
    private UUID calificacionId;

    @BeforeEach
    void setUp() {
        Rubrica rubrica = Rubrica.builder()
                .id(UUID.randomUUID())
                .nombre("Rúbrica Test")
                .build();

        ActividadEvaluativaReferencia actividad = ActividadEvaluativaReferencia.builder()
                .id(UUID.randomUUID())
                .titulo("Actividad Test")
                .rubrica(rubrica)
                .build();

        calificacionId = UUID.randomUUID();
        calificacion = CalificacionReferencia.builder()
                .id(calificacionId)
                .actividadEvaluativa(actividad)
                .referenciaEstudiante("EST-2026-001")
                .valorNumerico(new BigDecimal("4.2"))
                .build();
    }

    @Test
    @DisplayName("Crear feedback exitosamente")
    void crear_feedbackValido_debePersistir() {
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .calificacionReferenciaId(calificacionId)
                .resumenGeneral("Buen desempeño general")
                .comentarios(List.of())
                .build();

        Feedback feedbackGuardado = Feedback.builder()
                .id(UUID.randomUUID())
                .calificacionReferencia(calificacion)
                .resumenGeneral("Buen desempeño general")
                .estado("BORRADOR")
                .build();

        when(calificacionRepository.findById(calificacionId)).thenReturn(Optional.of(calificacion));
        when(feedbackRepository.findByCalificacionReferenciaId(calificacionId)).thenReturn(Optional.empty());
        when(feedbackRepository.save(any())).thenReturn(feedbackGuardado);
        doNothing().when(auditoriaCambioService).registrar(any(), any(), any(), any());

        Feedback result = feedbackService.crear(request);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo("BORRADOR");
        verify(feedbackRepository).save(any());
    }

    @Test
    @DisplayName("Crear feedback duplicado debe lanzar BusinessValidationException")
    void crear_feedbackDuplicado_debeLanzarExcepcion() {
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .calificacionReferenciaId(calificacionId)
                .build();

        Feedback feedbackExistente = Feedback.builder().id(UUID.randomUUID()).build();

        when(calificacionRepository.findById(calificacionId)).thenReturn(Optional.of(calificacion));
        when(feedbackRepository.findByCalificacionReferenciaId(calificacionId))
                .thenReturn(Optional.of(feedbackExistente));

        assertThatThrownBy(() -> feedbackService.crear(request))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("Ya existe un feedback");

        verify(feedbackRepository, never()).save(any());
    }

    @Test
    @DisplayName("Publicar feedback en estado BORRADOR debe cambiar estado")
    void publicar_feedbackBorrador_debeCambiarEstado() {
        UUID feedbackId = UUID.randomUUID();
        Feedback feedback = Feedback.builder()
                .id(feedbackId)
                .calificacionReferencia(calificacion)
                .estado("BORRADOR")
                .build();

        Feedback feedbackPublicado = Feedback.builder()
                .id(feedbackId)
                .calificacionReferencia(calificacion)
                .estado("PUBLICADO")
                .build();

        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback));
        when(feedbackRepository.save(any())).thenReturn(feedbackPublicado);
        doNothing().when(auditoriaCambioService).registrar(any(), any(), any(), any());

        Feedback result = feedbackService.publicar(feedbackId);

        assertThat(result.getEstado()).isEqualTo("PUBLICADO");
    }

    @Test
    @DisplayName("Listar feedback por referenciaEstudiante debe retornar lista correcta")
    void listarPorEstudiante_debeRetornarFeedbacksDelEstudiante() {
        String ref = "EST-2026-001";
        when(feedbackRepository.findByReferenciaEstudiante(ref))
                .thenReturn(List.of(
                        Feedback.builder().id(UUID.randomUUID()).calificacionReferencia(calificacion).build()
                ));

        List<Feedback> result = feedbackService.listarPorEstudiante(ref);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Obtener feedback inexistente debe lanzar ResourceNotFoundException")
    void obtenerPorId_feedbackInexistente_debeLanzarExcepcion() {
        UUID id = UUID.randomUUID();
        when(feedbackRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedbackService.obtenerPorId(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
