package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.AuditoriaCambio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditoriaCambioRepository extends JpaRepository<AuditoriaCambio, UUID> {

    List<AuditoriaCambio> findByEntidadAndEntidadIdOrderByFechaDesc(String entidad, UUID entidadId);

    Page<AuditoriaCambio> findAllByOrderByFechaDesc(Pageable pageable);
}
