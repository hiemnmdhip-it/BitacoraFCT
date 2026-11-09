package com.bitacorafct.bitacorafct.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class LimiteHorasExcedidoException extends CuadernoException {
	private double horasRegistradas;
	private double horasNuevas;
	private LocalDate fecha;
	
    public LimiteHorasExcedidoException(LocalDate fecha, double horasRegistradas, double horasNuevas) {
        super(String.format(
            "No se puede añadir la actividad: el %s ya tiene %.1f h registradas y con %.1f h más "
            + "superarías el máximo de 8 h diarias.",
            fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), horasRegistradas, horasNuevas));
        this.horasRegistradas = horasRegistradas;
        this.horasNuevas = horasNuevas;
        this.fecha = fecha;
    }
}
