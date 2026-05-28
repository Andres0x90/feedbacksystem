package co.edu.tdea.feedbacksystem.mapper;

import co.edu.tdea.feedbacksystem.dto.response.ComentarioCriterioResponse;
import co.edu.tdea.feedbacksystem.dto.response.FeedbackResponse;
import co.edu.tdea.feedbacksystem.entity.ComentarioCriterio;
import co.edu.tdea.feedbacksystem.entity.Feedback;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class FeedbackMapper {

    public FeedbackResponse toResponse(Feedback entity) {
        String referenciaEstudiante = null;
        if (entity.getCalificacionReferencia() != null) {
            referenciaEstudiante = entity.getCalificacionReferencia().getReferenciaEstudiante();
        }

        return FeedbackResponse.builder()
                .id(entity.getId())
                .calificacionReferenciaId(entity.getCalificacionReferencia() != null
                        ? entity.getCalificacionReferencia().getId() : null)
                .referenciaEstudiante(referenciaEstudiante)
                .resumenGeneral(entity.getResumenGeneral())
                .estado(entity.getEstado())
                .comentarios(toComentarioList(entity.getComentarios()))
                .fechaPublicacion(entity.getFechaPublicacion())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public ComentarioCriterioResponse toComentarioResponse(ComentarioCriterio entity) {
        return ComentarioCriterioResponse.builder()
                .id(entity.getId())
                .feedbackId(entity.getFeedback() != null ? entity.getFeedback().getId() : null)
                .criterioId(entity.getCriterio() != null ? entity.getCriterio().getId() : null)
                .criterioNombre(entity.getCriterio() != null ? entity.getCriterio().getNombre() : null)
                .comentario(entity.getComentario())
                .fortaleza(entity.getFortaleza())
                .oportunidadMejora(entity.getOportunidadMejora())
                .build();
    }

    public List<FeedbackResponse> toResponseList(List<Feedback> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toResponse).toList();
    }

    private List<ComentarioCriterioResponse> toComentarioList(List<ComentarioCriterio> comentarios) {
        if (comentarios == null) return Collections.emptyList();
        return comentarios.stream().map(this::toComentarioResponse).toList();
    }
}
