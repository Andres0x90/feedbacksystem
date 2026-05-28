package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.dto.request.CreateComentarioCriterioRequest;
import co.edu.tdea.feedbacksystem.dto.request.CreateFeedbackRequest;
import co.edu.tdea.feedbacksystem.entity.*;
import co.edu.tdea.feedbacksystem.exception.BusinessValidationException;
import co.edu.tdea.feedbacksystem.exception.ResourceNotFoundException;
import co.edu.tdea.feedbacksystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CalificacionReferenciaRepository calificacionRepository;
    private final CriterioRepository criterioRepository;
    private final AuditoriaCambioService auditoriaCambioService;

    @Transactional
    public Feedback crear(CreateFeedbackRequest request) {
        CalificacionReferencia calificacion = calificacionRepository.findById(request.getCalificacionReferenciaId())
                .orElseThrow(() -> new ResourceNotFoundException("CalificacionReferencia", request.getCalificacionReferenciaId()));

        if (feedbackRepository.findByCalificacionReferenciaId(calificacion.getId()).isPresent()) {
            throw new BusinessValidationException(
                    "Ya existe un feedback para la calificación " + calificacion.getId());
        }

        Feedback feedback = Feedback.builder()
                .calificacionReferencia(calificacion)
                .resumenGeneral(request.getResumenGeneral())
                .build();

        if (request.getComentarios() != null) {
            UUID rubricaId = calificacion.getActividadEvaluativa().getRubrica().getId();
            for (CreateComentarioCriterioRequest comentarioReq : request.getComentarios()) {
                Criterio criterio = criterioRepository.findById(comentarioReq.getCriterioId())
                        .orElseThrow(() -> new ResourceNotFoundException("Criterio", comentarioReq.getCriterioId()));

                if (!criterio.getRubrica().getId().equals(rubricaId)) {
                    throw new BusinessValidationException(
                            "El criterio " + criterio.getId() + " no pertenece a la rúbrica de esta actividad");
                }

                ComentarioCriterio comentario = ComentarioCriterio.builder()
                        .criterio(criterio)
                        .comentario(comentarioReq.getComentario())
                        .fortaleza(comentarioReq.getFortaleza())
                        .oportunidadMejora(comentarioReq.getOportunidadMejora())
                        .build();
                feedback.getComentarios().add(comentario);
                comentario.setFeedback(feedback);
            }
        }

        Feedback saved = feedbackRepository.save(feedback);
        auditoriaCambioService.registrar("Feedback", saved.getId(), "CREAR",
                "Feedback creado para calificación " + calificacion.getId()
                + " (estudiante: " + calificacion.getReferenciaEstudiante() + ")");
        return saved;
    }

    @Transactional(readOnly = true)
    public Feedback obtenerPorId(UUID id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", id));
    }

    @Transactional(readOnly = true)
    public List<Feedback> listarPorEstudiante(String referenciaEstudiante) {
        return feedbackRepository.findByReferenciaEstudiante(referenciaEstudiante);
    }

    @Transactional(readOnly = true)
    public List<Feedback> listarPorActividad(UUID actividadId) {
        return feedbackRepository.findByActividadId(actividadId);
    }

    @Transactional
    public Feedback publicar(UUID id) {
        Feedback feedback = obtenerPorId(id);
        if ("PUBLICADO".equals(feedback.getEstado())) {
            throw new BusinessValidationException("El feedback ya está publicado");
        }
        feedback.setEstado("PUBLICADO");
        feedback.setFechaPublicacion(LocalDateTime.now());
        Feedback saved = feedbackRepository.save(feedback);
        auditoriaCambioService.registrar("Feedback", saved.getId(), "PUBLICAR",
                "Feedback publicado para calificación " + feedback.getCalificacionReferencia().getId());
        return saved;
    }
}
