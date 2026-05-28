package co.edu.tdea.feedbacksystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "criterio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Criterio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rubrica_id", nullable = false, insertable = false, updatable = false)
    private UUID rubricaId;

    @NotBlank(message = "El nombre del criterio es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor o igual a 0.01")
    @DecimalMax(value = "100", message = "El peso debe ser menor o igual a 100")
    @Column(name = "peso", nullable = false, precision = 5, scale = 2)
    private BigDecimal peso;

    @NotNull(message = "El orden es obligatorio")
    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rubrica_id", nullable = false)
    private Rubrica rubrica;

    @OneToMany(mappedBy = "criterio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    @Builder.Default
    private List<NivelDesempeno> nivelesDesempeno = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (rubrica != null) {
            rubricaId = rubrica.getId();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Helper methods for bidirectional relationship
    public void addNivelDesempeno(NivelDesempeno nivelDesempeno) {
        nivelesDesempeno.add(nivelDesempeno);
        nivelDesempeno.setCriterio(this);
    }

    public void removeNivelDesempeno(NivelDesempeno nivelDesempeno) {
        nivelesDesempeno.remove(nivelDesempeno);
        nivelDesempeno.setCriterio(null);
    }
}
