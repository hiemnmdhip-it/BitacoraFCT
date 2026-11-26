package com.bitacorafct.bitacorafct.exception;

public class JornadaCerradaException extends CuadernoException {
	private final Long jornadaId;
	
	public JornadaCerradaException(Long jornadaId) {
		super("La jornada " + jornadaId + " pertenece a una semana y no admite más actividades");
		this.jornadaId = jornadaId;
	}
	public Long getJornadaId() {
		return jornadaId;
	}
}
