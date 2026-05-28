package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.ActividadEvaluativaReferencia;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActividadEvaluativaReferenciaRepository extends JpaRepository<ActividadEvaluativaReferencia, UUID> {

    @Override
    @EntityGraph(attributePaths = {"rubrica", "plantilla"})
    Optional<ActividadEvaluativaReferencia> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"rubrica", "plantilla"})
    List<ActividadEvaluativaReferencia> findAll();

    @EntityGraph(attributePaths = {"rubrica", "plantilla"})
    List<ActividadEvaluativaReferencia> findByRubricaId(UUID rubricaId);

    List<ActividadEvaluativaReferencia> findByEstado(String estado);
}
