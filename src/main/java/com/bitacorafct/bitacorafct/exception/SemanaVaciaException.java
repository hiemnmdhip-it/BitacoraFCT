package com.bitacorafct.bitacorafct.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SemanaVaciaException extends CuadernoException {
	private final LocalDate lunes;
	
	public SemanaVaciaException(LocalDate lunes) {
		super("No hay jornadas registradas en la semana del " + 
				lunes.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
		this.lunes = lunes;
	}
	public LocalDate getLunes() {
		return lunes;
	}
}
