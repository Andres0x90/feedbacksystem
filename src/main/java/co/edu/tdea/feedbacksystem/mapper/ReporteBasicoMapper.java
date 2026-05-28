package co.edu.tdea.feedbacksystem.mapper;

import co.edu.tdea.feedbacksystem.dto.response.ReporteBasicoResponse;
import co.edu.tdea.feedbacksystem.entity.ReporteBasico;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ReporteBasicoMapper {

    public ReporteBasicoResponse toResponse(ReporteBasico entity) {
        return ReporteBasicoResponse.builder()
                .id(entity.getId())
                .perfilAcademicoId(entity.getPerfilAcademico() != null ? entity.getPerfilAcademico().getId() : null)
                .tipo(entity.getTipo())
                .rangoFechas(entity.getRangoFechas())
                .totalFeedbacks(entity.getTotalFeedbacks())
                .indicadores(entity.getIndicadores())
                .fechaGeneracion(entity.getFechaGeneracion())
                .build();
    }

    public List<ReporteBasicoResponse> toResponseList(List<ReporteBasico> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toResponse).toList();
    }
}
