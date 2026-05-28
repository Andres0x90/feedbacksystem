package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.Criterio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CriterioRepository extends JpaRepository<Criterio, UUID> {

    @Override
    @EntityGraph(attributePaths = {"nivelesDesempeno", "rubrica"})
    Optional<Criterio> findById(UUID id);

    @EntityGraph(attributePaths = {"nivelesDesempeno", "rubrica"})
    List<Criterio> findByRubricaIdOrderByOrden(UUID rubricaId);

    List<Criterio> findByRubricaId(UUID rubricaId);
}
