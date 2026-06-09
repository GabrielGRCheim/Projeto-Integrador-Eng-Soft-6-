package com.senai.automotiva.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peca_id", nullable = false)
    private Peca peca;

    @Column(name = "tipo_movimentacao", nullable = false, length = 20)
    private String tipoMovimentacao; // ENTRADA, SAIDA, AJUSTE

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "quantidade_anterior")
    private Integer quantidadeAnterior;

    @Column(name = "quantidade_posterior")
    private Integer quantidadePosterior;

    @Column(length = 300)
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id")
    private OrdemServico ordemServico;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Peca getPeca() { return peca; }
    public void setPeca(Peca peca) { this.peca = peca; }

    public String getTipoMovimentacao() { return tipoMovimentacao; }
    public void setTipoMovimentacao(String tipoMovimentacao) { this.tipoMovimentacao = tipoMovimentacao; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Integer getQuantidadeAnterior() { return quantidadeAnterior; }
    public void setQuantidadeAnterior(Integer quantidadeAnterior) { this.quantidadeAnterior = quantidadeAnterior; }

    public Integer getQuantidadePosterior() { return quantidadePosterior; }
    public void setQuantidadePosterior(Integer quantidadePosterior) { this.quantidadePosterior = quantidadePosterior; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public OrdemServico getOrdemServico() { return ordemServico; }
    public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
