package com.mentecalma.engine;

import com.mentecalma.model.Recomendacion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@Slf4j
public class ResultRanker {

    private static final int MAX_RECOMENDACIONES = 3;

    public List<Recomendacion> rankear(List<InferenceEngine.ReglaActivada> reglasActivadas) {
        List<InferenceEngine.ReglaActivada> ordenadas = reglasActivadas.stream()
                .sorted(Comparator.comparingInt(InferenceEngine.ReglaActivada::prioridad).reversed())
                .toList();

        Set<Long> idsVistos = new LinkedHashSet<>();
        List<Recomendacion> resultado = new ArrayList<>();

        for (InferenceEngine.ReglaActivada regla : ordenadas) {
            Long recId = regla.recomendacion().getId();
            if (idsVistos.add(recId)) {
                resultado.add(regla.recomendacion());
                log.debug("Seleccionada: '{}' prioridad={}",
                        regla.recomendacion().getTitulo(), regla.prioridad());
            }
            if (resultado.size() == MAX_RECOMENDACIONES) break;
        }

        return resultado;
    }
}