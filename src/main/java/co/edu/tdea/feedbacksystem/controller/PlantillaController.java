package co.edu.tdea.feedbacksystem.controller;

import co.edu.tdea.feedbacksystem.dto.request.CreatePlantillaRequest;
import co.edu.tdea.feedbacksystem.dto.request.UpdatePlantillaRequest;
import co.edu.tdea.feedbacksystem.dto.response.PlantillaResponse;
import co.edu.tdea.feedbacksystem.mapper.PlantillaMapper;
import co.edu.tdea.feedbacksystem.service.PlantillaService;
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
@RequestMapping("/api/v1/plantillas")
@RequiredArgsConstructor
@Tag(name = "Plantillas de Retroalimentación", description = "Gestión de plantillas base para la retroalimentación")
public class PlantillaController {

    private final PlantillaService service;
    private final PlantillaMapper mapper;

    @PostMapping
    @Operation(summary = "Crear plantilla")
    public ResponseEntity<PlantillaResponse> crear(@Valid @RequestBody CreatePlantillaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(service.crear(request)));
    }

    @GetMapping
    @Operation(summary = "Listar plantillas")
    public ResponseEntity<List<PlantillaResponse>> listar() {
        return ResponseEntity.ok(mapper.toResponseList(service.listarTodas()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener plantilla por ID")
    public ResponseEntity<PlantillaResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponse(service.obtenerPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar plantilla")
    public ResponseEntity<PlantillaResponse> actualizar(@PathVariable UUID id,
                                                         @Valid @RequestBody UpdatePlantillaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.actualizar(id, request)));
    }
}
