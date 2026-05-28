package co.edu.tdea.feedbacksystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for rubric (Rubrica) information.
 * Contains all fields from the Rubrica entity plus nested criteria and performance levels.
 *
 * @author Feedback System Team
 * @version 1.0
 * @since 2026-05-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RubricaResponse {

    /**
     * Unique identifier of the rubric.
     */
    private UUID id;

    /**
     * Name of the rubric.
     */
    private String nombre;

    /**
     * Description of the rubric.
     */
    private String descripcion;

    /**
     * Version number of this rubric.
     * Used to track different versions of the same rubric.
     */
    private Integer version;

    /**
     * Status indicating whether the rubric is active.
     */
    private Boolean activa;

    /**
     * List of criteria that compose this rubric.
     */
    private List<CriterioResponse> criterios;

    /**
     * Timestamp when the rubric was created.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the rubric was last updated.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
