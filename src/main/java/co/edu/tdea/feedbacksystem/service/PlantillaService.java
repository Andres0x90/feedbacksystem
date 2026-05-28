package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.dto.request.CreatePlantillaRequest;
import co.edu.tdea.feedbacksystem.dto.request.UpdatePlantillaRequest;
import co.edu.tdea.feedbacksystem.entity.PlantillaRetroalimentacion;
import co.edu.tdea.feedbacksystem.exception.ResourceNotFoundException;
import co.edu.tdea.feedbacksystem.mapper.PlantillaMapper;
import co.edu.tdea.feedbacksystem.repository.PlantillaRetroalimentacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlantillaService {

    private final PlantillaRetroalimentacionRepository repository;
    private final PlantillaMapper mapper;
    private final AuditoriaCambioService auditoriaCambioService;

    @Transactional
    public PlantillaRetroalimentacion crear(CreatePlantillaRequest request) {
        PlantillaRetroalimentacion plantilla = mapper.toEntity(request);
        PlantillaRetroalimentacion saved = repository.save(plantilla);
        auditoriaCambioService.registrar("PlantillaRetroalimentacion", saved.getId(), "CREAR",
                "Plantilla creada: " + saved.getNombre());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<PlantillaRetroalimentacion> listarTodas() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public PlantillaRetroalimentacion obtenerPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plantilla", id));
    }

    @Transactional
    public PlantillaRetroalimentacion actualizar(UUID id, UpdatePlantillaRequest request) {
        PlantillaRetroalimentacion plantilla = obtenerPorId(id);
        plantilla.setNombre(request.getNombre());
        plantilla.setObjetivo(request.getObjetivo());
        plantilla.setContenidoBase(request.getContenidoBase());
        plantilla.setActiva(request.getActiva());
        PlantillaRetroalimentacion saved = repository.save(plantilla);
        auditoriaCambioService.registrar("PlantillaRetroalimentacion", saved.getId(), "ACTUALIZAR",
                "Plantilla actualizada: " + saved.getNombre());
        return saved;
    }
}
