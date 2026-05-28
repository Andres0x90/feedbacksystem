package co.edu.tdea.feedbacksystem.controller;

import co.edu.tdea.feedbacksystem.dto.response.PerfilAcademicoResponse;
import co.edu.tdea.feedbacksystem.dto.response.RecomendacionResponse;
import co.edu.tdea.feedbacksystem.mapper.PerfilAcademicoMapper;
import co.edu.tdea.feedbacksystem.service.PerfilAcademicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/perfiles")
@RequiredArgsConstructor
@Tag(name = "Perfil Académico Básico", description = "Consulta y recálculo de perfiles académicos por estudiante")
public class PerfilAcademicoController {

    private final PerfilAcademicoService service;
    private final PerfilAcademicoMapper mapper;

    @GetMapping("/{referenciaEstudiante}")
    @Operation(summary = "Obtener perfil académico",
               description = "Retorna el perfil académico básico del estudiante identificado por referenciaEstudiante")
    public ResponseEntity<PerfilAcademicoResponse> obtener(@PathVariable String referenciaEstudiante) {
        return ResponseEntity.ok(mapper.toResponse(service.obtenerPorReferencia(referenciaEstudiante)));
    }

    @PostMapping("/{referenciaEstudiante}/recalcular")
    @Operation(summary = "Recalcular perfil académico",
               description = "Recalcula fortalezas, brechas y tendencia a partir de calificaciones y feedback existentes")
    public ResponseEntity<PerfilAcademicoResponse> recalcular(@PathVariable String referenciaEstudiante) {
        return ResponseEntity.ok(mapper.toResponse(service.recalcular(referenciaEstudiante)));
    }

    @GetMapping("/{referenciaEstudiante}/recomendaciones")
    @Operation(summary = "Obtener recomendaciones",
               description = "Retorna las recomendaciones activas generadas para el estudiante")
    public ResponseEntity<List<RecomendacionResponse>> obtenerRecomendaciones(@PathVariable String referenciaEstudiante) {
        return ResponseEntity.ok(
                service.obtenerRecomendaciones(referenciaEstudiante).stream()
                        .map(mapper::toRecomendacionResponse)
                        .toList());
    }
}
