package com.senai.automotiva.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ItemOrdemServicoDTO {

    public static class Requisicao {
        @NotNull(message = "Ordem de serviço é obrigatória")
        private Long ordemServicoId;

        private Long servicoId;
        private Long pecaId;

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser positiva")
        private Integer quantidade;

        @NotNull(message = "Valor unitário é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        private BigDecimal valorUnitario;

        private String observacao;

        public Long getOrdemServicoId() { return ordemServicoId; }
        public void setOrdemServicoId(Long ordemServicoId) { this.ordemServicoId = ordemServicoId; }

        public Long getServicoId() { return servicoId; }
        public void setServicoId(Long servicoId) { this.servicoId = servicoId; }

        public Long getPecaId() { return pecaId; }
        public void setPecaId(Long pecaId) { this.pecaId = pecaId; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

        public BigDecimal getValorUnitario() { return valorUnitario; }
        public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

        public String getObservacao() { return observacao; }
        public void setObservacao(String observacao) { this.observacao = observacao; }
    }

    public static class Resposta {
        private Long id;
        private Long ordemServicoId;
        private Long servicoId;
        private String nomeServico;
        private Long pecaId;
        private String nomePeca;
        private Integer quantidade;
        private BigDecimal valorUnitario;
        private BigDecimal valorTotal;
        private String observacao;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getOrdemServicoId() { return ordemServicoId; }
        public void setOrdemServicoId(Long ordemServicoId) { this.ordemServicoId = ordemServicoId; }

        public Long getServicoId() { return servicoId; }
        public void setServicoId(Long servicoId) { this.servicoId = servicoId; }

        public String getNomeServico() { return nomeServico; }
        public void setNomeServico(String nomeServico) { this.nomeServico = nomeServico; }

        public Long getPecaId() { return pecaId; }
        public void setPecaId(Long pecaId) { this.pecaId = pecaId; }

        public String getNomePeca() { return nomePeca; }
        public void setNomePeca(String nomePeca) { this.nomePeca = nomePeca; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

        public BigDecimal getValorUnitario() { return valorUnitario; }
        public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

        public BigDecimal getValorTotal() { return valorTotal; }
        public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

        public String getObservacao() { return observacao; }
        public void setObservacao(String observacao) { this.observacao = observacao; }
    }
}
