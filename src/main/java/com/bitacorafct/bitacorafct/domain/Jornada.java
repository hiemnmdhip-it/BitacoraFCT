package com.bitacorafct.bitacorafct.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "jornadas")
@Getter
/*@NoArgsConstructor
@AllArgsConstructor*/
public class Jornada {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "fecha", nullable = false, unique = true)
	private LocalDate fecha;
	
	@Column(name = "reflexion", columnDefinition = "TEXT", nullable = true)
	private String reflexion;
	
}
