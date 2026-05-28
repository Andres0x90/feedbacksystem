package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.CalificacionReferencia;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalificacionReferenciaRepository extends JpaRepository<CalificacionReferencia, UUID> {

    @Override
    @EntityGraph(attributePaths = {"actividadEvaluativa", "actividadEvaluativa.rubrica", "feedback"})
    Optional<CalificacionReferencia> findById(UUID id);

    @EntityGraph(attributePaths = {"actividadEvaluativa", "actividadEvaluativa.rubrica", "feedback"})
    List<CalificacionReferencia> findByReferenciaEstudiante(String referenciaEstudiante);

    @EntityGraph(attributePaths = {"actividadEvaluativa", "feedback"})
    List<CalificacionReferencia> findByActividadEvaluativaId(UUID actividadEvaluativaId);

    boolean existsByActividadEvaluativaIdAndReferenciaEstudiante(UUID actividadEvaluativaId, String referenciaEstudiante);

    @Query("SELECT AVG(c.valorNumerico) FROM CalificacionReferencia c WHERE c.referenciaEstudiante = :ref")
    Optional<BigDecimal> findPromedioByReferenciaEstudiante(@Param("ref") String referenciaEstudiante);

    @Query("SELECT AVG(c.valorNumerico) FROM CalificacionReferencia c WHERE c.actividadEvaluativa.id = :actividadId")
    Optional<BigDecimal> findPromedioByActividadId(@Param("actividadId") UUID actividadId);
}
