package com.trabalhointeligencia.uniparking.services;
import com.trabalhointeligencia.uniparking.models.Registro;
import com.trabalhointeligencia.uniparking.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class RegistroService {
    @Autowired
    private RegistroRepository registroRepository;
    public Registro salvarRegistro(Registro registro) {
        return registroRepository.save(registro);
    }
    public List<Registro> listarRegistros() {
        return registroRepository.findAll();
    }
    public Page<Registro> findPaginated(Pageable pageable) {
        return registroRepository.findAll(pageable);
    }
    public void excluirRegistro(Integer id) {
        registroRepository.deleteById(id);
    }
}
