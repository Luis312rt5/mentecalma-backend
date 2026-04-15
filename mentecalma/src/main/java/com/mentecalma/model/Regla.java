package com.mentecalma.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reglas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Regla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String condicionesJson;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recomendacion_id", nullable = false)
    private Recomendacion recomendacion;

    @Column(nullable = false)
    @Builder.Default
    private Integer prioridad = 5;

    @Column(nullable = false)
    @Builder.Default
    private boolean activa = true;

    @Column(nullable = false)
    @Builder.Default
    private Integer version = 1;
}