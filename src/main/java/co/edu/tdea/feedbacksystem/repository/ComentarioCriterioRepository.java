package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.ComentarioCriterio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComentarioCriterioRepository extends JpaRepository<ComentarioCriterio, UUID> {

    List<ComentarioCriterio> findByFeedbackId(UUID feedbackId);

    List<ComentarioCriterio> findByCriterioId(UUID criterioId);

    @Query("""
            SELECT cc FROM ComentarioCriterio cc
            JOIN cc.feedback f
            JOIN f.calificacionReferencia cr
            WHERE cr.referenciaEstudiante = :ref
            """)
    List<ComentarioCriterio> findByReferenciaEstudiante(@Param("ref") String referenciaEstudiante);
}
