package com.bitacorafct.bitacorafct.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cierres_semanales")
@Getter
@NoArgsConstructor
public class CierreSemanal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "lunes", nullable = false, unique = true)
    private LocalDate lunes;
	
	@Column(name = "total_horas")
	private LocalDateTime totalHoras;
	
	@Column(name = "fecha_cierre")
	private LocalDateTime fechaCierre;

}
