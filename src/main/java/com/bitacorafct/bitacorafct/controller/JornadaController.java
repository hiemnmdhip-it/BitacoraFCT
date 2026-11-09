package com.bitacorafct.bitacorafct.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bitacorafct.bitacorafct.dto.JornadaResumenDTO;
import com.bitacorafct.bitacorafct.service.JornadaService;

@Controller
public class JornadaController {

    private final JornadaService jornadaService;

    public JornadaController(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping("/jornadas")
    public String listar(Model model) {
        List<JornadaResumenDTO> jornadas = jornadaService.listarJornadas();
        model.addAttribute("jornadas", jornadas);
        return "jornadas";
    }
}