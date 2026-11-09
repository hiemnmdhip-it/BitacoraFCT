package com.bitacorafct.bitacorafct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bitacorafct.bitacorafct.domain.Actividad;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    int countByJornadaId(Long jornadaId);

    @Query("""
    		SELECT COALESCE(SUM(a.horas), 0.0) 
    		FROM Actividad a
    		WHERE a.jornada.id = :jornadaId
    		""")
    double sumarHorasDeJornada(@Param("jornadaId") Long jornadaId);
}