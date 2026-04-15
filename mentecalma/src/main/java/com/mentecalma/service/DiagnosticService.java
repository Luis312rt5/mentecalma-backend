package com.mentecalma.service;

import com.mentecalma.dto.request.CheckInRequest;
import com.mentecalma.dto.response.CheckInResponse;
import com.mentecalma.dto.response.RecommendationResponse;
import com.mentecalma.engine.InferenceEngine;
import com.mentecalma.engine.UserSession;
import com.mentecalma.model.CheckIn;
import com.mentecalma.model.HabitoLog;
import com.mentecalma.model.Usuario;
import com.mentecalma.repository.CheckInRepository;
import com.mentecalma.repository.HabitoLogRepository;
import com.mentecalma.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiagnosticService {

    private final CheckInRepository checkInRepository;
    private final HabitoLogRepository habitoLogRepository;
    private final UsuarioRepository usuarioRepository;
    private final InferenceEngine inferenceEngine;
    private final ObjectMapper objectMapper;

    private static final String MSG_CRITICO =
            "Tu nivel de estrés es muy alto. Te recomendamos hablar con un profesional. " +
                    "Línea 106 (Colombia) — atención gratuita las 24 horas.";

    @Transactional
    public CheckInResponse procesarCheckIn(CheckInRequest req, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        String sintomasJson = serializarSintomas(req.sintomas());

        CheckIn checkIn = CheckIn.builder()
                .usuario(usuario)
                .fecha(LocalDate.now())
                .nivelEstres(req.nivelEstres())
                .situacion(req.situacion())
                .horasSueno(req.horasSueno())
                .sintomasJson(sintomasJson)
                .notasLibres(req.notasLibres())
                .build();

        checkIn = checkInRepository.save(checkIn);

        UserSession session = UserSession.builder()
                .nivelEstres(req.nivelEstres())
                .situacion(req.situacion())
                .horasSueno(req.horasSueno())
                .sintomas(req.sintomas() != null ? req.sintomas() : List.of())
                .checkInId(checkIn.getId())
                .usuarioId(usuario.getId())
                .build();

        InferenceEngine.ResultadoInferencia resultado = inferenceEngine.inferir(session);

        if (resultado.esCritico()) {
            return new CheckInResponse(
                    checkIn.getId(), checkIn.getFecha(),
                    req.nivelEstres(), req.situacion(), req.horasSueno(),
                    "CRITICO", MSG_CRITICO, List.of()
            );
        }

        final CheckIn checkInFinal = checkIn;
        resultado.recomendaciones().forEach(rec -> {
            HabitoLog habitoLog = HabitoLog.builder()
                    .checkIn(checkInFinal)
                    .recomendacion(rec)
                    .completado(false)
                    .build();
            habitoLogRepository.save(habitoLog);
        });

        Map<Long, String> recIdANombreRegla = new HashMap<>();
        resultado.reglasActivadas().forEach(ra ->
                recIdANombreRegla.put(ra.recomendacion().getId(), ra.nombreRegla())
        );

        List<RecommendationResponse> recsDto = resultado.recomendaciones().stream()
                .map(rec -> new RecommendationResponse(
                        rec.getId(), rec.getTitulo(), rec.getTipo(),
                        rec.getDescripcion(), rec.getDuracionMin(),
                        rec.getContenidoJson(),
                        recIdANombreRegla.getOrDefault(rec.getId(), "—")
                ))
                .toList();

        String tipoResultado = resultado.recomendaciones().isEmpty()
                ? "SIN_COINCIDENCIAS" : "EXITOSO";

        return new CheckInResponse(
                checkIn.getId(), checkIn.getFecha(),
                req.nivelEstres(), req.situacion(), req.horasSueno(),
                tipoResultado, null, recsDto
        );
    }

    public List<CheckInResponse> obtenerHistorial(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        return checkInRepository.findAllByUsuarioIdOrderByFechaDesc(usuario.getId())
                .stream()
                .map(ci -> new CheckInResponse(
                        ci.getId(), ci.getFecha(), ci.getNivelEstres(),
                        ci.getSituacion(), ci.getHorasSueno(),
                        "HISTORICO", null, List.of()
                ))
                .toList();
    }

    private String serializarSintomas(List<String> sintomas) {
        if (sintomas == null || sintomas.isEmpty()) return "[]";
        try {
            return objectMapper.writeValueAsString(sintomas);
        } catch (Exception e) {
            return "[]";
        }
    }

    public CheckInResponse obtenerCheckInConRecomendaciones(Long checkInId, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        CheckIn checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new IllegalArgumentException("CheckIn no encontrado"));

        if (!checkIn.getUsuario().getId().equals(usuario.getId()))
            throw new SecurityException("No autorizado");

        List<HabitoLog> logs = habitoLogRepository.findAllByCheckInId(checkInId);

        List<RecommendationResponse> recsDto = logs.stream()
                .map(log -> new RecommendationResponse(
                        log.getRecomendacion().getId(),
                        log.getRecomendacion().getTitulo(),
                        log.getRecomendacion().getTipo(),
                        log.getRecomendacion().getDescripcion(),
                        log.getRecomendacion().getDuracionMin(),
                        log.getRecomendacion().getContenidoJson(),
                        "—"
                ))
                .toList();

        return new CheckInResponse(
                checkIn.getId(), checkIn.getFecha(),
                checkIn.getNivelEstres(), checkIn.getSituacion(),
                checkIn.getHorasSueno(), "HISTORICO", null, recsDto
        );
    }
}