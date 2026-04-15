package com.mentecalma.engine;
import com.mentecalma.model.enums.Situacion;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserSession {
    private Integer nivelEstres;
    private Situacion situacion;
    private Double horasSueno;
    @Builder.Default
    private List<String> sintomas = List.of();
    private Long checkInId;
    private Long usuarioId;
}