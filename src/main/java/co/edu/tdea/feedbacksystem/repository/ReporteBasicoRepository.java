package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.ReporteBasico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReporteBasicoRepository extends JpaRepository<ReporteBasico, UUID> {

    List<ReporteBasico> findByPerfilAcademicoId(UUID perfilAcademicoId);

    List<ReporteBasico> findByTipo(String tipo);
}
