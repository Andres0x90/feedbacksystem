package co.edu.tdea.feedbacksystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "perfil_academico_basico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilAcademicoBasico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "La referencia del estudiante es obligatoria")
    @Column(name = "referencia_estudiante", nullable = false, unique = true)
    private String referenciaEstudiante;

    @Column(name = "fortalezas", columnDefinition = "TEXT")
    private String fortalezas;

    @Column(name = "brechas", columnDefinition = "TEXT")
    private String brechas;

    @Column(name = "tendencia", length = 50)
    private String tendencia;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "perfilAcademico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Recomendacion> recomendaciones = new ArrayList<>();

    @OneToMany(mappedBy = "perfilAcademico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReporteBasico> reportes = new ArrayList<>();

    @PrePersist
    @PreUpdate
    protected void onSave() {
        fechaActualizacion = LocalDateTime.now();
    }
}
