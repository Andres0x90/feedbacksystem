package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO for creating a new criterion (Criterio).
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
public class CreateCriterioRequest {

    /**
     * Name of the criterion.
     * Must not be blank.
     */
    @NotBlank(message = "El nombre del criterio no puede estar vacio")
    private String nombre;

    /**
     * Description of the criterion.
     * Optional field providing detailed information about this criterion.
     */
    private String descripcion;

    /**
     * Weight of this criterion in the rubric.
     * Must be between 0.01 and 100.
     */
    @NotNull(message = "El peso del criterio no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor o igual a 0.01")
    @DecimalMax(value = "100", message = "El peso debe ser menor o igual a 100")
    private BigDecimal peso;

    /**
     * Display order of this criterion within the rubric.
     * Must be at least 1.
     */
    @NotNull(message = "El orden no puede ser nulo")
    @Min(value = 1, message = "El orden debe ser mayor o igual a 1")
    private Integer orden;

    /**
     * List of performance levels for this criterion.
     * Must not be empty and all elements must be valid.
     */
    @NotEmpty(message = "El criterio debe tener al menos un nivel de desempeno")
    @Valid
    private List<CreateNivelDesempenoRequest> niveles;
}
