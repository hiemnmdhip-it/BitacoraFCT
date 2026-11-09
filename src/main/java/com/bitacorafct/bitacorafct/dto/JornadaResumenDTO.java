package com.bitacorafct.bitacorafct.dto;

import java.time.LocalDate;

public record JornadaResumenDTO(
        Long id,
        LocalDate fecha,
        double totalHoras,
        int numActividades,
        boolean tieneReflexion) {
}