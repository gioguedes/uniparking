package com.trabalhointeligencia.uniparking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.trabalhointeligencia.uniparking.models.Estacionamento;

public interface EstacionamentoRepository extends JpaRepository<Estacionamento, Integer> {
}
