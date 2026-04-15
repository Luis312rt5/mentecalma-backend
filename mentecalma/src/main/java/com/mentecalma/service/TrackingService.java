package com.mentecalma.service;

import com.mentecalma.dto.request.HabitLogRequest;
import com.mentecalma.dto.response.UserStatsResponse;
import com.mentecalma.model.CheckIn;
import com.mentecalma.model.HabitoLog;
import com.mentecalma.model.Recomendacion;
import com.mentecalma.model.Usuario;
import com.mentecalma.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final HabitoLogRepository habitoLogRepository;
    private final CheckInRepository checkInRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public HabitoLog registrarHabito(HabitLogRequest req, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        CheckIn checkIn = checkInRepository.findById(req.checkInId())
                .orElseThrow(() -> new IllegalArgumentException("CheckIn no encontrado"));

        if (!checkIn.getUsuario().getId().equals(usuario.getId()))
            throw new SecurityException("No autorizado");

        Recomendacion rec = recomendacionRepository.findById(req.recomendacionId())
                .orElseThrow(() -> new IllegalArgumentException("Recomendación no encontrada"));

        List<HabitoLog> logs = habitoLogRepository.findAllByCheckInId(checkIn.getId());
        HabitoLog habitoLog = logs.stream()
                .filter(l -> l.getRecomendacion().getId().equals(rec.getId()))
                .findFirst()
                .orElse(HabitoLog.builder().checkIn(checkIn).recomendacion(rec).build());

        habitoLog.setCompletado(req.completado());
        if (req.efectividad() != null) habitoLog.setEfectividad(req.efectividad());

        return habitoLogRepository.save(habitoLog);
    }

    public UserStatsResponse obtenerEstadisticas(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        Long usuarioId = usuario.getId();

        int totalCheckins = checkInRepository.findAllByUsuarioIdOrderByFechaDesc(usuarioId).size();

        Double avg7  = checkInRepository.promedioEstresDesde(usuarioId, LocalDate.now().minusDays(7));
        Double avg30 = checkInRepository.promedioEstresDesde(usuarioId, LocalDate.now().minusDays(30));

        Long completados = habitoLogRepository.countCompletadosByUsuario(usuarioId);

        List<Object[]> top = habitoLogRepository.topRecomendacionEfectiva(usuarioId);
        String topTitulo = null;
        Double topEfectividad = null;
        if (!top.isEmpty()) {
            Long recId = (Long) top.get(0)[0];
            topEfectividad = (Double) top.get(0)[1];
            topTitulo = recomendacionRepository.findById(recId)
                    .map(Recomendacion::getTitulo).orElse("—");
        }

        int racha = calcularRacha(usuarioId);

        return new UserStatsResponse(
                totalCheckins,
                avg7  != null ? Math.round(avg7  * 10.0) / 10.0 : null,
                avg30 != null ? Math.round(avg30 * 10.0) / 10.0 : null,
                completados.intValue(),
                topTitulo,
                topEfectividad != null ? Math.round(topEfectividad * 10.0) / 10.0 : null,
                racha
        );
    }

    private int calcularRacha(Long usuarioId) {
        List<CheckIn> checkins = checkInRepository.findUltimos30(usuarioId);
        if (checkins.isEmpty()) return 0;

        int racha = 0;
        LocalDate esperada = LocalDate.now();
        for (CheckIn ci : checkins) {
            if (ci.getFecha().equals(esperada)) {
                racha++;
                esperada = esperada.minusDays(1);
            } else break;
        }
        return racha;
    }
}