package com.bitacorafct.bitacorafct.exception;

public abstract class CuadernoException extends RuntimeException {
    protected CuadernoException(String mensaje) {
        super(mensaje);
    }
}
