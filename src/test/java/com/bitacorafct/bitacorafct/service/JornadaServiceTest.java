package com.bitacorafct.bitacorafct.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bitacorafct.bitacorafct.domain.Actividad;
import com.bitacorafct.bitacorafct.domain.Jornada;
import com.bitacorafct.bitacorafct.exception.JornadaDuplicadaException;
import com.bitacorafct.bitacorafct.exception.JornadaNoEncontradaException;
import com.bitacorafct.bitacorafct.exception.LimiteHorasExcedidoException;
import com.bitacorafct.bitacorafct.repository.ActividadRepository;
import com.bitacorafct.bitacorafct.repository.JornadaRepository;

@ExtendWith(MockitoExtension.class)
class JornadaServiceTest {

    @Mock
    private JornadaRepository jornadaRepository;

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private JornadaService jornadaService;

    private final LocalDate fecha = LocalDate.of(2026, 10, 5);

    @Test
    void crearJornada_conFechaDuplicada_lanzaExcepcionYNoGuarda() {
        when(jornadaRepository.existsByFecha(fecha)).thenReturn(true);

        assertThrows(JornadaDuplicadaException.class,
                () -> jornadaService.crearJornada(fecha, "Reflexión"));

        verify(jornadaRepository, never()).save(any(Jornada.class));
    }

    @Test
    void anadirActividad_conJornadaInexistente_lanzaExcepcionYNoGuarda() {
        when(jornadaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(JornadaNoEncontradaException.class,
                () -> jornadaService.anadirActividad(99L, "Tarea", 2.0));

        verify(actividadRepository, never()).save(any(Actividad.class));
    }

    @Test
    void anadirActividad_siSuperaElLimite_lanzaExcepcionYNoGuarda() {
        Jornada jornada = new Jornada(fecha, null);
        when(jornadaRepository.findById(1L)).thenReturn(Optional.of(jornada));
        when(actividadRepository.sumarHorasDeJornada(1L)).thenReturn(6.0);

        LimiteHorasExcedidoException ex = assertThrows(LimiteHorasExcedidoException.class,
                () -> jornadaService.anadirActividad(1L, "Tarea", 3.0));

        assertTrue(ex.getMessage().contains("8 h"));
        verify(actividadRepository, never()).save(any(Actividad.class));
    }

    @Test
    void anadirActividad_siLlegaJustoAlLimite_seGuarda() {
        Jornada jornada = new Jornada(fecha, null);
        when(jornadaRepository.findById(1L)).thenReturn(Optional.of(jornada));
        when(actividadRepository.sumarHorasDeJornada(1L)).thenReturn(6.0);

        assertDoesNotThrow(() -> jornadaService.anadirActividad(1L, "Tarea", 2.0));

        verify(actividadRepository).save(any(Actividad.class));
    }

    @Test
    void anadirActividad_valida_guardaLaActividadConLosDatosEsperados() {
        Jornada jornada = new Jornada(fecha, null);
        when(jornadaRepository.findById(1L)).thenReturn(Optional.of(jornada));
        when(actividadRepository.sumarHorasDeJornada(1L)).thenReturn(3.0);

        jornadaService.anadirActividad(1L, "Escritura de tests", 4.0);

        ArgumentCaptor<Actividad> captor = ArgumentCaptor.forClass(Actividad.class);
        verify(actividadRepository).save(captor.capture());

        Actividad guardada = captor.getValue();
        assertEquals("Escritura de tests", guardada.getDescripcion());
        assertEquals(4.0, guardada.getHoras(), 0.0001);
        assertSame(jornada, guardada.getJornada());
    }
}
