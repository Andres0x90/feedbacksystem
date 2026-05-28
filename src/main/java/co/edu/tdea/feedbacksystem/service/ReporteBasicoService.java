package co.edu.tdea.feedbacksystem.service;

import co.edu.tdea.feedbacksystem.entity.CalificacionReferencia;
import co.edu.tdea.feedbacksystem.entity.Feedback;
import co.edu.tdea.feedbacksystem.entity.ReporteBasico;
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

@Service
@RequiredArgsConstructor
public class ReporteBasicoService {

    private final ReporteBasicoRepository reporteRepository;
    private final FeedbackRepository feedbackRepository;
    private final CalificacionReferenciaRepository calificacionRepository;
    private final ActividadEvaluativaReferenciaRepository actividadRepository;

    @Transactional
    public ReporteBasico generarReporteActividad(UUID actividadId) {
        actividadRepository.findById(actividadId)
                .orElseThrow(() -> new ResourceNotFoundException("ActividadEvaluativaReferencia", actividadId));

        List<CalificacionReferencia> calificaciones = calificacionRepository.findByActividadEvaluativaId(actividadId);
        List<Feedback> feedbacks = feedbackRepository.findByActividadId(actividadId);

        Optional<BigDecimal> promedio = calificacionRepository.findPromedioByActividadId(actividadId);

        String indicadores = buildIndicadoresActividad(calificaciones, feedbacks, promedio);

        ReporteBasico reporte = ReporteBasico.builder()
                .tipo("ACTIVIDAD")
                .rangoFechas(actividadId.toString())
                .totalFeedbacks(feedbacks.size())
                .indicadores(indicadores)
                .build();

        return reporteRepository.save(reporte);
    }

    @Transactional
    public ReporteBasico generarReporteFeedback() {
        long total = feedbackRepository.count();
        long publicados = feedbackRepository.countByEstado("PUBLICADO");
        long borradores = feedbackRepository.countByEstado("BORRADOR");

        String indicadores = String.format(
                "{\"total\":%d,\"publicados\":%d,\"borradores\":%d}", total, publicados, borradores);

        ReporteBasico reporte = ReporteBasico.builder()
                .tipo("FEEDBACK_GENERAL")
                .totalFeedbacks((int) total)
                .indicadores(indicadores)
                .build();

        return reporteRepository.save(reporte);
    }

    @Transactional
    public ReporteBasico generarReporteCoordinacion() {
        long totalCalificaciones = calificacionRepository.count();
        long totalFeedbacks = feedbackRepository.count();
        long feedbacksPublicados = feedbackRepository.countByEstado("PUBLICADO");

        String indicadores = String.format(
                "{\"totalCalificaciones\":%d,\"totalFeedbacks\":%d,\"feedbacksPublicados\":%d,\"tasaFeedback\":\"%.1f%%\"}",
                totalCalificaciones, totalFeedbacks, feedbacksPublicados,
                totalCalificaciones > 0 ? (feedbacksPublicados * 100.0 / totalCalificaciones) : 0.0);

        ReporteBasico reporte = ReporteBasico.builder()
                .tipo("COORDINACION")
                .totalFeedbacks((int) totalFeedbacks)
                .indicadores(indicadores)
                .build();

        return reporteRepository.save(reporte);
    }

    private String buildIndicadoresActividad(List<CalificacionReferencia> calificaciones,
                                              List<Feedback> feedbacks,
                                              Optional<BigDecimal> promedio) {
        int total = calificaciones.size();
        long aprobados = calificaciones.stream()
                .filter(c -> c.getValorNumerico().compareTo(new BigDecimal("3.0")) >= 0)
                .count();

        return String.format(
                "{\"totalEstudiantes\":%d,\"estudiantesAprobados\":%d,\"promedioCalificacion\":%.2f,\"totalFeedbacks\":%d}",
                total, aprobados,
                promedio.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                feedbacks.size());
    }
}
