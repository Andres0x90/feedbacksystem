package co.edu.tdea.feedbacksystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionReferenciaResponse {
    private UUID id;
    private UUID actividadEvaluativaId;
    private String actividadTitulo;
    private String referenciaEstudiante;
    private BigDecimal valorNumerico;
    private String observacionGeneral;
    private UUID feedbackId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaRegistro;
}
