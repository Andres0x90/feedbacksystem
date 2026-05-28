package co.edu.tdea.feedbacksystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for performance level (Nivel de Desempeno) information.
 * Contains all fields from the NivelDesempeno entity.
 *
 * @author Feedback System Team
 * @version 1.0
 * @since 2026-05-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NivelDesempenoResponse {

    /**
     * Unique identifier of the performance level.
     */
    private UUID id;

    /**
     * UUID of the criterion this performance level belongs to.
     */
    private UUID criterioId;

    /**
     * Name of the performance level.
     */
    private String nombre;

    /**
     * Description of the performance level.
     */
    private String descripcion;

    /**
     * Minimum score for this performance level.
     */
    private BigDecimal puntajeMin;

    /**
     * Maximum score for this performance level.
     */
    private BigDecimal puntajeMax;

    /**
     * Timestamp when the performance level was created.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the performance level was last updated.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
