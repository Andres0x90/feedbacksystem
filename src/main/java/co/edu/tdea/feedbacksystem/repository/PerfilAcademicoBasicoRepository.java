package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.PerfilAcademicoBasico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilAcademicoBasicoRepository extends JpaRepository<PerfilAcademicoBasico, UUID> {

    @EntityGraph(attributePaths = {"recomendaciones"})
    Optional<PerfilAcademicoBasico> findByReferenciaEstudiante(String referenciaEstudiante);

    @Override
    @EntityGraph(attributePaths = {"recomendaciones"})
    Optional<PerfilAcademicoBasico> findById(UUID id);
}
