package com.mentecalma.engine;

import com.mentecalma.model.Recomendacion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InferenceEngine {

    private final KnowledgeBase knowledgeBase;
    private final RuleEvaluator ruleEvaluator;
    private final ResultRanker resultRanker;

    public ResultadoInferencia inferir(UserSession session) {
        log.debug("Iniciando inferencia usuario={}, nivelEstres={}",
                session.getUsuarioId(), session.getNivelEstres());

        if (session.getNivelEstres() != null && session.getNivelEstres() >= 9) {
            log.warn("Nivel crítico detectado: {}", session.getNivelEstres());
            return ResultadoInferencia.critico();
        }

        List<KnowledgeBase.ReglaConCondiciones> reglasActivas =
                knowledgeBase.cargarReglasActivas();

        List<ReglaActivada> reglasActivadas = new ArrayList<>();
        for (KnowledgeBase.ReglaConCondiciones regla : reglasActivas) {
            boolean todasCumplidas = regla.condiciones().stream()
                    .allMatch(cond -> ruleEvaluator.evaluar(cond, session));
            if (todasCumplidas) {
                log.debug("Regla activada: '{}' prioridad={}", regla.nombre(), regla.prioridad());
                reglasActivadas.add(new ReglaActivada(
                        regla.id(), regla.nombre(), regla.recomendacion(), regla.prioridad()
                ));
            }
        }

        if (reglasActivadas.isEmpty()) {
            log.info("Ninguna regla activada.");
            return ResultadoInferencia.sinCoincidencias();
        }

        List<Recomendacion> top = resultRanker.rankear(reglasActivadas);
        return ResultadoInferencia.exitoso(top, reglasActivadas);
    }

    public record ReglaActivada(
            Long reglaId,
            String nombreRegla,
            Recomendacion recomendacion,
            Integer prioridad
    ) {}

    public record ResultadoInferencia(
            TipoResultado tipo,
            List<Recomendacion> recomendaciones,
            List<ReglaActivada> reglasActivadas
    ) {
        public enum TipoResultado { EXITOSO, CRITICO, SIN_COINCIDENCIAS }

        public boolean esCritico() { return tipo == TipoResultado.CRITICO; }

        static ResultadoInferencia exitoso(List<Recomendacion> recs, List<ReglaActivada> reglas) {
            return new ResultadoInferencia(TipoResultado.EXITOSO, recs, reglas);
        }
        static ResultadoInferencia critico() {
            return new ResultadoInferencia(TipoResultado.CRITICO, List.of(), List.of());
        }
        static ResultadoInferencia sinCoincidencias() {
            return new ResultadoInferencia(TipoResultado.SIN_COINCIDENCIAS, List.of(), List.of());
        }
    }
}