package com.bitacorafct.bitacorafct.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bitacorafct.bitacorafct.domain.Jornada;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {

	@Query("""
			Select j From Jornada j
			Where j.jornada.id = :Id and j.fecha = :fecha
			order by j.fecha desc
			""")
	List<Jornada> existsByJornadaAndFecha(Long id, LocalDate fecha);
	
}
