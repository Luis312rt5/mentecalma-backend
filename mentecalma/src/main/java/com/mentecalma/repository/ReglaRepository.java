package com.mentecalma.repository;
import com.mentecalma.model.Regla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReglaRepository extends JpaRepository<Regla, Long> {
    List<Regla> findAllByActivaTrue();
    List<Regla> findAllByOrderByPrioridadDesc();
}