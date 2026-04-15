package com.mentecalma.engine;

import com.mentecalma.model.enums.OperadorCondicion;
import com.mentecalma.model.enums.Situacion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
public class RuleEvaluator {

    public boolean evaluar(Condicion condicion, UserSession session) {
        try {
            return switch (condicion.getCampo()) {
                case "nivelEstres" -> evaluarNumerico(condicion, session.getNivelEstres());
                case "horasSueno"  -> evaluarNumerico(condicion, session.getHorasSueno());
                case "situacion"   -> evaluarSituacion(condicion, session.getSituacion());
                case "sintomas"    -> evaluarSintomas(condicion, session.getSintomas());
                default -> {
                    log.warn("Campo desconocido: '{}'", condicion.getCampo());
                    yield false;
                }
            };
        } catch (Exception e) {
            log.error("Error evaluando condición: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluarNumerico(Condicion cond, Number hechoRaw) {
        if (hechoRaw == null) return false;
        double hecho = hechoRaw.doubleValue();
        double umbral = Double.parseDouble(cond.getValor());
        return switch (cond.getOperador()) {
            case GREATER_THAN     -> hecho >  umbral;
            case GREATER_OR_EQUAL -> hecho >= umbral;
            case LESS_THAN        -> hecho <  umbral;
            case LESS_OR_EQUAL    -> hecho <= umbral;
            case EQUALS           -> hecho == umbral;
            case NOT_EQUALS       -> hecho != umbral;
            default -> false;
        };
    }

    private boolean evaluarSituacion(Condicion cond, Situacion hecho) {
        if (hecho == null) return false;
        Situacion umbral = Situacion.valueOf(cond.getValor());
        return switch (cond.getOperador()) {
            case EQUALS     -> hecho == umbral;
            case NOT_EQUALS -> hecho != umbral;
            default -> false;
        };
    }

    private boolean evaluarSintomas(Condicion cond, List<String> sintomas) {
        if (sintomas == null || sintomas.isEmpty()) return false;
        String valorBuscado = cond.getValor().toLowerCase();
        return switch (cond.getOperador()) {
            case CONTAINS   -> sintomas.stream().anyMatch(s -> s.toLowerCase().contains(valorBuscado));
            case NOT_EQUALS -> sintomas.stream().noneMatch(s -> s.toLowerCase().contains(valorBuscado));
            default -> false;
        };
    }
}