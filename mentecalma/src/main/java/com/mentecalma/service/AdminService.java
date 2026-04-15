package com.mentecalma.service;

import com.mentecalma.engine.KnowledgeBase;
import com.mentecalma.model.Recomendacion;
import com.mentecalma.model.Regla;
import com.mentecalma.repository.RecomendacionRepository;
import com.mentecalma.repository.ReglaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ReglaRepository reglaRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final KnowledgeBase knowledgeBase;

    // ── Reglas ────────────────────────────────────────────
    public List<Regla> listarReglas() {
        return reglaRepository.findAllByOrderByPrioridadDesc();
    }

    public Regla crearRegla(Regla regla) {
        Regla saved = reglaRepository.save(regla);
        knowledgeBase.invalidarCache();
        return saved;
    }

    @Transactional
    public Regla actualizarRegla(Long id, Regla datos) {
        Regla regla = reglaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Regla no encontrada: " + id));
        regla.setNombre(datos.getNombre());
        regla.setCondicionesJson(datos.getCondicionesJson());
        regla.setPrioridad(datos.getPrioridad());
        regla.setActiva(datos.isActiva());
        regla.setVersion(regla.getVersion() + 1);
        Regla saved = reglaRepository.save(regla);
        knowledgeBase.invalidarCache();
        return saved;
    }

    public void toggleRegla(Long id, boolean activa) {
        Regla regla = reglaRepository.findById(id).orElseThrow();
        regla.setActiva(activa);
        reglaRepository.save(regla);
        knowledgeBase.invalidarCache();
    }

    public void eliminarRegla(Long id) {
        reglaRepository.deleteById(id);
        knowledgeBase.invalidarCache();
    }

    // ── Recomendaciones ───────────────────────────────────
    public List<Recomendacion> listarRecomendaciones() {
        return recomendacionRepository.findAll();
    }

    public Recomendacion crearRecomendacion(Recomendacion rec) {
        return recomendacionRepository.save(rec);
    }

    @Transactional
    public Recomendacion actualizarRecomendacion(Long id, Recomendacion datos) {
        Recomendacion rec = recomendacionRepository.findById(id).orElseThrow();
        rec.setTitulo(datos.getTitulo());
        rec.setTipo(datos.getTipo());
        rec.setDescripcion(datos.getDescripcion());
        rec.setDuracionMin(datos.getDuracionMin());
        rec.setPrioridad(datos.getPrioridad());
        rec.setContenidoJson(datos.getContenidoJson());
        rec.setActiva(datos.isActiva());
        return recomendacionRepository.save(rec);
    }
}