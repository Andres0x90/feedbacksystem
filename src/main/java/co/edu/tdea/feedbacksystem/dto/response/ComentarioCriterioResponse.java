package co.edu.tdea.feedbacksystem.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioCriterioResponse {
    private UUID id;
    private UUID feedbackId;
    private UUID criterioId;
    private String criterioNombre;
    private String comentario;
    private String fortaleza;
    private String oportunidadMejora;
}
