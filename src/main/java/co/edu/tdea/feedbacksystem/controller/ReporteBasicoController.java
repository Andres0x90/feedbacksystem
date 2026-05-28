package co.edu.tdea.feedbacksystem.controller;

import co.edu.tdea.feedbacksystem.dto.response.ReporteBasicoResponse;
import co.edu.tdea.feedbacksystem.mapper.ReporteBasicoMapper;
import co.edu.tdea.feedbacksystem.service.ReporteBasicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes Básicos", description = "Generación de reportes agregados para docentes y coordinación académica")
public class ReporteBasicoController {

    private final ReporteBasicoService service;
    private final ReporteBasicoMapper mapper;

    @GetMapping("/actividad/{actividadId}")
    @Operation(summary = "Reporte por actividad",
               description = "Genera estadísticas de calificaciones y feedback para una actividad evaluativa específica")
    public ResponseEntity<ReporteBasicoResponse> reporteActividad(@PathVariable UUID actividadId) {
        return ResponseEntity.ok(mapper.toResponse(service.generarReporteActividad(actividadId)));
    }

    @GetMapping("/feedback")
    @Operation(summary = "Reporte de feedback general",
               description = "Genera un resumen del estado de todos los feedbacks registrados")
    public ResponseEntity<ReporteBasicoResponse> reporteFeedback() {
        return ResponseEntity.ok(mapper.toResponse(service.generarReporteFeedback()));
    }

    @GetMapping("/coordinacion")
    @Operation(summary = "Reporte para coordinación académica",
               description = "Genera indicadores globales: total de calificaciones, feedbacks y tasa de cobertura")
    public ResponseEntity<ReporteBasicoResponse> reporteCoordinacion() {
        return ResponseEntity.ok(mapper.toResponse(service.generarReporteCoordinacion()));
    }
}
