package co.edu.tdea.feedbacksystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "plantilla_retroalimentacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantillaRetroalimentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "El nombre de la plantilla es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "objetivo", columnDefinition = "TEXT")
    private String objetivo;

    @Column(name = "contenido_base", columnDefinition = "TEXT")
    private String contenidoBase;

    @NotNull(message = "La versión es obligatoria")
    @Min(value = 1, message = "La versión debe ser mayor o igual a 1")
    @Column(name = "version", nullable = false)
    private Integer version;

    @NotNull(message = "El estado activa es obligatorio")
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "plantilla", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ActividadEvaluativaReferencia> actividades = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (version == null) version = 1;
        if (activa == null) activa = true;
    }
}
