package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.entity.*;
import co.edu.tdea.feedbacksystem.exception.BusinessValidationException;
import co.edu.tdea.feedbacksystem.exception.ResourceNotFoundException;
import co.edu.tdea.feedbacksystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfilAcademicoService {

    private final PerfilAcademicoBasicoRepository perfilRepository;
    private final CalificacionReferenciaRepository calificacionRepository;
    private final ComentarioCriterioRepository comentarioRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final AuditoriaCambioService auditoriaCambioService;

    @Transactional(readOnly = true)
    public PerfilAcademicoBasico obtenerPorReferencia(String referenciaEstudiante) {
        return perfilRepository.findByReferenciaEstudiante(referenciaEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("PerfilAcademicoBasico", referenciaEstudiante));
    }

    @Transactional
    public PerfilAcademicoBasico recalcular(String referenciaEstudiante) {
        List<CalificacionReferencia> calificaciones = calificacionRepository.findByReferenciaEstudiante(referenciaEstudiante);

        if (calificaciones.isEmpty()) {
            throw new BusinessValidationException(
                    "No se encontraron calificaciones para el estudiante '" + referenciaEstudiante
                    + "'. No es posible calcular el perfil.");
        }

        List<ComentarioCriterio> comentarios = comentarioRepository.findByReferenciaEstudiante(referenciaEstudiante);

        String fortalezas = comentarios.stream()
                .filter(c -> c.getFortaleza() != null && !c.getFortaleza().isBlank())
                .map(c -> "• " + c.getFortaleza())
                .distinct()
                .collect(Collectors.joining("\n"));

        String brechas = comentarios.stream()
                .filter(c -> c.getOportunidadMejora() != null && !c.getOportunidadMejora().isBlank())
                .map(c -> "• " + c.getOportunidadMejora())
                .distinct()
                .collect(Collectors.joining("\n"));

        String tendencia = calcularTendencia(calificaciones);

        PerfilAcademicoBasico perfil = perfilRepository.findByReferenciaEstudiante(referenciaEstudiante)
                .orElse(PerfilAcademicoBasico.builder()
                        .referenciaEstudiante(referenciaEstudiante)
                        .build());

        perfil.setFortalezas(fortalezas.isBlank() ? "Sin fortalezas identificadas aún" : fortalezas);
        perfil.setBrechas(brechas.isBlank() ? "Sin brechas identificadas aún" : brechas);
        perfil.setTendencia(tendencia);

        PerfilAcademicoBasico saved = perfilRepository.save(perfil);

        generarRecomendaciones(saved, brechas, tendencia);

        auditoriaCambioService.registrar("PerfilAcademicoBasico", saved.getId(), "RECALCULAR",
                "Perfil recalculado para estudiante: " + referenciaEstudiante);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Recomendacion> obtenerRecomendaciones(String referenciaEstudiante) {
        PerfilAcademicoBasico perfil = obtenerPorReferencia(referenciaEstudiante);
        return recomendacionRepository.findByPerfilAcademicoId(perfil.getId());
    }

    private String calcularTendencia(List<CalificacionReferencia> calificaciones) {
        if (calificaciones.size() < 2) return "SIN_DATOS";

        int mitad = calificaciones.size() / 2;
        BigDecimal promedioReciente = calificaciones.subList(mitad, calificaciones.size())
                .stream().map(CalificacionReferencia::getValorNumerico)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(calificaciones.size() - mitad), 2, RoundingMode.HALF_UP);

        BigDecimal promedioAnterior = calificaciones.subList(0, mitad)
                .stream().map(CalificacionReferencia::getValorNumerico)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(mitad), 2, RoundingMode.HALF_UP);

        int comparacion = promedioReciente.compareTo(promedioAnterior);
        if (comparacion > 0) return "MEJORANDO";
        if (comparacion < 0) return "DECLINANDO";
        return "ESTABLE";
    }

    private void generarRecomendaciones(PerfilAcademicoBasico perfil, String brechas, String tendencia) {
        recomendacionRepository.deleteAll(recomendacionRepository.findByPerfilAcademicoId(perfil.getId()));

        if (!brechas.isBlank() && !brechas.contains("Sin brechas")) {
            Recomendacion rec = Recomendacion.builder()
                    .perfilAcademico(perfil)
                    .tipo("MEJORA")
                    .descripcion("Se identificaron las siguientes áreas de mejora: " + brechas)
                    .prioridad("ALTA")
                    .estado("ACTIVA")
                    .build();
            recomendacionRepository.save(rec);
        }

        if ("DECLINANDO".equals(tendencia)) {
            Recomendacion rec = Recomendacion.builder()
                    .perfilAcademico(perfil)
                    .tipo("ALERTA")
                    .descripcion("Se detecta tendencia declinante en el desempeño del estudiante. Se recomienda intervención temprana.")
                    .prioridad("ALTA")
                    .estado("ACTIVA")
                    .build();
            recomendacionRepository.save(rec);
        } else if ("MEJORANDO".equals(tendencia)) {
            Recomendacion rec = Recomendacion.builder()
                    .perfilAcademico(perfil)
                    .tipo("REFUERZO")
                    .descripcion("El estudiante muestra tendencia positiva. Continuar con las estrategias actuales.")
                    .prioridad("BAJA")
                    .estado("ACTIVA")
                    .build();
            recomendacionRepository.save(rec);
        }
    }

}
