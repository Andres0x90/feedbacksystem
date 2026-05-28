package co.edu.tdea.feedbacksystem.controller;

import co.edu.tdea.feedbacksystem.dto.request.CreateActividadReferenciaRequest;
import co.edu.tdea.feedbacksystem.dto.request.UpdateActividadReferenciaRequest;
import co.edu.tdea.feedbacksystem.dto.response.ActividadReferenciaResponse;
import co.edu.tdea.feedbacksystem.mapper.ActividadReferenciaMapper;
import co.edu.tdea.feedbacksystem.service.ActividadReferenciaService;
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
@RequestMapping("/api/v1/actividades-referencia")
@RequiredArgsConstructor
@Tag(name = "Actividades Evaluativas de Referencia", description = "Gestión de actividades evaluativas asociadas a rúbricas")
public class ActividadReferenciaController {

    private final ActividadReferenciaService service;
    private final ActividadReferenciaMapper mapper;

    @PostMapping
    @Operation(summary = "Crear actividad evaluativa de referencia")
    public ResponseEntity<ActividadReferenciaResponse> crear(@Valid @RequestBody CreateActividadReferenciaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(service.crear(request)));
    }

    @GetMapping
    @Operation(summary = "Listar actividades evaluativas")
    public ResponseEntity<List<ActividadReferenciaResponse>> listar() {
        return ResponseEntity.ok(mapper.toResponseList(service.listarTodas()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener actividad por ID")
    public ResponseEntity<ActividadReferenciaResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponse(service.obtenerPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar actividad evaluativa")
    public ResponseEntity<ActividadReferenciaResponse> actualizar(@PathVariable UUID id,
                                                                   @Valid @RequestBody UpdateActividadReferenciaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.actualizar(id, request)));
    }
}
