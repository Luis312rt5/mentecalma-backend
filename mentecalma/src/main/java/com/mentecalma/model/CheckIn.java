package com.mentecalma.model;

import com.mentecalma.model.enums.Situacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "checkins")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate fecha = LocalDate.now();

    @Column(nullable = false)
    private Integer nivelEstres;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Situacion situacion;

    @Column(nullable = false)
    private Double horasSueno;

    @Column(columnDefinition = "TEXT")
    private String sintomasJson;

    @Column(columnDefinition = "TEXT")
    private String notasLibres;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}