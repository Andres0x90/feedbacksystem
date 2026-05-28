package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlantillaRequest {

    @NotBlank(message = "El nombre de la plantilla es obligatorio")
    private String nombre;

    private String objetivo;

    private String contenidoBase;
}
