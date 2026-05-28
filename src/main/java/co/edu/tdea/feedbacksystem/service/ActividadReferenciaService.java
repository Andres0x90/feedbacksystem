package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.dto.request.CreateActividadReferenciaRequest;
import co.edu.tdea.feedbacksystem.dto.request.UpdateActividadReferenciaRequest;
import co.edu.tdea.feedbacksystem.entity.ActividadEvaluativaReferencia;
import co.edu.tdea.feedbacksystem.entity.PlantillaRetroalimentacion;
import co.edu.tdea.feedbacksystem.entity.Rubrica;
import co.edu.tdea.feedbacksystem.exception.ResourceNotFoundException;
import co.edu.tdea.feedbacksystem.repository.ActividadEvaluativaReferenciaRepository;
import co.edu.tdea.feedbacksystem.repository.PlantillaRetroalimentacionRepository;
import co.edu.tdea.feedbacksystem.repository.RubricaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActividadReferenciaService {

    private final ActividadEvaluativaReferenciaRepository repository;
    private final RubricaRepository rubricaRepository;
    private final PlantillaRetroalimentacionRepository plantillaRepository;
    private final AuditoriaCambioService auditoriaCambioService;

    @Transactional
    public ActividadEvaluativaReferencia crear(CreateActividadReferenciaRequest request) {
        Rubrica rubrica = rubricaRepository.findById(request.getRubricaId())
                .orElseThrow(() -> new ResourceNotFoundException("Rúbrica", request.getRubricaId()));

        ActividadEvaluativaReferencia actividad = ActividadEvaluativaReferencia.builder()
                .rubrica(rubrica)
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .fecha(request.getFecha())
                .ponderacion(request.getPonderacion())
                .build();

        if (request.getPlantillaId() != null) {
            PlantillaRetroalimentacion plantilla = plantillaRepository.findById(request.getPlantillaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plantilla", request.getPlantillaId()));
            actividad.setPlantilla(plantilla);
        }

        ActividadEvaluativaReferencia saved = repository.save(actividad);
        auditoriaCambioService.registrar("ActividadEvaluativaReferencia", saved.getId(), "CREAR",
                "Actividad creada: " + saved.getTitulo());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<ActividadEvaluativaReferencia> listarTodas() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ActividadEvaluativaReferencia obtenerPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActividadEvaluativaReferencia", id));
    }

    @Transactional
    public ActividadEvaluativaReferencia actualizar(UUID id, UpdateActividadReferenciaRequest request) {
        ActividadEvaluativaReferencia actividad = obtenerPorId(id);
        actividad.setTitulo(request.getTitulo());
        actividad.setDescripcion(request.getDescripcion());
        actividad.setFecha(request.getFecha());
        actividad.setPonderacion(request.getPonderacion());
        actividad.setEstado(request.getEstado());

        if (request.getPlantillaId() != null) {
            PlantillaRetroalimentacion plantilla = plantillaRepository.findById(request.getPlantillaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plantilla", request.getPlantillaId()));
            actividad.setPlantilla(plantilla);
        } else {
            actividad.setPlantilla(null);
        }

        ActividadEvaluativaReferencia saved = repository.save(actividad);
        auditoriaCambioService.registrar("ActividadEvaluativaReferencia", saved.getId(), "ACTUALIZAR",
                "Actividad actualizada: " + saved.getTitulo());
        return saved;
    }
}
