package com.bitacorafct.bitacorafct.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitacorafct.bitacorafct.domain.CierreSemanal;

public interface cierreSemanalRepository extends JpaRepository<CierreSemanal, Long> {
	
	boolean existisByLunes(LocalDate lunes);

}
