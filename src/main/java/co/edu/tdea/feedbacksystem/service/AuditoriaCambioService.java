package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.entity.AuditoriaCambio;
import co.edu.tdea.feedbacksystem.repository.AuditoriaCambioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditoriaCambioService {

    private final AuditoriaCambioRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(String entidad, UUID entidadId, String accion, String detalle) {
        AuditoriaCambio registro = AuditoriaCambio.builder()
                .entidad(entidad)
                .entidadId(entidadId)
                .accion(accion)
                .detalle(detalle)
                .build();
        repository.save(registro);
    }

    @Transactional(readOnly = true)
    public Page<AuditoriaCambio> listarTodos(int page, int size) {
        return repository.findAllByOrderByFechaDesc(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public List<AuditoriaCambio> listarPorEntidad(String entidad, UUID entidadId) {
        return repository.findByEntidadAndEntidadIdOrderByFechaDesc(entidad, entidadId);
    }
}
