package com.mentecalma.dto.request;

import com.mentecalma.model.enums.Situacion;
import jakarta.validation.constraints.*;
import java.util.List;

public record CheckInRequest(
        @NotNull @Min(1) @Max(10) Integer nivelEstres,
        @NotNull Situacion situacion,
        @NotNull @DecimalMin("0.0") @DecimalMax("24.0") Double horasSueno,
        List<String> sintomas,
        String notasLibres
) {}