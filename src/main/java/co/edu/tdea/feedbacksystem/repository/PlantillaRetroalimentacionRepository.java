package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.PlantillaRetroalimentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlantillaRetroalimentacionRepository extends JpaRepository<PlantillaRetroalimentacion, UUID> {

    List<PlantillaRetroalimentacion> findByActivaTrue();

    Optional<PlantillaRetroalimentacion> findTopByNombreOrderByVersionDesc(String nombre);
}
