package co.edu.tdea.feedbacksystem.mapper;

import co.edu.tdea.feedbacksystem.dto.response.PerfilAcademicoResponse;
import co.edu.tdea.feedbacksystem.dto.response.RecomendacionResponse;
import co.edu.tdea.feedbacksystem.entity.PerfilAcademicoBasico;
import co.edu.tdea.feedbacksystem.entity.Recomendacion;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PerfilAcademicoMapper {

    public PerfilAcademicoResponse toResponse(PerfilAcademicoBasico entity) {
        return PerfilAcademicoResponse.builder()
                .id(entity.getId())
                .referenciaEstudiante(entity.getReferenciaEstudiante())
                .fortalezas(entity.getFortalezas())
                .brechas(entity.getBrechas())
                .tendencia(entity.getTendencia())
                .recomendaciones(toRecomendacionList(entity.getRecomendaciones()))
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }

    public RecomendacionResponse toRecomendacionResponse(Recomendacion entity) {
        return RecomendacionResponse.builder()
                .id(entity.getId())
                .perfilAcademicoId(entity.getPerfilAcademico() != null ? entity.getPerfilAcademico().getId() : null)
                .tipo(entity.getTipo())
                .descripcion(entity.getDescripcion())
                .prioridad(entity.getPrioridad())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    private List<RecomendacionResponse> toRecomendacionList(List<Recomendacion> recomendaciones) {
        if (recomendaciones == null) return Collections.emptyList();
        return recomendaciones.stream().map(this::toRecomendacionResponse).toList();
    }
}
