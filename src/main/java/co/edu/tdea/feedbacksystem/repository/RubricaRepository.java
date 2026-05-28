package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.Rubrica;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RubricaRepository extends JpaRepository<Rubrica, UUID> {

    @Override
    @EntityGraph(attributePaths = {"criterios"})
    List<Rubrica> findAll();

    @Override
    @EntityGraph(attributePaths = {"criterios"})
    Optional<Rubrica> findById(UUID id);

    @EntityGraph(attributePaths = {"criterios"})
    List<Rubrica> findByActivaTrue();

    Optional<Rubrica> findByNombreAndVersion(String nombre, Integer version);

    Optional<Rubrica> findTopByNombreOrderByVersionDesc(String nombre);
}
