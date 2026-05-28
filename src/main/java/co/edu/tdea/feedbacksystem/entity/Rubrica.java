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
@Table(name = "rubrica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rubrica {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "El nombre de la rúbrica es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "La versión es obligatoria")
    @Min(value = 1, message = "La versión debe ser mayor o igual a 1")
    @Column(name = "version", nullable = false)
    private Integer version;

    @NotNull(message = "El estado activa es obligatorio")
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "rubrica", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Criterio> criterios = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Helper methods for bidirectional relationship
    public void addCriterio(Criterio criterio) {
        criterios.add(criterio);
        criterio.setRubrica(this);
    }

    public void removeCriterio(Criterio criterio) {
        criterios.remove(criterio);
        criterio.setRubrica(null);
    }
}
