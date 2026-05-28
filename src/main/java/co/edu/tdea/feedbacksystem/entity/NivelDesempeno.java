package co.edu.tdea.feedbacksystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "nivel_desempeno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelDesempeno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "criterio_id", nullable = false, insertable = false, updatable = false)
    private UUID criterioId;

    @NotBlank(message = "El nombre del nivel de desempeño es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El puntaje mínimo es obligatorio")
    @DecimalMin(value = "0", message = "El puntaje mínimo debe ser mayor o igual a 0")
    @Column(name = "puntaje_min", nullable = false, precision = 5, scale = 2)
    private BigDecimal puntajeMin;

    @NotNull(message = "El puntaje máximo es obligatorio")
    @DecimalMin(value = "0", message = "El puntaje máximo debe ser mayor o igual a 0")
    @Column(name = "puntaje_max", nullable = false, precision = 5, scale = 2)
    private BigDecimal puntajeMax;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criterio_id", nullable = false)
    private Criterio criterio;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (criterio != null) {
            criterioId = criterio.getId();
        }
        validatePuntajes();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        validatePuntajes();
    }

    private void validatePuntajes() {
        if (puntajeMin != null && puntajeMax != null && puntajeMin.compareTo(puntajeMax) > 0) {
            throw new IllegalArgumentException("El puntaje mínimo no puede ser mayor que el puntaje máximo");
        }
    }
}
