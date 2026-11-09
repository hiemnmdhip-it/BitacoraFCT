package com.bitacorafct.bitacorafct.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JornadaDuplicadaException extends CuadernoException {
	private LocalDate fecha;
	
    public JornadaDuplicadaException(LocalDate fecha) {
        super("Ya existe una jornada registrada el "
              + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
        this.fecha = fecha;
    }
    
    public LocalDate getFecha() {
		return fecha;
	}
}