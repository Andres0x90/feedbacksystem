package co.edu.tdea.feedbacksystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlantillaRequest {

    @NotBlank(message = "El nombre de la plantilla es obligatorio")
    private String nombre;

    private String objetivo;

    private String contenidoBase;

    @NotNull(message = "El estado activa no puede ser nulo")
    private Boolean activa;
}
