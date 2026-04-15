package com.mentecalma.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "habito_logs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HabitoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkin_id", nullable = false)
    private CheckIn checkIn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recomendacion_id", nullable = false)
    private Recomendacion recomendacion;

    @Column(nullable = false)
    @Builder.Default
    private boolean completado = false;

    private Integer efectividad;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fecha = LocalDateTime.now();
}