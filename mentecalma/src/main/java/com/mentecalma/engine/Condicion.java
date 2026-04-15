package com.mentecalma.engine;
import com.mentecalma.model.enums.OperadorCondicion;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Condicion {
    private String campo;
    private OperadorCondicion operador;
    private String valor;
}