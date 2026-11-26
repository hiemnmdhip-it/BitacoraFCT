package com.bitacorafct.bitacorafct.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitacorafct.bitacorafct.domain.CierreSemanal;

public interface CierreSemanalRepository extends JpaRepository<CierreSemanal, Long> {
	
	boolean existsByLunes(LocalDate lunes);

}
