package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.dto.request.CreateCalificacionReferenciaRequest;
import co.edu.tdea.feedbacksystem.entity.ActividadEvaluativaReferencia;
import co.edu.tdea.feedbacksystem.entity.CalificacionReferencia;
import co.edu.tdea.feedbacksystem.entity.Rubrica;
import co.edu.tdea.feedbacksystem.exception.BusinessValidationException;
import co.edu.tdea.feedbacksystem.exception.ResourceNotFoundException;
import co.edu.tdea.feedbacksystem.repository.ActividadEvaluativaReferenciaRepository;
import co.edu.tdea.feedbacksystem.repository.CalificacionReferenciaRepository;
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
@DisplayName("CalificacionReferenciaService - Pruebas Unitarias")
class CalificacionReferenciaServiceTest {

    @Mock
    private CalificacionReferenciaRepository repository;

    @Mock
    private ActividadEvaluativaReferenciaRepository actividadRepository;

    @Mock
    private AuditoriaCambioService auditoriaCambioService;

    @InjectMocks
    private CalificacionReferenciaService service;

    @Test
    @DisplayName("Registrar calificación válida debe persistir exitosamente")
    void crear_calificacionValida_debePersistir() {
        UUID actividadId = UUID.randomUUID();
        ActividadEvaluativaReferencia actividad = ActividadEvaluativaReferencia.builder()
                .id(actividadId)
                .titulo("Test")
                .rubrica(Rubrica.builder().id(UUID.randomUUID()).build())
                .build();

        CreateCalificacionReferenciaRequest request = CreateCalificacionReferenciaRequest.builder()
                .actividadEvaluativaId(actividadId)
                .referenciaEstudiante("EST-2026-005")
                .valorNumerico(new BigDecimal("4.0"))
                .build();

        CalificacionReferencia saved = CalificacionReferencia.builder()
                .id(UUID.randomUUID())
                .actividadEvaluativa(actividad)
                .referenciaEstudiante("EST-2026-005")
                .valorNumerico(new BigDecimal("4.0"))
                .build();

        when(actividadRepository.findById(actividadId)).thenReturn(Optional.of(actividad));
        when(repository.existsByActividadEvaluativaIdAndReferenciaEstudiante(actividadId, "EST-2026-005"))
                .thenReturn(false);
        when(repository.save(any())).thenReturn(saved);
        doNothing().when(auditoriaCambioService).registrar(any(), any(), any(), any());

        CalificacionReferencia result = service.crear(request);

        assertThat(result).isNotNull();
        assertThat(result.getValorNumerico()).isEqualByComparingTo("4.0");
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Registrar calificación duplicada debe lanzar BusinessValidationException")
    void crear_calificacionDuplicada_debeLanzarExcepcion() {
        UUID actividadId = UUID.randomUUID();
        ActividadEvaluativaReferencia actividad = ActividadEvaluativaReferencia.builder()
                .id(actividadId).titulo("Test").build();

        CreateCalificacionReferenciaRequest request = CreateCalificacionReferenciaRequest.builder()
                .actividadEvaluativaId(actividadId)
                .referenciaEstudiante("EST-2026-001")
                .valorNumerico(new BigDecimal("3.5"))
                .build();

        when(actividadRepository.findById(actividadId)).thenReturn(Optional.of(actividad));
        when(repository.existsByActividadEvaluativaIdAndReferenciaEstudiante(actividadId, "EST-2026-001"))
                .thenReturn(true);

        assertThatThrownBy(() -> service.crear(request))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("Ya existe una calificación");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Listar calificaciones por referenciaEstudiante")
    void listarPorEstudiante_debeRetornarCalificaciones() {
        String ref = "EST-2026-001";
        when(repository.findByReferenciaEstudiante(ref))
                .thenReturn(List.of(
                        CalificacionReferencia.builder().referenciaEstudiante(ref).build()
                ));

        List<CalificacionReferencia> result = service.listarPorEstudiante(ref);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getReferenciaEstudiante()).isEqualTo(ref);
    }

    @Test
    @DisplayName("Obtener calificación inexistente lanza ResourceNotFoundException")
    void obtenerPorId_inexistente_debeLanzarExcepcion() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
