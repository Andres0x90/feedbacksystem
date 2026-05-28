package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.Feedback;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    @Override
    @EntityGraph(attributePaths = {
            "calificacionReferencia",
            "calificacionReferencia.actividadEvaluativa",
            "comentarios",
            "comentarios.criterio"
    })
    Optional<Feedback> findById(UUID id);

    @EntityGraph(attributePaths = {
            "calificacionReferencia",
            "calificacionReferencia.actividadEvaluativa"
    })
    Optional<Feedback> findByCalificacionReferenciaId(UUID calificacionReferenciaId);

    @Query("""
            SELECT f FROM Feedback f
            JOIN FETCH f.calificacionReferencia cr
            JOIN FETCH cr.actividadEvaluativa
            LEFT JOIN FETCH f.comentarios c
            LEFT JOIN FETCH c.criterio
            WHERE cr.referenciaEstudiante = :ref
            ORDER BY f.fechaCreacion DESC
            """)
    List<Feedback> findByReferenciaEstudiante(@Param("ref") String referenciaEstudiante);

    @Query("""
            SELECT f FROM Feedback f
            JOIN FETCH f.calificacionReferencia cr
            JOIN FETCH cr.actividadEvaluativa
            LEFT JOIN FETCH f.comentarios c
            LEFT JOIN FETCH c.criterio
            WHERE cr.actividadEvaluativa.id = :actividadId
            """)
    List<Feedback> findByActividadId(@Param("actividadId") UUID actividadId);

    long countByEstado(String estado);
}
