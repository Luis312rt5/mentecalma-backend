package com.mentecalma.dto.response;

public record UserStatsResponse(
        Integer totalCheckins,
        Double promedioEstresUltimos7Dias,
        Double promedioEstresUltimos30Dias,
        Integer habitosCompletados,
        String recomendacionMasEfectiva,
        Double efectividadPromedio,
        Integer rachaActualDias
) {}