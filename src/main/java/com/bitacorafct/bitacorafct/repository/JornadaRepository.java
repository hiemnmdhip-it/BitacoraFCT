package com.bitacorafct.bitacorafct.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitacorafct.bitacorafct.domain.Jornada;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {

    boolean existsByFecha(LocalDate fecha);

    List<Jornada> findAllByOrderByFechaDesc();
}