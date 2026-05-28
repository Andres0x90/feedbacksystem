package co.edu.tdea.feedbacksystem.controller;

import co.edu.tdea.feedbacksystem.dto.response.AuditoriaCambioResponse;
import co.edu.tdea.feedbacksystem.mapper.AuditoriaCambioMapper;
import co.edu.tdea.feedbacksystem.service.AuditoriaCambioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auditoria")
@RequiredArgsConstructor
@Tag(name = "Auditoría", description = "Registro de trazabilidad de cambios en el sistema")
public class AuditoriaCambioController {

    private final AuditoriaCambioService service;
    private final AuditoriaCambioMapper mapper;

    @GetMapping
    @Operation(summary = "Listar registros de auditoría",
               description = "Retorna todos los cambios registrados paginados, ordenados por fecha descendente")
    public ResponseEntity<Page<AuditoriaCambioResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<co.edu.tdea.feedbacksystem.entity.AuditoriaCambio> auditPage = service.listarTodos(page, size);
        List<AuditoriaCambioResponse> content = mapper.toResponseList(auditPage.getContent());
        return ResponseEntity.ok(new PageImpl<>(content, auditPage.getPageable(), auditPage.getTotalElements()));
    }

    @GetMapping("/{entidad}/{entidadId}")
    @Operation(summary = "Listar auditoría por entidad",
               description = "Retorna el historial de cambios de un registro específico identificado por tipo y UUID")
    public ResponseEntity<List<AuditoriaCambioResponse>> listarPorEntidad(
            @PathVariable String entidad,
            @PathVariable UUID entidadId) {
        return ResponseEntity.ok(mapper.toResponseList(service.listarPorEntidad(entidad, entidadId)));
    }
}
