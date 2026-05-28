package co.edu.tdea.feedbacksystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for criterion (Criterio) information.
 * Contains all fields from the Criterio entity plus nested performance levels.
 *
 * @author Feedback System Team
 * @version 1.0
 * @since 2026-05-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterioResponse {

    /**
     * Unique identifier of the criterion.
     */
    private UUID id;

    /**
     * UUID of the rubric this criterion belongs to.
     */
    private UUID rubricaId;

    /**
     * Name of the criterion.
     */
    private String nombre;

    /**
     * Description of the criterion.
     */
    private String descripcion;

    /**
     * Weight of this criterion in the rubric.
     */
    private BigDecimal peso;

    /**
     * Display order of this criterion within the rubric.
     */
    private Integer orden;

    /**
     * List of performance levels for this criterion.
     */
    private List<NivelDesempenoResponse> niveles;

    /**
     * Timestamp when the criterion was created.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the criterion was last updated.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
