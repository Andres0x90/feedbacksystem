package co.edu.tdea.feedbacksystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteBasicoResponse {
    private UUID id;
    private UUID perfilAcademicoId;
    private String tipo;
    private String rangoFechas;
    private Integer totalFeedbacks;
    private String indicadores;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaGeneracion;
}
