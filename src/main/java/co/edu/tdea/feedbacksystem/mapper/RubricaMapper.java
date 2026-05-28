package co.edu.tdea.feedbacksystem.mapper;

import co.edu.tdea.feedbacksystem.dto.request.CreateCriterioRequest;
import co.edu.tdea.feedbacksystem.dto.request.CreateNivelDesempenoRequest;
import co.edu.tdea.feedbacksystem.dto.request.CreateRubricaRequest;
import co.edu.tdea.feedbacksystem.dto.response.CriterioResponse;
import co.edu.tdea.feedbacksystem.dto.response.NivelDesempenoResponse;
import co.edu.tdea.feedbacksystem.dto.response.RubricaResponse;
import co.edu.tdea.feedbacksystem.entity.Criterio;
import co.edu.tdea.feedbacksystem.entity.NivelDesempeno;
import co.edu.tdea.feedbacksystem.entity.Rubrica;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class RubricaMapper {

    public Rubrica toEntity(CreateRubricaRequest request) {
        Rubrica rubrica = Rubrica.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .version(1)
                .activa(true)
                .build();

        if (request.getCriterios() != null) {
            request.getCriterios().forEach(cr -> {
                Criterio criterio = toCriterioEntity(cr);
                rubrica.addCriterio(criterio);
            });
        }
        return rubrica;
    }

    public Criterio toCriterioEntity(CreateCriterioRequest request) {
        Criterio criterio = Criterio.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .peso(request.getPeso())
                .orden(request.getOrden())
                .build();

        if (request.getNiveles() != null) {
            request.getNiveles().forEach(nr -> {
                NivelDesempeno nivel = toNivelEntity(nr);
                criterio.addNivelDesempeno(nivel);
            });
        }
        return criterio;
    }

    public NivelDesempeno toNivelEntity(CreateNivelDesempenoRequest request) {
        return NivelDesempeno.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .puntajeMin(request.getPuntajeMin())
                .puntajeMax(request.getPuntajeMax())
                .build();
    }

    public RubricaResponse toResponse(Rubrica rubrica) {
        return RubricaResponse.builder()
                .id(rubrica.getId())
                .nombre(rubrica.getNombre())
                .descripcion(rubrica.getDescripcion())
                .version(rubrica.getVersion())
                .activa(rubrica.getActiva())
                .criterios(toCriterioResponseList(rubrica.getCriterios()))
                .createdAt(rubrica.getFechaCreacion())
                .updatedAt(rubrica.getFechaActualizacion())
                .build();
    }

    public CriterioResponse toCriterioResponse(Criterio criterio) {
        return CriterioResponse.builder()
                .id(criterio.getId())
                .rubricaId(criterio.getRubrica() != null ? criterio.getRubrica().getId() : null)
                .nombre(criterio.getNombre())
                .descripcion(criterio.getDescripcion())
                .peso(criterio.getPeso())
                .orden(criterio.getOrden())
                .niveles(toNivelResponseList(criterio.getNivelesDesempeno()))
                .createdAt(criterio.getFechaCreacion())
                .updatedAt(criterio.getFechaActualizacion())
                .build();
    }

    public NivelDesempenoResponse toNivelResponse(NivelDesempeno nivel) {
        return NivelDesempenoResponse.builder()
                .id(nivel.getId())
                .criterioId(nivel.getCriterio() != null ? nivel.getCriterio().getId() : null)
                .nombre(nivel.getNombre())
                .descripcion(nivel.getDescripcion())
                .puntajeMin(nivel.getPuntajeMin())
                .puntajeMax(nivel.getPuntajeMax())
                .createdAt(nivel.getFechaCreacion())
                .updatedAt(nivel.getFechaActualizacion())
                .build();
    }

    public List<RubricaResponse> toResponseList(List<Rubrica> rubricas) {
        if (rubricas == null) return Collections.emptyList();
        return rubricas.stream().map(this::toResponse).toList();
    }

    private List<CriterioResponse> toCriterioResponseList(List<Criterio> criterios) {
        if (criterios == null) return Collections.emptyList();
        return criterios.stream().map(this::toCriterioResponse).toList();
    }

    private List<NivelDesempenoResponse> toNivelResponseList(List<NivelDesempeno> niveles) {
        if (niveles == null) return Collections.emptyList();
        return niveles.stream().map(this::toNivelResponse).toList();
    }
}
