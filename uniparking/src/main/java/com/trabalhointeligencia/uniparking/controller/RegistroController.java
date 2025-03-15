package com.trabalhointeligencia.uniparking.controller;

import com.trabalhointeligencia.uniparking.services.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registros")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @GetMapping
    public String listarRegistros(Model model) {
        model.addAttribute("registros", registroService.listarRegistros());
        return "admin";
    }
}
