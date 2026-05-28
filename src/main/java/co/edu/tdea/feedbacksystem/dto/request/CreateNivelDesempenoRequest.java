package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new performance level (Nivel de Desempeno).
 * Contains validation constraints to ensure data integrity.
 *
 * @author Feedback System Team
 * @version 1.0
 * @since 2026-05-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNivelDesempenoRequest {

    /**
     * Name of the performance level.
     * Must not be blank.
     */
    @NotBlank(message = "El nombre del nivel de desempeno no puede estar vacio")
    private String nombre;

    /**
     * Description of the performance level.
     * Optional field providing detailed information about this level.
     */
    private String descripcion;

    /**
     * Minimum score for this performance level.
     * Must not be null and must be at least 0.
     */
    @NotNull(message = "El puntaje minimo no puede ser nulo")
    @DecimalMin(value = "0", message = "El puntaje minimo debe ser mayor o igual a 0")
    private BigDecimal puntajeMin;

    /**
     * Maximum score for this performance level.
     * Must not be null and must be at least 0.
     */
    @NotNull(message = "El puntaje maximo no puede ser nulo")
    @DecimalMin(value = "0", message = "El puntaje maximo debe ser mayor o igual a 0")
    private BigDecimal puntajeMax;
}
