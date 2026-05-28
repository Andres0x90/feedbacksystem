package co.edu.tdea.feedbacksystem.repository;

import co.edu.tdea.feedbacksystem.entity.NivelDesempeno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing NivelDesempeno entities.
 * Provides CRUD operations and custom queries for performance level management within criteria.
 *
 * @author Feedback System Team
 * @version 1.0
 * @since 2026-05-27
 */
@Repository
public interface NivelDesempenoRepository extends JpaRepository<NivelDesempeno, UUID> {

    /**
     * Finds all performance levels belonging to a specific criterion.
     *
     * @param criterioId the UUID of the criterion
     * @return List of performance levels
     */
    List<NivelDesempeno> findByCriterioId(UUID criterioId);
}
