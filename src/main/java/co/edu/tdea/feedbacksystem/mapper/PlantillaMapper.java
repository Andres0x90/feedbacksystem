package co.edu.tdea.feedbacksystem.mapper;

import co.edu.tdea.feedbacksystem.dto.request.CreatePlantillaRequest;
import co.edu.tdea.feedbacksystem.dto.response.PlantillaResponse;
import co.edu.tdea.feedbacksystem.entity.PlantillaRetroalimentacion;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PlantillaMapper {

    public PlantillaRetroalimentacion toEntity(CreatePlantillaRequest request) {
        return PlantillaRetroalimentacion.builder()
                .nombre(request.getNombre())
                .objetivo(request.getObjetivo())
                .contenidoBase(request.getContenidoBase())
                .version(1)
                .activa(true)
                .build();
    }

    public PlantillaResponse toResponse(PlantillaRetroalimentacion entity) {
        return PlantillaResponse.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .objetivo(entity.getObjetivo())
                .contenidoBase(entity.getContenidoBase())
                .version(entity.getVersion())
                .activa(entity.getActiva())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public List<PlantillaResponse> toResponseList(List<PlantillaRetroalimentacion> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toResponse).toList();
    }
}
