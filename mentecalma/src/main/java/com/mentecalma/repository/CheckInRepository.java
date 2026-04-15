package com.mentecalma.repository;
import com.mentecalma.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    List<CheckIn> findAllByUsuarioIdOrderByFechaDesc(Long usuarioId);

    Optional<CheckIn> findTopByUsuarioIdOrderByFechaDesc(Long usuarioId);

    boolean existsByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);

    @Query("SELECT AVG(c.nivelEstres) FROM CheckIn c WHERE c.usuario.id = :usuarioId AND c.fecha >= :desde")
    Double promedioEstresDesde(@Param("usuarioId") Long usuarioId, @Param("desde") LocalDate desde);

    @Query("SELECT c FROM CheckIn c WHERE c.usuario.id = :usuarioId ORDER BY c.fecha DESC LIMIT 30")
    List<CheckIn> findUltimos30(@Param("usuarioId") Long usuarioId);
}