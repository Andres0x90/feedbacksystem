package co.edu.tdea.feedbacksystem.controller;

import co.edu.tdea.feedbacksystem.dto.request.CreateCalificacionReferenciaRequest;
import co.edu.tdea.feedbacksystem.dto.response.CalificacionReferenciaResponse;
import co.edu.tdea.feedbacksystem.mapper.CalificacionReferenciaMapper;
import co.edu.tdea.feedbacksystem.service.CalificacionReferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/calificaciones-referencia")
@RequiredArgsConstructor
@Tag(name = "Calificaciones de Referencia", description = "Registro de calificaciones numéricas por estudiante y actividad")
public class CalificacionReferenciaController {

    private final CalificacionReferenciaService service;
    private final CalificacionReferenciaMapper mapper;

    @PostMapping
    @Operation(summary = "Registrar calificación de referencia")
    public ResponseEntity<CalificacionReferenciaResponse> crear(@Valid @RequestBody CreateCalificacionReferenciaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(service.crear(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener calificación por ID")
    public ResponseEntity<CalificacionReferenciaResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponse(service.obtenerPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar calificaciones por referenciaEstudiante",
               description = "Filtra calificaciones por el identificador externo del estudiante")
    public ResponseEntity<List<CalificacionReferenciaResponse>> listar(
            @Parameter(description = "Identificador externo del estudiante")
            @RequestParam(required = false) String referenciaEstudiante) {

        if (referenciaEstudiante != null && !referenciaEstudiante.isBlank()) {
            return ResponseEntity.ok(mapper.toResponseList(service.listarPorEstudiante(referenciaEstudiante)));
        }
        return ResponseEntity.ok(java.util.Collections.emptyList());
    }
}
