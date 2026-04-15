package com.mentecalma.controller;

import com.mentecalma.model.Recomendacion;
import com.mentecalma.model.Regla;
import com.mentecalma.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/rules")
    public ResponseEntity<List<Regla>> listarReglas() {
        return ResponseEntity.ok(adminService.listarReglas());
    }

    @PostMapping("/rules")
    public ResponseEntity<Regla> crearRegla(@RequestBody Regla regla) {
        return ResponseEntity.ok(adminService.crearRegla(regla));
    }

    @PutMapping("/rules/{id}")
    public ResponseEntity<Regla> actualizarRegla(@PathVariable Long id, @RequestBody Regla regla) {
        return ResponseEntity.ok(adminService.actualizarRegla(id, regla));
    }

    @PatchMapping("/rules/{id}/toggle")
    public ResponseEntity<Void> toggleRegla(@PathVariable Long id, @RequestParam boolean activa) {
        adminService.toggleRegla(id, activa);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<Void> eliminarRegla(@PathVariable Long id) {
        adminService.eliminarRegla(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Recomendacion>> listarRecomendaciones() {
        return ResponseEntity.ok(adminService.listarRecomendaciones());
    }

    @PostMapping("/recommendations")
    public ResponseEntity<Recomendacion> crearRecomendacion(@RequestBody Recomendacion rec) {
        return ResponseEntity.ok(adminService.crearRecomendacion(rec));
    }

    @PutMapping("/recommendations/{id}")
    public ResponseEntity<Recomendacion> actualizarRecomendacion(
            @PathVariable Long id, @RequestBody Recomendacion rec) {
        return ResponseEntity.ok(adminService.actualizarRecomendacion(id, rec));
    }
}