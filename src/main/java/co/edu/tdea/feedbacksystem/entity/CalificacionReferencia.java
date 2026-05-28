package co.edu.tdea.feedbacksystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "calificacion_referencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionReferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "La actividad evaluativa es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_evaluativa_id", nullable = false)
    private ActividadEvaluativaReferencia actividadEvaluativa;

    @NotBlank(message = "La referencia del estudiante es obligatoria")
    @Column(name = "referencia_estudiante", nullable = false)
    private String referenciaEstudiante;

    @NotNull(message = "El valor numérico es obligatorio")
    @DecimalMin(value = "0", message = "El valor numérico debe ser mayor o igual a 0")
    @DecimalMax(value = "5", message = "El valor numérico debe ser menor o igual a 5")
    @Column(name = "valor_numerico", nullable = false, precision = 4, scale = 2)
    private BigDecimal valorNumerico;

    @Column(name = "observacion_general", columnDefinition = "TEXT")
    private String observacionGeneral;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @OneToOne(mappedBy = "calificacionReferencia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Feedback feedback;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
