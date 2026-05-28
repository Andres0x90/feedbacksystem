package co.edu.tdea.feedbacksystem.controller;

import co.edu.tdea.feedbacksystem.dto.request.CreateCriterioRequest;
import co.edu.tdea.feedbacksystem.dto.request.CreateRubricaRequest;
import co.edu.tdea.feedbacksystem.dto.request.UpdateRubricaRequest;
import co.edu.tdea.feedbacksystem.dto.response.CriterioResponse;
import co.edu.tdea.feedbacksystem.dto.response.RubricaResponse;
import co.edu.tdea.feedbacksystem.mapper.RubricaMapper;
import co.edu.tdea.feedbacksystem.service.RubricaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rubricas")
@RequiredArgsConstructor
@Tag(name = "Rúbricas", description = "Gestión de rúbricas de evaluación y sus criterios")
public class RubricaController {

    private final RubricaService service;
    private final RubricaMapper mapper;

    @PostMapping
    @Operation(summary = "Crear rúbrica", description = "Crea una nueva rúbrica con criterios y niveles de desempeño. La suma de pesos de criterios debe ser 100.")
    public ResponseEntity<RubricaResponse> crear(@Valid @RequestBody CreateRubricaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(service.crearRubrica(request)));
    }

    @GetMapping
    @Operation(summary = "Listar rúbricas", description = "Retorna todas las rúbricas registradas")
    public ResponseEntity<List<RubricaResponse>> listar() {
        return ResponseEntity.ok(mapper.toResponseList(service.listarTodas()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rúbrica por ID")
    public ResponseEntity<RubricaResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponse(service.obtenerPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rúbrica", description = "Actualiza nombre, descripción y estado activa de la rúbrica")
    public ResponseEntity<RubricaResponse> actualizar(@PathVariable UUID id,
                                                       @Valid @RequestBody UpdateRubricaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.actualizar(id, request)));
    }

    @PostMapping("/{id}/versiones")
    @Operation(summary = "Crear nueva versión", description = "Crea una copia de la rúbrica con versión incrementada")
    public ResponseEntity<RubricaResponse> crearVersion(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(service.crearVersion(id)));
    }

    @PostMapping("/{id}/criterios")
    @Operation(summary = "Agregar criterio", description = "Agrega un criterio a una rúbrica existente")
    public ResponseEntity<CriterioResponse> agregarCriterio(@PathVariable UUID id,
                                                              @Valid @RequestBody CreateCriterioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toCriterioResponse(service.agregarCriterio(id, request)));
    }

    @GetMapping("/{id}/criterios")
    @Operation(summary = "Listar criterios", description = "Retorna los criterios de una rúbrica ordenados por posición")
    public ResponseEntity<List<CriterioResponse>> listarCriterios(@PathVariable UUID id) {
        return ResponseEntity.ok(
                service.listarCriteriosPorRubrica(id).stream()
                        .map(mapper::toCriterioResponse)
                        .toList());
    }
}
