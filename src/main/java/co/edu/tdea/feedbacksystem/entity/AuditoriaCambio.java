package co.edu.tdea.feedbacksystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auditoria_cambio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaCambio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "La entidad es obligatoria")
    @Column(name = "entidad", nullable = false, length = 100)
    private String entidad;

    @Column(name = "entidad_id")
    private UUID entidadId;

    @NotBlank(message = "La acción es obligatoria")
    @Column(name = "accion", nullable = false, length = 100)
    private String accion;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }
}
