package com.mentecalma.model;

import com.mentecalma.model.enums.TipoRecomendacion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recomendaciones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRecomendacion tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private Integer duracionMin;

    @Column(nullable = false)
    @Builder.Default
    private Integer prioridad = 5;

    @Column(columnDefinition = "TEXT")
    private String contenidoJson;

    @Column(nullable = false)
    @Builder.Default
    private boolean activa = true;
}