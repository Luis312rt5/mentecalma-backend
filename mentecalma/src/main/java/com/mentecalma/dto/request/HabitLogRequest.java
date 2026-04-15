package com.mentecalma.dto.request;

import jakarta.validation.constraints.*;

public record HabitLogRequest(
        @NotNull Long checkInId,
        @NotNull Long recomendacionId,
        @NotNull Boolean completado,
        @Min(1) @Max(5) Integer efectividad
) {}