package com.bitacorafct.bitacorafct.exception;

public class JornadaNoEncontradaException extends CuadernoException {
	private Long id;
	
    public JornadaNoEncontradaException(Long id) {
        super("No existe ninguna jornada con el identificador " + id + ".");
        this.id = id;
    }
    
    public Long getId() {
    	return id;
    }
}
