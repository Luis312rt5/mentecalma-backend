package com.mentecalma.repository;
import com.mentecalma.model.Recomendacion;
import com.mentecalma.model.enums.TipoRecomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    List<Recomendacion> findAllByActivaTrue();
    List<Recomendacion> findAllByTipoAndActivaTrue(TipoRecomendacion tipo);
}