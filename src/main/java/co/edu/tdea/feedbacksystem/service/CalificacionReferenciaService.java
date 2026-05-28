package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.dto.request.CreateCalificacionReferenciaRequest;
import co.edu.tdea.feedbacksystem.entity.ActividadEvaluativaReferencia;
import co.edu.tdea.feedbacksystem.entity.CalificacionReferencia;
import co.edu.tdea.feedbacksystem.exception.BusinessValidationException;
import co.edu.tdea.feedbacksystem.exception.ResourceNotFoundException;
import co.edu.tdea.feedbacksystem.repository.ActividadEvaluativaReferenciaRepository;
import co.edu.tdea.feedbacksystem.repository.CalificacionReferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalificacionReferenciaService {

    private final CalificacionReferenciaRepository repository;
    private final ActividadEvaluativaReferenciaRepository actividadRepository;
    private final AuditoriaCambioService auditoriaCambioService;

    @Transactional
    public CalificacionReferencia crear(CreateCalificacionReferenciaRequest request) {
        ActividadEvaluativaReferencia actividad = actividadRepository.findById(request.getActividadEvaluativaId())
                .orElseThrow(() -> new ResourceNotFoundException("ActividadEvaluativaReferencia", request.getActividadEvaluativaId()));

        if (repository.existsByActividadEvaluativaIdAndReferenciaEstudiante(
                request.getActividadEvaluativaId(), request.getReferenciaEstudiante())) {
            throw new BusinessValidationException(
                    "Ya existe una calificación para el estudiante '" + request.getReferenciaEstudiante()
                    + "' en esta actividad");
        }

        CalificacionReferencia calificacion = CalificacionReferencia.builder()
                .actividadEvaluativa(actividad)
                .referenciaEstudiante(request.getReferenciaEstudiante())
                .valorNumerico(request.getValorNumerico())
                .observacionGeneral(request.getObservacionGeneral())
                .build();

        CalificacionReferencia saved = repository.save(calificacion);
        auditoriaCambioService.registrar("CalificacionReferencia", saved.getId(), "CREAR",
                "Calificación registrada para estudiante " + saved.getReferenciaEstudiante()
                + " en actividad " + actividad.getTitulo() + ": " + saved.getValorNumerico());
        return saved;
    }

    @Transactional(readOnly = true)
    public CalificacionReferencia obtenerPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CalificacionReferencia", id));
    }

    @Transactional(readOnly = true)
    public List<CalificacionReferencia> listarPorEstudiante(String referenciaEstudiante) {
        return repository.findByReferenciaEstudiante(referenciaEstudiante);
    }

    @Transactional(readOnly = true)
    public List<CalificacionReferencia> listarPorActividad(UUID actividadId) {
        actividadRepository.findById(actividadId)
                .orElseThrow(() -> new ResourceNotFoundException("ActividadEvaluativaReferencia", actividadId));
        return repository.findByActividadEvaluativaId(actividadId);
    }
}
