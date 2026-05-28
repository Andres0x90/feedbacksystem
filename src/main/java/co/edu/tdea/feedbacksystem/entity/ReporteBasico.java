package co.edu.tdea.feedbacksystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reporte_basico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteBasico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_academico_id")
    private PerfilAcademicoBasico perfilAcademico;

    @NotBlank(message = "El tipo de reporte es obligatorio")
    @Column(name = "tipo", nullable = false, length = 100)
    private String tipo;

    @Column(name = "rango_fechas", length = 255)
    private String rangoFechas;

    @Column(name = "total_feedbacks")
    private Integer totalFeedbacks;

    @Column(name = "indicadores", columnDefinition = "TEXT")
    private String indicadores;

    @Column(name = "fecha_generacion", nullable = false, updatable = false)
    private LocalDateTime fechaGeneracion;

    @PrePersist
    protected void onCreate() {
        fechaGeneracion = LocalDateTime.now();
        if (totalFeedbacks == null) totalFeedbacks = 0;
    }
}
