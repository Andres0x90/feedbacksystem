package co.edu.tdea.feedbacksystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilAcademicoResponse {
    private UUID id;
    private String referenciaEstudiante;
    private String fortalezas;
    private String brechas;
    private String tendencia;
    private List<RecomendacionResponse> recomendaciones;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaActualizacion;
}
