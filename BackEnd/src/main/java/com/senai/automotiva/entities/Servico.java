package com.senai.automotiva.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 300)
    private String descricao;

    @Column(name = "preco_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoBase;

    @Column(name = "tempo_estimado_horas")
    private Double tempoEstimadoHoras;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPrecoBase() { return precoBase; }
    public void setPrecoBase(BigDecimal precoBase) { this.precoBase = precoBase; }

    public Double getTempoEstimadoHoras() { return tempoEstimadoHoras; }
    public void setTempoEstimadoHoras(Double tempoEstimadoHoras) { this.tempoEstimadoHoras = tempoEstimadoHoras; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
