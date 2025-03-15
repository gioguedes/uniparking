package com.trabalhointeligencia.uniparking.services;

import com.trabalhointeligencia.uniparking.models.Estacionamento;
import com.trabalhointeligencia.uniparking.repository.EstacionamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EstacionamentoService {
    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Estacionamento salvarEstacionamento(Estacionamento estacionamento) {
        return estacionamentoRepository.save(estacionamento);
    }

    public List<Estacionamento> listarEstacionamentos() {
        return estacionamentoRepository.findAll();
    }

    public Optional<Estacionamento> buscarEstacionamentoPorId(Integer id) {
        return estacionamentoRepository.findById(id);
    }

    public BigDecimal valorcobranca(LocalDateTime dataEntrada, LocalDateTime dataSaida, int tempoMinimo, BigDecimal valorBase, BigDecimal valorAdicional) {
        return jdbcTemplate.execute((Connection conn) -> {
            try (CallableStatement stmt = conn.prepareCall("{call valorcobranca(?, ?, ?, ?, ?, ?)}")) {
                stmt.setTimestamp(1, java.sql.Timestamp.valueOf(dataEntrada));
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(dataSaida));
                stmt.setInt(3, tempoMinimo);
                stmt.setBigDecimal(4, valorBase);
                stmt.setBigDecimal(5, valorAdicional);
                stmt.registerOutParameter(6, Types.DECIMAL);
                stmt.execute();
                return stmt.getBigDecimal(6);
            }
        });
    }
}
