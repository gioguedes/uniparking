package com.trabalhointeligencia.uniparking.controller;

import com.trabalhointeligencia.uniparking.models.Cliente;
import com.trabalhointeligencia.uniparking.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cadastroCliente";
    }

    @PostMapping
    public String salvarCliente(@Valid @ModelAttribute Cliente cliente, BindingResult result) {
        if (cliente.getNome() != null && cliente.getNome().length() > 100) {
            result.rejectValue("nome", "error.cliente", "O nome deve ter no máximo 100 caracteres");
        }
        if (cliente.getTelefone() != null && cliente.getTelefone().length() > 20) {
            result.rejectValue("telefone", "error.cliente", "O telefone deve ter no máximo 20 caracteres");
        }
        if (cliente.getEmail() != null && cliente.getEmail().length() > 100) {
            result.rejectValue("email", "error.cliente", "O e-mail deve ter no máximo 100 caracteres");
        }
        if (cliente.getEndereco() != null && cliente.getEndereco().length() > 100) {
            result.rejectValue("endereco", "error.cliente", "O endereço deve ter no máximo 100 caracteres");
        }
        if (result.hasErrors()) {
            return "cadastroCliente";
        }
        clienteService.salvarCliente(cliente);
        return "redirect:/clientes/listar";
    }

    @GetMapping("/listar")
    public String listarClientes(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        int pageSize = 10;
        Page<Cliente> clientePage = clienteService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("clientePage", clientePage);
        int totalPages = clientePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "clients";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        Optional<Cliente> clienteOpt = clienteService.buscarClientePorId(id);
        if (clienteOpt.isPresent()) {
            model.addAttribute("cliente", clienteOpt.get());
            return "editarCliente";
        }
        return "redirect:/clientes/listar";
    }

    @PostMapping("/editar")
    public String editarCliente(@Valid @ModelAttribute Cliente cliente, BindingResult result) {
        if (cliente.getNome() != null && cliente.getNome().length() > 100) {
            result.rejectValue("nome", "error.cliente", "O nome deve ter no máximo 100 caracteres");
        }
        if (cliente.getTelefone() != null && cliente.getTelefone().length() > 20) {
            result.rejectValue("telefone", "error.cliente", "O telefone deve ter no máximo 20 caracteres");
        }
        if (cliente.getEmail() != null && cliente.getEmail().length() > 100) {
            result.rejectValue("email", "error.cliente", "O e-mail deve ter no máximo 100 caracteres");
        }
        if (cliente.getEndereco() != null && cliente.getEndereco().length() > 200) {
            result.rejectValue("endereco", "error.cliente", "O endereço deve ter no máximo 200 caracteres");
        }
        if (result.hasErrors()) {
            return "editarCliente";
        }
        clienteService.salvarCliente(cliente);
        return "redirect:/clientes/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable("id") Integer id) {
        clienteService.excluirCliente(id);
        return "redirect:/clientes/listar";
    }
}
