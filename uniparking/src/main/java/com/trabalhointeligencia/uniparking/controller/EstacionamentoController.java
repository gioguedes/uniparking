package com.trabalhointeligencia.uniparking.controller;

import com.trabalhointeligencia.uniparking.models.Estacionamento;
import com.trabalhointeligencia.uniparking.models.Valores;
import com.trabalhointeligencia.uniparking.services.EstacionamentoService;
import com.trabalhointeligencia.uniparking.services.ValoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/estacionamento")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoService estacionamentoService;

    @Autowired
    private ValoresService valoresService;

    @GetMapping
    public String listarEstacionamentos(Model model) {
        model.addAttribute("estacionamentos", estacionamentoService.listarEstacionamentos());
        model.addAttribute("modo", "listar");
        return "estacionamento";
    }

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("estacionamento", new Estacionamento());
        model.addAttribute("modo", "cadastrar");
        return "estacionamento";
    }

    @PostMapping
    public String salvarEstacionamento(@ModelAttribute Estacionamento estacionamento,
                                       @RequestParam("tempoMinimo") Integer tempoMinimo,
                                       @RequestParam("valorBase") String valorBase,
                                       @RequestParam("valorAdicional") String valorAdicional) {
        Estacionamento estSalvo = estacionamentoService.salvarEstacionamento(estacionamento);
        Valores valores = new Valores();
        valores.setEstacionamento(estSalvo);
        valores.setTempoMinimo(tempoMinimo);
        valores.setValorBase(new java.math.BigDecimal(valorBase));
        valores.setValorAdicional(new java.math.BigDecimal(valorAdicional));
        valoresService.salvarValor(valores);
        return "redirect:/estacionamento";
    }
}
