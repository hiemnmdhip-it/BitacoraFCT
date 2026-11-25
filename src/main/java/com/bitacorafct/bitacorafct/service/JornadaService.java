package com.bitacorafct.bitacorafct.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitacorafct.bitacorafct.domain.Actividad;
import com.bitacorafct.bitacorafct.domain.CierreSemanal;
import com.bitacorafct.bitacorafct.domain.Jornada;
import com.bitacorafct.bitacorafct.dto.CierreDTO;
import com.bitacorafct.bitacorafct.dto.JornadaResumenDTO;
import com.bitacorafct.bitacorafct.exception.JornadaDuplicadaException;
import com.bitacorafct.bitacorafct.exception.JornadaNoEncontradaException;
import com.bitacorafct.bitacorafct.exception.LimiteHorasExcedidoException;
import com.bitacorafct.bitacorafct.exception.SemanaVaciaException;
import com.bitacorafct.bitacorafct.exception.SemanaYaCerradaException;
import com.bitacorafct.bitacorafct.repository.ActividadRepository;
import com.bitacorafct.bitacorafct.repository.JornadaRepository;
import com.bitacorafct.bitacorafct.repository.cierreSemanalRepository;

@Service
public class JornadaService {

    public static final double MAX_HORAS_DIARIAS = 8.0;

    private final JornadaRepository jornadaRepository;
    private final ActividadRepository actividadRepository;
    private final cierreSemanalRepository cierreSemanalRepository;

    public JornadaService(JornadaRepository jornadaRepository,
                          ActividadRepository actividadRepository,
                          cierreSemanalRepository cierreSemanalRepository) {
        this.jornadaRepository = jornadaRepository;
        this.actividadRepository = actividadRepository;
        this.cierreSemanalRepository = cierreSemanalRepository;
    }

    @Transactional
    public Long crearJornada(LocalDate fecha, String reflexion) {
        if (jornadaRepository.existsByFecha(fecha)) {
            throw new JornadaDuplicadaException(fecha);
        }
        return jornadaRepository.save(new Jornada(fecha, reflexion)).getId();
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
    public void anadirActividad(Long jornadaId, String descripcion, double horas) {
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
    
    public CierreDTO cerrarSemana(LocalDate diaDeLaSemana) {
    	
		LocalDate lunes = diaDeLaSemana.with(DayOfWeek.MONDAY);
		LocalDate domingo = diaDeLaSemana.with(DayOfWeek.SUNDAY);
		
		if(cierreSemanalRepository.existisByLunes(lunes)) {
			throw new SemanaYaCerradaException(lunes);
		}
		
	/*	List<Jornada> jornadas = jornadaRepository.findByFechaBetweenOrderByFechaAsc(lunes, domingo);
			if (jornadas.isEmpty()) {
				throw new SemanaVaciaException(lunes);
			}
			double totalHoras = actividadRepository.sumarHorasEntre(lunes, domingo);
		    
		    CierreSemanal cierre = new CierreSemanal(lunes, totalHoras, LocalDateTime.now());
		    cierre = cierreSemanalRepository.save(cierre); 

		    
		    for (Jornada jornada : jornadas) {
		        jornada.asignarCierre(cierre); 
		       
		    }

		
		    return new CierreDTO(lunes, totalHoras, jornadas.size());*/
		}

		

    }
    
