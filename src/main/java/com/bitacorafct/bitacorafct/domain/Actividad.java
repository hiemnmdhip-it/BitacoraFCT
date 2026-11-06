package com.bitacorafct.bitacorafct.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "actividades")
public class Actividad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jornada_id", nullable = false)
	private Jornada jornada;
	
	@Column(name = "descripcion", length = 255, nullable = false)
	private String descripcion;
	
	@Column(name = "horas", nullable = false)
	private Double horas;
	
	public void Horas(Double horas) {
		if (horas <0)
			throw new IllegalArgumentException("Las horas no pueden ser negativas");
		this.horas = horas;
	}
	

}
