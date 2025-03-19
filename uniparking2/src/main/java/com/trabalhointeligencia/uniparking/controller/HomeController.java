package com.trabalhointeligencia.uniparking.controller;

import com.trabalhointeligencia.uniparking.models.Estacionamento;
import com.trabalhointeligencia.uniparking.models.Registro;
import com.trabalhointeligencia.uniparking.services.EstacionamentoService;
import com.trabalhointeligencia.uniparking.services.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private EstacionamentoService estacionamentoService;

    @Autowired
    private RegistroService registroService;

    @GetMapping("/")
    public String home(Model model) {
        List<Estacionamento> estacionamentos = estacionamentoService.listarEstacionamentos()
                .stream()
                .filter(est -> Boolean.TRUE.equals(est.getAtivo()))
                .collect(Collectors.toList());
        Map<Integer, Integer> vagasDisponiveisMap = new HashMap<>();
        for (Estacionamento est : estacionamentos) {
            int totalVagas = est.getQntVagas();
            long vagasOcupadas = registroService.listarRegistros().stream()
                    .filter(reg -> reg.getDataSaida() == null
                            && reg.getVaga() != null
                            && reg.getVaga().getEstacionamento().getIdEstacionamento().equals(est.getIdEstacionamento()))
                    .count();
            int disponiveis = totalVagas - (int) vagasOcupadas;
            vagasDisponiveisMap.put(est.getIdEstacionamento(), disponiveis);
        }
        List<Registro> registrosAtivos = registroService.listarRegistros().stream()
                .filter(reg -> reg.getDataSaida() == null && reg.getVeiculo() != null)
                .collect(Collectors.toList());
        model.addAttribute("estacionamentos", estacionamentos);
        model.addAttribute("vagasDisponiveisMap", vagasDisponiveisMap);
        model.addAttribute("registrosAtivos", registrosAtivos);
        return "index";
    }
}
