package com.mentecalma.dto.response;

import com.mentecalma.model.enums.Situacion;
import java.time.LocalDate;
import java.util.List;

public record CheckInResponse(
        Long checkInId,
        LocalDate fecha,
        Integer nivelEstres,
        Situacion situacion,
        Double horasSueno,
        String tipoResultado,
        String mensajeCritico,
        List<RecommendationResponse> recomendaciones
) {}