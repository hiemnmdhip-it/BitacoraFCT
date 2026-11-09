package com.bitacorafct.bitacorafct.dto;

import java.time.LocalDate;

public record JornadaResumenDTO(
        Long id,
        LocalDate fecha,
        double totalHoras,
        long numActividades,
        boolean tieneReflexion) {
}