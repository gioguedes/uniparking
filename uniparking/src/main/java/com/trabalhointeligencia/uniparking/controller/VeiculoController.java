package com.trabalhointeligencia.uniparking.controller;

import com.trabalhointeligencia.uniparking.models.Registro;
import com.trabalhointeligencia.uniparking.models.Veiculo;
import com.trabalhointeligencia.uniparking.models.Vaga;
import com.trabalhointeligencia.uniparking.services.ClienteService;
import com.trabalhointeligencia.uniparking.services.EstacionamentoService;
import com.trabalhointeligencia.uniparking.services.RegistroService;
import com.trabalhointeligencia.uniparking.services.ValoresService;
import com.trabalhointeligencia.uniparking.services.VagaService;
import com.trabalhointeligencia.uniparking.services.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EstacionamentoService estacionamentoService;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private RegistroService registroService;

    @Autowired
    private ValoresService valoresService;

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("veiculo", new Veiculo());
        model.addAttribute("clientes", clienteService.listarClientes());
        model.addAttribute("estacionamentos", estacionamentoService.listarEstacionamentos());
        return "cadastroVeiculo";
    }

    @PostMapping
    public String salvarVeiculo(@ModelAttribute Veiculo veiculo,
                                @RequestParam("estacionamentoId") Integer estacionamentoId,
                                @RequestParam(value = "numeroVaga", required = false) String numeroVaga) {
        Veiculo novoVeiculo = veiculoService.salvarVeiculo(veiculo);
        Vaga vagaSelecionada = null;
        if (numeroVaga != null && !numeroVaga.trim().isEmpty()) {
            vagaSelecionada = new Vaga();
            vagaSelecionada.setNumeroVaga(numeroVaga);
            Optional<com.trabalhointeligencia.uniparking.models.Estacionamento> estOpt = estacionamentoService.buscarEstacionamentoPorId(estacionamentoId);
            if (estOpt.isPresent()) {
                vagaSelecionada.setEstacionamento(estOpt.get());
            }
            vagaSelecionada = vagaService.salvarVaga(vagaSelecionada);
        } else {
            List<Vaga> vagasDoEstacionamento = vagaService.listarVagas().stream()
                    .filter(v -> v.getEstacionamento().getIdEstacionamento().equals(estacionamentoId))
                    .collect(Collectors.toList());
            if (!vagasDoEstacionamento.isEmpty()) {
                vagaSelecionada = vagasDoEstacionamento.get(0);
            }
        }
        Registro registro = new Registro();
        registro.setDataEntrada(LocalDateTime.now());
        registro.setDataSaida(null);
        registro.setVeiculo(novoVeiculo);
        registro.setVaga(vagaSelecionada);
        registroService.salvarRegistro(registro);
        return "redirect:/veiculos/listar";
    }

    @GetMapping("/listar")
    public String listarVeiculos(Model model) {
        List<Veiculo> veiculosAtivos = registroService.listarRegistros().stream()
                .filter(reg -> reg.getDataSaida() == null && reg.getVeiculo() != null)
                .map(Registro::getVeiculo)
                .collect(Collectors.toList());
        model.addAttribute("veiculos", veiculosAtivos);
        return "vehicles";
    }

    @PostMapping("/checkout/{id}")
    public String checkoutVeiculo(@PathVariable("id") Integer veiculoId) {
        Optional<Registro> registroOpt = registroService.listarRegistros().stream()
                .filter(reg -> reg.getVeiculo() != null &&
                        reg.getVeiculo().getId().equals(veiculoId) &&
                        reg.getDataSaida() == null)
                .findFirst();
        if (registroOpt.isPresent()) {
            Registro registro = registroOpt.get();
            LocalDateTime dataSaida = LocalDateTime.now();
            registro.setDataSaida(dataSaida);
            if (registro.getVaga() != null && registro.getVaga().getEstacionamento() != null) {
                Integer estId = registro.getVaga().getEstacionamento().getIdEstacionamento();
                Optional<com.trabalhointeligencia.uniparking.models.Valores> valoresOpt = valoresService.buscarPorEstacionamentoId(estId);
                if (valoresOpt.isPresent()) {
                    com.trabalhointeligencia.uniparking.models.Valores valores = valoresOpt.get();
                    BigDecimal valorCobranca = estacionamentoService.valorcobranca(
                            registro.getDataEntrada(),
                            dataSaida,
                            valores.getTempoMinimo(),
                            valores.getValorBase(),
                            valores.getValorAdicional()
                    );
                    registro.setValorCobrado(valorCobranca);
                }
            }
            registroService.salvarRegistro(registro);
        }
        return "redirect:/veiculos/listar";
    }
}
