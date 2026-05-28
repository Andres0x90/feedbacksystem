package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateComentarioCriterioRequest {

    @NotNull(message = "El id del criterio es obligatorio")
    private UUID criterioId;

    private String comentario;

    private String fortaleza;

    private String oportunidadMejora;
}
