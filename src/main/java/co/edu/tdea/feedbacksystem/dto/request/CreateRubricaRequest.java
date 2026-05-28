package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for creating a new rubric (Rubrica).
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
public class CreateRubricaRequest {

    /**
     * Name of the rubric.
     * Must not be blank.
     */
    @NotBlank(message = "El nombre de la rubrica no puede estar vacio")
    private String nombre;

    /**
     * Description of the rubric.
     * Optional field providing detailed information about this rubric.
     */
    private String descripcion;

    /**
     * List of criteria that compose this rubric.
     * Must not be empty and all elements must be valid.
     */
    @NotEmpty(message = "La rubrica debe tener al menos un criterio")
    @Valid
    private List<CreateCriterioRequest> criterios;
}
