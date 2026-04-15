package com.mentecalma.dto.response;

import com.mentecalma.model.enums.TipoRecomendacion;

public record RecommendationResponse(
        Long id,
        String titulo,
        TipoRecomendacion tipo,
        String descripcion,
        Integer duracionMin,
        String contenidoJson,
        String reglaActivada
) {}