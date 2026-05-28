package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating an existing rubric (Rubrica).
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
public class UpdateRubricaRequest {

    /**
     * Updated name of the rubric.
     * Must not be blank.
     */
    @NotBlank(message = "El nombre de la rubrica no puede estar vacio")
    private String nombre;

    /**
     * Updated description of the rubric.
     * Optional field providing detailed information about this rubric.
     */
    private String descripcion;

    /**
     * Status indicating whether the rubric is active.
     * Must not be null.
     */
    @NotNull(message = "El estado activa no puede ser nulo")
    private Boolean activa;
}
