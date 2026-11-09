package com.bitacorafct.bitacorafct.exception;

public class JornadaNoEncontradaException extends CuadernoException {
    public JornadaNoEncontradaException(Long id) {
        super("No existe ninguna jornada con el identificador " + id + ".");
    }
}
