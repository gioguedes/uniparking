package com.trabalhointeligencia.uniparking.controller;
import com.trabalhointeligencia.uniparking.models.Registro;
import com.trabalhointeligencia.uniparking.services.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;
import java.util.stream.IntStream;
@Controller
@RequestMapping("/registros")
public class RegistroController {
    @Autowired
    private RegistroService registroService;
    @GetMapping
    public String listarRegistros(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        int pageSize = 10;
        Page<Registro> registroPage = registroService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("registroPage", registroPage);
        int totalPages = registroPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin";
    }
    @GetMapping("/excluir/{id}")
    public String excluirRegistro(@PathVariable("id") Integer id) {
        registroService.excluirRegistro(id);
        return "redirect:/registros";
    }
}
