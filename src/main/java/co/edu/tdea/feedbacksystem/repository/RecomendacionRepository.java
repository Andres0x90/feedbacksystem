package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, UUID> {

    List<Recomendacion> findByPerfilAcademicoId(UUID perfilAcademicoId);

    List<Recomendacion> findByPerfilAcademicoIdAndEstado(UUID perfilAcademicoId, String estado);
}
