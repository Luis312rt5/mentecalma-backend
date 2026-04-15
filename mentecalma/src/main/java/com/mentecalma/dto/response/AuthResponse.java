package com.mentecalma.dto.response;

public record AuthResponse(
        String token,
        String email,
        String nombre,
        String rol
) {}