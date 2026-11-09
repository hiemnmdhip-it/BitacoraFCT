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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.bitacorafct.bitacorafct.domain.Actividad;
import com.bitacorafct.bitacorafct.domain.Jornada;
import com.bitacorafct.bitacorafct.dto.JornadaResumenDTO;
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

    // ---------- crearJornada ----------

    @Test
    void crearJornada_conFechaDuplicada_lanzaExcepcionYNoGuarda() {
        when(jornadaRepository.existsByFecha(fecha)).thenReturn(true);

        JornadaDuplicadaException ex = assertThrows(JornadaDuplicadaException.class,
                () -> jornadaService.crearJornada(fecha, "Reflexión"));

        assertEquals(fecha, ex.getFecha());
        verify(jornadaRepository, never()).save(any(Jornada.class));
    }

    @Test
    void crearJornada_valida_guardaLaJornadaYDevuelveSuId() {
        when(jornadaRepository.save(any(Jornada.class))).thenAnswer(inv -> {
            Jornada j = inv.getArgument(0);
            ReflectionTestUtils.setField(j, "id", 10L);   // simula el ID que asigna la BD
            return j;
        });

        Long id = jornadaService.crearJornada(fecha, "Mi reflexión");

        ArgumentCaptor<Jornada> captor = ArgumentCaptor.forClass(Jornada.class);
        verify(jornadaRepository).save(captor.capture());   // exactamente un save

        assertEquals(10L, id);
        assertEquals(fecha, captor.getValue().getFecha());
        assertEquals("Mi reflexión", captor.getValue().getReflexion());
    }

    // ---------- anadirActividad ----------

    @Test
    void anadirActividad_conJornadaInexistente_lanzaExcepcionYNoGuarda() {
        when(jornadaRepository.findById(99L)).thenReturn(Optional.empty());

        JornadaNoEncontradaException ex = assertThrows(JornadaNoEncontradaException.class,
                () -> jornadaService.anadirActividad(99L, "Tarea", 2.0));

        assertEquals(99L, ex.getId());
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
        assertEquals(fecha, ex.getFecha());
        assertEquals(6.0, ex.getHorasRegistradas(), 0.0001);
        assertEquals(3.0, ex.getHorasNuevas(), 0.0001);
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

    // ---------- listarJornadas ----------

    @Test
    void listarJornadas_montaUnResumenPorJornadaEnElOrdenDelRepositorio() {
        Jornada reciente = jornadaConId(2L, LocalDate.of(2026, 10, 16), null);
        Jornada antigua = jornadaConId(1L, LocalDate.of(2026, 10, 5), "Reflexión");

        when(jornadaRepository.findAllByOrderByFechaDesc()).thenReturn(List.of(reciente, antigua));
        when(actividadRepository.sumarHorasDeJornada(2L)).thenReturn(6.0);
        when(actividadRepository.countByJornadaId(2L)).thenReturn(1);
        when(actividadRepository.sumarHorasDeJornada(1L)).thenReturn(8.0);
        when(actividadRepository.countByJornadaId(1L)).thenReturn(3);

        List<JornadaResumenDTO> resumen = jornadaService.listarJornadas();

        assertEquals(2, resumen.size());
        assertEquals(new JornadaResumenDTO(2L, LocalDate.of(2026, 10, 16), 6.0, 1, false), resumen.get(0));
        assertEquals(new JornadaResumenDTO(1L, LocalDate.of(2026, 10, 5), 8.0, 3, true), resumen.get(1));
    }

    private Jornada jornadaConId(Long id, LocalDate fecha, String reflexion) {
        Jornada j = new Jornada(fecha, reflexion);
        ReflectionTestUtils.setField(j, "id", id);
        return j;
    }
}