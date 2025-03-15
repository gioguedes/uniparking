package com.trabalhointeligencia.uniparking.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Estacionamento")
public class Estacionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estacionamento")
    private Integer idEstacionamento;

    @Column(name = "qnt_vagas", nullable = false)
    private Integer qntVagas;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "endereco", length = 200)
    private String endereco;

    public Integer getIdEstacionamento() {
        return idEstacionamento;
    }
    public void setIdEstacionamento(Integer idEstacionamento) {
        this.idEstacionamento = idEstacionamento;
    }
    public Integer getQntVagas() {
        return qntVagas;
    }
    public void setQntVagas(Integer qntVagas) {
        this.qntVagas = qntVagas;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
