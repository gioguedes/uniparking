package com.trabalhointeligencia.uniparking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.trabalhointeligencia.uniparking.models.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
