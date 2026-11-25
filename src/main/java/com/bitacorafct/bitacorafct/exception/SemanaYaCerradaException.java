package com.bitacorafct.bitacorafct.exception;

import java.time.LocalDate;

public class SemanaYaCerradaException extends CuadernoException {
		private LocalDate lunes;
		
		public SemanaYaCerradaException(LocalDate lunes) {
			super("La semana ya está cerrada");
			this.lunes = lunes;
		}
		public LocalDate getLunes() {
			return lunes;
		}
}
