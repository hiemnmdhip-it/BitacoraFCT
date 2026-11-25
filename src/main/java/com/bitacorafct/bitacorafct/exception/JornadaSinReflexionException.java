package com.bitacorafct.bitacorafct.exception;

public class JornadaSinReflexionException extends CuadernoException {
	private String reflexion;
	
	public JornadaSinReflexionException(String reflexion) {
		super("La jornada no tiene reflexion");
		this.reflexion = reflexion;
	}
	public String getReflexion() {
		return reflexion;
	}
}
