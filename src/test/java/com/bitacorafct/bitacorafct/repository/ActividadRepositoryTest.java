package com.bitacorafct.bitacorafct.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.bitacorafct.bitacorafct.domain.Actividad;
import com.bitacorafct.bitacorafct.domain.Jornada;

@DataJpaTest
class ActividadRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ActividadRepository actividadRepository;

    @Test
    void sumarHorasDeJornada_conVariasActividades_devuelveLaSuma() {
        Jornada jornada = em.persist(new Jornada(LocalDate.of(2026, 10, 5), "Reflexión"));
        em.persist(new Actividad(jornada, "Presentación", 3.0));
        em.persist(new Actividad(jornada, "Configuración", 1.5));
        em.persist(new Actividad(jornada, "Documentación", 3.5));
        em.flush();

        double suma = actividadRepository.sumarHorasDeJornada(jornada.getId());

        assertEquals(8.0, suma, 0.0001);
    }

    @Test
    void sumarHorasDeJornada_sinActividades_devuelveCero() {
        Jornada jornada = em.persistAndFlush(new Jornada(LocalDate.of(2026, 10, 6), null));

        double suma = actividadRepository.sumarHorasDeJornada(jornada.getId());

        assertEquals(0.0, suma, 0.0001);
    }

    @Test
    void sumarHorasDeJornada_soloCuentaLasActividadesDeEsaJornada() {
        Jornada j1 = em.persist(new Jornada(LocalDate.of(2026, 10, 5), null));
        Jornada j2 = em.persist(new Jornada(LocalDate.of(2026, 10, 6), null));
        em.persist(new Actividad(j1, "Tarea A", 2.0));
        em.persist(new Actividad(j2, "Tarea B", 5.0));
        em.flush();

        assertEquals(2.0, actividadRepository.sumarHorasDeJornada(j1.getId()), 0.0001);
        assertEquals(1L, actividadRepository.countByJornadaId(j1.getId()));
    }
}
