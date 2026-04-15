package com.mentecalma.engine;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.mentecalma.model.Regla;
import com.mentecalma.repository.ReglaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KnowledgeBase {

    private final ReglaRepository reglaRepository;
    private final ObjectMapper objectMapper;

    @Cacheable("reglas-activas")
    public List<ReglaConCondiciones> cargarReglasActivas() {
        log.info("Cargando base de conocimiento desde BD...");
        List<Regla> reglas = reglaRepository.findAllByActivaTrue();
        log.info("{} reglas activas encontradas.", reglas.size());
        return reglas.stream().map(this::parsearRegla).toList();
    }

    @CacheEvict(value = "reglas-activas", allEntries = true)
    public void invalidarCache() {
        log.info("Caché de reglas invalidada.");
    }

    private ReglaConCondiciones parsearRegla(Regla regla) {
        try {
            List<Condicion> condiciones = objectMapper.readValue(
                    regla.getCondicionesJson(),
                    new TypeReference<List<Condicion>>() {}
            );
            return new ReglaConCondiciones(
                    regla.getId(), regla.getNombre(),
                    condiciones, regla.getRecomendacion(), regla.getPrioridad()
            );
        } catch (Exception e) {
            log.error("Error parseando regla id={}: {}", regla.getId(), e.getMessage());
            return new ReglaConCondiciones(
                    regla.getId(), regla.getNombre(),
                    List.of(), regla.getRecomendacion(), 0
            );
        }
    }

    public record ReglaConCondiciones(
            Long id,
            String nombre,
            List<Condicion> condiciones,
            com.mentecalma.model.Recomendacion recomendacion,
            Integer prioridad
    ) {}
}