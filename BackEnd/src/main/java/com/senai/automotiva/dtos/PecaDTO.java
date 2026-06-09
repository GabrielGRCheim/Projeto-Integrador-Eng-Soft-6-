package com.senai.automotiva.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class PecaDTO {

    public static class Requisicao {
        @NotBlank(message = "Nome da peça é obrigatório")
        private String nome;

        private String codigo;
        private String descricao;

        @NotNull(message = "Preço de custo é obrigatório")
        @Positive(message = "Preço de custo deve ser positivo")
        private BigDecimal precoCusto;

        @NotNull(message = "Preço de venda é obrigatório")
        @Positive(message = "Preço de venda deve ser positivo")
        private BigDecimal precoVenda;

        @PositiveOrZero(message = "Quantidade de estoque não pode ser negativa")
        private Integer quantidadeEstoque = 0;

        private Integer quantidadeMinima = 1;
        private String unidade;
        private String fornecedor;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public BigDecimal getPrecoCusto() { return precoCusto; }
        public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }

        public BigDecimal getPrecoVenda() { return precoVenda; }
        public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

        public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
        public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

        public Integer getQuantidadeMinima() { return quantidadeMinima; }
        public void setQuantidadeMinima(Integer quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }

        public String getUnidade() { return unidade; }
        public void setUnidade(String unidade) { this.unidade = unidade; }

        public String getFornecedor() { return fornecedor; }
        public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
    }

    public static class Resposta {
        private Long id;
        private String nome;
        private String codigo;
        private String descricao;
        private BigDecimal precoCusto;
        private BigDecimal precoVenda;
        private Integer quantidadeEstoque;
        private Integer quantidadeMinima;
        private String unidade;
        private String fornecedor;
        private Boolean ativo;
        private Boolean estoqueBaixo;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public BigDecimal getPrecoCusto() { return precoCusto; }
        public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }

        public BigDecimal getPrecoVenda() { return precoVenda; }
        public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

        public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
        public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

        public Integer getQuantidadeMinima() { return quantidadeMinima; }
        public void setQuantidadeMinima(Integer quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }

        public String getUnidade() { return unidade; }
        public void setUnidade(String unidade) { this.unidade = unidade; }

        public String getFornecedor() { return fornecedor; }
        public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }

        public Boolean getAtivo() { return ativo; }
        public void setAtivo(Boolean ativo) { this.ativo = ativo; }

        public Boolean getEstoqueBaixo() { return estoqueBaixo; }
        public void setEstoqueBaixo(Boolean estoqueBaixo) { this.estoqueBaixo = estoqueBaixo; }
    }

    public static class AjusteEstoque {
        @NotNull(message = "Quantidade é obrigatória")
        private Integer quantidade;

        @NotBlank(message = "Motivo é obrigatório")
        private String motivo;

        @NotBlank(message = "Tipo de movimentação é obrigatório")
        private String tipoMovimentacao; // ENTRADA, SAIDA, AJUSTE

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }

        public String getTipoMovimentacao() { return tipoMovimentacao; }
        public void setTipoMovimentacao(String tipoMovimentacao) { this.tipoMovimentacao = tipoMovimentacao; }
    }
}
