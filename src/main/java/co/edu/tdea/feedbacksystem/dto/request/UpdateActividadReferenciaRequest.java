package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateActividadReferenciaRequest {

    private UUID plantillaId;

    @NotBlank(message = "El título de la actividad es obligatorio")
    private String titulo;

    private String descripcion;

    private LocalDate fecha;

    @DecimalMin(value = "0", message = "La ponderación debe ser mayor o igual a 0")
    @DecimalMax(value = "100", message = "La ponderación debe ser menor o igual a 100")
    private BigDecimal ponderacion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
