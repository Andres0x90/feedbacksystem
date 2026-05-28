package co.edu.tdea.feedbacksystem.mapper;

import co.edu.tdea.feedbacksystem.dto.response.CalificacionReferenciaResponse;
import co.edu.tdea.feedbacksystem.entity.CalificacionReferencia;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CalificacionReferenciaMapper {

    public CalificacionReferenciaResponse toResponse(CalificacionReferencia entity) {
        return CalificacionReferenciaResponse.builder()
                .id(entity.getId())
                .actividadEvaluativaId(entity.getActividadEvaluativa() != null ? entity.getActividadEvaluativa().getId() : null)
                .actividadTitulo(entity.getActividadEvaluativa() != null ? entity.getActividadEvaluativa().getTitulo() : null)
                .referenciaEstudiante(entity.getReferenciaEstudiante())
                .valorNumerico(entity.getValorNumerico())
                .observacionGeneral(entity.getObservacionGeneral())
                .feedbackId(entity.getFeedback() != null ? entity.getFeedback().getId() : null)
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }

    public List<CalificacionReferenciaResponse> toResponseList(List<CalificacionReferencia> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toResponse).toList();
    }
}
