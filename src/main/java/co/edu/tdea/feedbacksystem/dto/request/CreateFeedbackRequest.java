package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeedbackRequest {

    @NotNull(message = "El id de la calificación de referencia es obligatorio")
    private UUID calificacionReferenciaId;

    private String resumenGeneral;

    @Valid
    private List<CreateComentarioCriterioRequest> comentarios;
}
