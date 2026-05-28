package co.edu.tdea.feedbacksystem.mapper;

import co.edu.tdea.feedbacksystem.dto.response.ActividadReferenciaResponse;
import co.edu.tdea.feedbacksystem.entity.ActividadEvaluativaReferencia;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ActividadReferenciaMapper {

    public ActividadReferenciaResponse toResponse(ActividadEvaluativaReferencia entity) {
        return ActividadReferenciaResponse.builder()
                .id(entity.getId())
                .rubricaId(entity.getRubrica() != null ? entity.getRubrica().getId() : null)
                .rubricaNombre(entity.getRubrica() != null ? entity.getRubrica().getNombre() : null)
                .plantillaId(entity.getPlantilla() != null ? entity.getPlantilla().getId() : null)
                .plantillaNombre(entity.getPlantilla() != null ? entity.getPlantilla().getNombre() : null)
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .fecha(entity.getFecha())
                .ponderacion(entity.getPonderacion())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public List<ActividadReferenciaResponse> toResponseList(List<ActividadEvaluativaReferencia> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toResponse).toList();
    }
}
