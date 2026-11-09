package com.bitacorafct.bitacorafct.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitacorafct.bitacorafct.domain.Actividad;
import com.bitacorafct.bitacorafct.domain.Jornada;
import com.bitacorafct.bitacorafct.dto.JornadaResumenDTO;
import com.bitacorafct.bitacorafct.exception.JornadaDuplicadaException;
import com.bitacorafct.bitacorafct.exception.JornadaNoEncontradaException;
import com.bitacorafct.bitacorafct.exception.LimiteHorasExcedidoException;
import com.bitacorafct.bitacorafct.repository.ActividadRepository;
import com.bitacorafct.bitacorafct.repository.JornadaRepository;

@Service
public class JornadaService {

    public static final double MAX_HORAS_DIARIAS = 8.0;

    private final JornadaRepository jornadaRepository;
    private final ActividadRepository actividadRepository;

    public JornadaService(JornadaRepository jornadaRepository,
                          ActividadRepository actividadRepository) {
        this.jornadaRepository = jornadaRepository;
        this.actividadRepository = actividadRepository;
    }

    @Transactional
    public Long crearJornada(LocalDate fecha, String reflexion) {
        if (jornadaRepository.existsByFecha(fecha)) {
            throw new JornadaDuplicadaException(fecha);
        }
        Jornada jornada = jornadaRepository.save(new Jornada(fecha, reflexion));
        return jornadaRepository.save(jornada).getId();
    }

    @Transactional
    public void anadirActividad(Long jornadaId, String descripcion, double horas) {
        Jornada jornada = jornadaRepository.findById(jornadaId)
                .orElseThrow(() -> new JornadaNoEncontradaException(jornadaId));

        double horasRegistradas = actividadRepository.sumarHorasDeJornada(jornadaId);

        if (horasRegistradas + horas > MAX_HORAS_DIARIAS) {
            throw new LimiteHorasExcedidoException(jornada.getFecha(), horasRegistradas, horas);
        }

        actividadRepository.save(new Actividad(jornada, descripcion, horas));
    }

   /* @Transactional
    public void añadirActividad(Long jornadaId, String descripcion, double horas) {
    	if(!jornadaRepository.existsById(jornadaId)) {
			throw new JornadaNoEncontradaException(jornadaId);
		}
    	double horasRegistradas = actividadRepository.sumarHorasDeJornada(jornadaId);
    	Jornada jornada = jornadaRepository.findById(jornadaId).get();
    	if (horasRegistradas + horas > MAX_HORAS_DIARIAS) {
    		throw new LimiteHorasExcedidoException(jornada.getFecha(), horasRegistradas, horas);
    	}
    	Actividad actividad = new Actividad(jornada, descripcion, horas);
    	actividadRepository.save(actividad); 
    }*/
    
    
    @Transactional(readOnly = true)
    public List<JornadaResumenDTO> listarJornadas() {
        return jornadaRepository.findAllByOrderByFechaDesc().stream()
                .map(j -> new JornadaResumenDTO(
                        j.getId(),
                        j.getFecha(),
                        actividadRepository.sumarHorasDeJornada(j.getId()),
                        actividadRepository.countByJornadaId(j.getId()),
                        j.tieneReflexion()))
                .toList();
    }
}