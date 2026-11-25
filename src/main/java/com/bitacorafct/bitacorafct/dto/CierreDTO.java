package com.bitacorafct.bitacorafct.dto;

import java.time.LocalDate;

public record CierreDTO(
		LocalDate lunes,
		double totalHoras,
		int numJornadas
		
		) {

}
