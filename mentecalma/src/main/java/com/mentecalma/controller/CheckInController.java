package com.mentecalma.controller;

import com.mentecalma.dto.request.CheckInRequest;
import com.mentecalma.dto.request.HabitLogRequest;
import com.mentecalma.dto.response.CheckInResponse;
import com.mentecalma.dto.response.UserStatsResponse;
import com.mentecalma.service.DiagnosticService;
import com.mentecalma.service.TrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CheckInController {

    private final DiagnosticService diagnosticService;
    private final TrackingService trackingService;

    @PostMapping("/checkin")
    public ResponseEntity<CheckInResponse> realizarCheckIn(
            @Valid @RequestBody CheckInRequest req,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(diagnosticService.procesarCheckIn(req, user.getUsername()));
    }

    @GetMapping("/checkin/history")
    public ResponseEntity<List<CheckInResponse>> historial(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(diagnosticService.obtenerHistorial(user.getUsername()));
    }

    @PostMapping("/habits/log")
    public ResponseEntity<Void> logHabito(
            @Valid @RequestBody HabitLogRequest req,
            @AuthenticationPrincipal UserDetails user) {
        trackingService.registrarHabito(req, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkin/{id}/recommendations")
    public ResponseEntity<CheckInResponse> obtenerRecomendacionesPorCheckIn(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(diagnosticService.obtenerCheckInConRecomendaciones(id, user.getUsername()));
    }

    @GetMapping("/stats/me")
    public ResponseEntity<UserStatsResponse> miEstadistica(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(trackingService.obtenerEstadisticas(user.getUsername()));
    }
}