package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCalificacionReferenciaRequest {

    @NotNull(message = "El id de la actividad evaluativa es obligatorio")
    private UUID actividadEvaluativaId;

    @NotBlank(message = "La referencia del estudiante es obligatoria")
    private String referenciaEstudiante;

    @NotNull(message = "El valor numérico es obligatorio")
    @DecimalMin(value = "0", message = "El valor numérico debe ser mayor o igual a 0")
    @DecimalMax(value = "5", message = "El valor numérico debe ser menor o igual a 5")
    private BigDecimal valorNumerico;

    private String observacionGeneral;
}
