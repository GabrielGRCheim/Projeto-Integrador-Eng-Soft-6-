package com.senai.automotiva.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ServicoDTO {

    public static class RequisicaoServico {
        @NotBlank(message = "Nome do serviço é obrigatório")
        private String nome;

        private String descricao;

        @NotNull(message = "Preço base é obrigatório")
        @Positive(message = "Preço deve ser positivo")
        private BigDecimal precoBase;

        private Double tempoEstimadoHoras;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public BigDecimal getPrecoBase() { return precoBase; }
        public void setPrecoBase(BigDecimal precoBase) { this.precoBase = precoBase; }

        public Double getTempoEstimadoHoras() { return tempoEstimadoHoras; }
        public void setTempoEstimadoHoras(Double tempoEstimadoHoras) { this.tempoEstimadoHoras = tempoEstimadoHoras; }
    }

    public static class RespostaServico {
        private Long id;
        private String nome;
        private String descricao;
        private BigDecimal precoBase;
        private Double tempoEstimadoHoras;
        private Boolean ativo;

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
}
