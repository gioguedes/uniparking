package com.trabalhointeligencia.uniparking.services;

import com.trabalhointeligencia.uniparking.models.Cliente;
import com.trabalhointeligencia.uniparking.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findByAtivoTrue();
    }

    public Page<Cliente> findPaginated(Pageable pageable) {
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), clientesAtivos.size());
        List<Cliente> sublist = clientesAtivos.subList(start, end);
        return new PageImpl<>(sublist, pageable, clientesAtivos.size());
    }

    public Optional<Cliente> buscarClientePorId(Integer id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if(clienteOpt.isPresent() && clienteOpt.get().getAtivo()){
            return clienteOpt;
        }
        return Optional.empty();
    }

    public void excluirCliente(Integer id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if(clienteOpt.isPresent()){
            Cliente cliente = clienteOpt.get();
            cliente.setAtivo(false);
            clienteRepository.save(cliente);
        }
    }
}
