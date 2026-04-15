package com.mentecalma.repository;
import com.mentecalma.model.HabitoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitoLogRepository extends JpaRepository<HabitoLog, Long> {

    List<HabitoLog> findAllByCheckInId(Long checkInId);

    @Query("SELECT h FROM HabitoLog h WHERE h.checkIn.usuario.id = :usuarioId ORDER BY h.fecha DESC")
    List<HabitoLog> findAllByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT AVG(h.efectividad) FROM HabitoLog h WHERE h.checkIn.usuario.id = :usuarioId AND h.recomendacion.id = :recomendacionId AND h.efectividad IS NOT NULL")
    Double efectividadPromedio(@Param("usuarioId") Long usuarioId, @Param("recomendacionId") Long recomendacionId);

    @Query("SELECT COUNT(h) FROM HabitoLog h WHERE h.checkIn.usuario.id = :usuarioId AND h.completado = true")
    Long countCompletadosByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT h.recomendacion.id, AVG(h.efectividad) as avg FROM HabitoLog h WHERE h.checkIn.usuario.id = :usuarioId AND h.efectividad IS NOT NULL GROUP BY h.recomendacion.id ORDER BY avg DESC LIMIT 1")
    List<Object[]> topRecomendacionEfectiva(@Param("usuarioId") Long usuarioId);
}