package com.bitacorafct.bitacorafct.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LimiteHorasExcedidoException extends CuadernoException {
    public LimiteHorasExcedidoException(LocalDate fecha, double horasRegistradas, double horasNuevas) {
        super(String.format(
            "No se puede añadir la actividad: el %s ya tiene %.1f h registradas y con %.1f h más "
            + "superarías el máximo de 8 h diarias.",
            fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), horasRegistradas, horasNuevas));
    }
}
