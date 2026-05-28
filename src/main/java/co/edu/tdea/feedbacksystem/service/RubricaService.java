package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.dto.request.CreateCriterioRequest;
import co.edu.tdea.feedbacksystem.dto.request.CreateRubricaRequest;
import co.edu.tdea.feedbacksystem.dto.request.UpdateRubricaRequest;
import co.edu.tdea.feedbacksystem.entity.Criterio;
import co.edu.tdea.feedbacksystem.entity.NivelDesempeno;
import co.edu.tdea.feedbacksystem.entity.Rubrica;
import co.edu.tdea.feedbacksystem.exception.BusinessValidationException;
import co.edu.tdea.feedbacksystem.exception.ResourceNotFoundException;
import co.edu.tdea.feedbacksystem.mapper.RubricaMapper;
import co.edu.tdea.feedbacksystem.repository.CriterioRepository;
import co.edu.tdea.feedbacksystem.repository.RubricaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RubricaService {

    private final RubricaRepository rubricaRepository;
    private final CriterioRepository criterioRepository;
    private final RubricaMapper mapper;
    private final AuditoriaCambioService auditoriaCambioService;

    @Transactional
    public Rubrica crearRubrica(CreateRubricaRequest request) {
        validarSumaPesos(request.getCriterios().stream()
                .map(c -> c.getPeso())
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Rubrica rubrica = mapper.toEntity(request);
        Rubrica saved = rubricaRepository.save(rubrica);
        auditoriaCambioService.registrar("Rubrica", saved.getId(), "CREAR",
                "Rúbrica creada: " + saved.getNombre() + " v" + saved.getVersion());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Rubrica> listarTodas() {
        List<Rubrica> rubricas = rubricaRepository.findAll();
        rubricas.forEach(r -> r.getCriterios().forEach(c -> c.getNivelesDesempeno().size()));
        return rubricas;
    }

    @Transactional(readOnly = true)
    public Rubrica obtenerPorId(UUID id) {
        Rubrica rubrica = rubricaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rúbrica", id));
        rubrica.getCriterios().forEach(c -> c.getNivelesDesempeno().size());
        return rubrica;
    }

    @Transactional
    public Rubrica actualizar(UUID id, UpdateRubricaRequest request) {
        Rubrica rubrica = obtenerPorId(id);
        rubrica.setNombre(request.getNombre());
        rubrica.setDescripcion(request.getDescripcion());
        rubrica.setActiva(request.getActiva());
        Rubrica saved = rubricaRepository.save(rubrica);
        auditoriaCambioService.registrar("Rubrica", saved.getId(), "ACTUALIZAR",
                "Rúbrica actualizada: " + saved.getNombre());
        return saved;
    }

    @Transactional
    public Rubrica crearVersion(UUID id) {
        Rubrica original = obtenerPorId(id);

        if (!original.getCriterios().isEmpty()) {
            BigDecimal suma = original.getCriterios().stream()
                    .map(Criterio::getPeso)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            validarSumaPesos(suma);
        }

        Rubrica nuevaVersion = Rubrica.builder()
                .nombre(original.getNombre())
                .descripcion(original.getDescripcion())
                .version(original.getVersion() + 1)
                .activa(false)
                .build();

        original.getCriterios().forEach(c -> {
            Criterio nuevoCriterio = Criterio.builder()
                    .nombre(c.getNombre())
                    .descripcion(c.getDescripcion())
                    .peso(c.getPeso())
                    .orden(c.getOrden())
                    .build();

            c.getNivelesDesempeno().forEach(n -> {
                NivelDesempeno nuevoNivel = NivelDesempeno.builder()
                        .nombre(n.getNombre())
                        .descripcion(n.getDescripcion())
                        .puntajeMin(n.getPuntajeMin())
                        .puntajeMax(n.getPuntajeMax())
                        .build();
                nuevoCriterio.addNivelDesempeno(nuevoNivel);
            });

            nuevaVersion.addCriterio(nuevoCriterio);
        });

        Rubrica saved = rubricaRepository.save(nuevaVersion);
        auditoriaCambioService.registrar("Rubrica", saved.getId(), "VERSIONAR",
                "Nueva versión " + saved.getVersion() + " creada desde rúbrica " + original.getId());
        return saved;
    }

    @Transactional
    public Criterio agregarCriterio(UUID rubricaId, CreateCriterioRequest request) {
        Rubrica rubrica = obtenerPorId(rubricaId);

        BigDecimal sumaActual = rubrica.getCriterios().stream()
                .map(Criterio::getPeso)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal nuevaSuma = sumaActual.add(request.getPeso());

        if (nuevaSuma.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessValidationException(
                    "Al agregar este criterio la suma de pesos (" + nuevaSuma + ") superaría 100");
        }

        Criterio criterio = mapper.toCriterioEntity(request);
        rubrica.addCriterio(criterio);
        rubricaRepository.save(rubrica);

        auditoriaCambioService.registrar("Criterio", criterio.getId() != null ? criterio.getId() : UUID.randomUUID(),
                "CREAR", "Criterio " + criterio.getNombre() + " agregado a rúbrica " + rubricaId);
        return criterio;
    }

    @Transactional(readOnly = true)
    public List<Criterio> listarCriteriosPorRubrica(UUID rubricaId) {
        obtenerPorId(rubricaId);
        return criterioRepository.findByRubricaIdOrderByOrden(rubricaId);
    }

    private void validarSumaPesos(BigDecimal suma) {
        if (suma.compareTo(new BigDecimal("100")) != 0) {
            throw new BusinessValidationException(
                    "La suma de ponderaciones de los criterios debe ser 100. Suma actual: " + suma);
        }
    }
}
