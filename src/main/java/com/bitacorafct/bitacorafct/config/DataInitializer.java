package com.bitacorafct.bitacorafct.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bitacorafct.bitacorafct.service.JornadaService;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final JornadaService jornadaService;

    public DataInitializer(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @Override
    public void run(String... args) {
        // Lunes 05/10/2026 - 8 h
        Long lunes = jornadaService.crearJornada(LocalDate.of(2026, 10, 5),
                "Primer día en la empresa: me han presentado al equipo y el entorno de trabajo.");
        jornadaService.anadirActividad(lunes, "Presentación del equipo y de la empresa", 3);
        jornadaService.anadirActividad(lunes, "Configuración del equipo y accesos", 1.5);
        jornadaService.anadirActividad(lunes, "Lectura de la documentación del proyecto", 3.5);

        // Martes 06/10/2026 - 8 h
        Long martes = jornadaService.crearJornada(LocalDate.of(2026, 10, 6),
                "He entendido la estructura del proyecto y cómo se organiza el código.");
        jornadaService.anadirActividad(martes, "Estudio de la arquitectura de la aplicación", 4);
        jornadaService.anadirActividad(martes, "Primeras pruebas en local", 4);

        // Miércoles 07/10/2026 - 6 h
        Long miercoles = jornadaService.crearJornada(LocalDate.of(2026, 10, 7),
                "Primera tarea real: corregir un error sencillo en una consulta.");
        jornadaService.anadirActividad(miercoles, "Corrección de un error en una consulta", 6);

        // Jueves 08/10/2026 - 7 h
        Long jueves = jornadaService.crearJornada(LocalDate.of(2026, 10, 8),
                "He aprendido a escribir tests y a seguir el flujo de revisión del equipo.");
        jornadaService.anadirActividad(jueves, "Reunión de seguimiento con el tutor", 2);
        jornadaService.anadirActividad(jueves, "Escritura de tests unitarios", 5);

        // Viernes 09/10/2026 - 3 h
        Long viernes = jornadaService.crearJornada(LocalDate.of(2026, 10, 9),
                "Jornada corta, dedicada a documentar lo aprendido durante la semana.");
        jornadaService.anadirActividad(viernes, "Documentación de lo aprendido", 3);

        // Lunes 12/10/2026 - 7 h
        Long lunes2 = jornadaService.crearJornada(LocalDate.of(2026, 10, 12),
                "Empiezo a trabajar con más autonomía en las tareas asignadas.");
        jornadaService.anadirActividad(lunes2, "Desarrollo de una nueva funcionalidad", 7);

        // Viernes 16/10/2026 - 6 h, reflexión pendiente
        Long viernes2 = jornadaService.crearJornada(LocalDate.of(2026, 10, 16), null);
        jornadaService.anadirActividad(viernes2, "Revisión de código con el equipo", 6);
    }
}