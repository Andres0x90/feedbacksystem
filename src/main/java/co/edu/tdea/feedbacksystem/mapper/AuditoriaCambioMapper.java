package co.edu.tdea.feedbacksystem.mapper;

import co.edu.tdea.feedbacksystem.dto.response.AuditoriaCambioResponse;
import co.edu.tdea.feedbacksystem.entity.AuditoriaCambio;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AuditoriaCambioMapper {

    public AuditoriaCambioResponse toResponse(AuditoriaCambio entity) {
        return AuditoriaCambioResponse.builder()
                .id(entity.getId())
                .entidad(entity.getEntidad())
                .entidadId(entity.getEntidadId())
                .accion(entity.getAccion())
                .detalle(entity.getDetalle())
                .fecha(entity.getFecha())
                .build();
    }

    public List<AuditoriaCambioResponse> toResponseList(List<AuditoriaCambio> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toResponse).toList();
    }
}
