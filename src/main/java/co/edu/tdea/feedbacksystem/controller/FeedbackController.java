package co.edu.tdea.feedbacksystem.controller;

import co.edu.tdea.feedbacksystem.dto.request.CreateFeedbackRequest;
import co.edu.tdea.feedbacksystem.dto.response.FeedbackResponse;
import co.edu.tdea.feedbacksystem.mapper.FeedbackMapper;
import co.edu.tdea.feedbacksystem.service.FeedbackService;
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
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Gestión de retroalimentación personalizada por estudiante")
public class FeedbackController {

    private final FeedbackService service;
    private final FeedbackMapper mapper;

    @PostMapping
    @Operation(summary = "Crear feedback", description = "Crea retroalimentación para una calificación, incluyendo comentarios por criterio")
    public ResponseEntity<FeedbackResponse> crear(@Valid @RequestBody CreateFeedbackRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(service.crear(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener feedback por ID")
    public ResponseEntity<FeedbackResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponse(service.obtenerPorId(id)));
    }

    @GetMapping("/estudiante/{referenciaEstudiante}")
    @Operation(summary = "Listar feedback por estudiante",
               description = "Retorna todo el feedback disponible para un identificador externo de estudiante")
    public ResponseEntity<List<FeedbackResponse>> listarPorEstudiante(
            @PathVariable String referenciaEstudiante) {
        return ResponseEntity.ok(mapper.toResponseList(service.listarPorEstudiante(referenciaEstudiante)));
    }

    @GetMapping("/actividad/{actividadId}")
    @Operation(summary = "Listar feedback por actividad")
    public ResponseEntity<List<FeedbackResponse>> listarPorActividad(@PathVariable UUID actividadId) {
        return ResponseEntity.ok(mapper.toResponseList(service.listarPorActividad(actividadId)));
    }

    @PostMapping("/{id}/publicar")
    @Operation(summary = "Publicar feedback", description = "Cambia el estado del feedback a PUBLICADO")
    public ResponseEntity<FeedbackResponse> publicar(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponse(service.publicar(id)));
    }
}
