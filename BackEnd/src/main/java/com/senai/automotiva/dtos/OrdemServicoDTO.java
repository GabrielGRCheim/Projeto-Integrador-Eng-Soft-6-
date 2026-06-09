package com.senai.automotiva.dtos;

import com.senai.automotiva.enums.StatusOrdemServico;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class OrdemServicoDTO {

    public static class Requisicao {
        @NotNull(message = "Carro é obrigatório")
        private Long carroId;

        private Long responsavelId;
        private String descricao;
        private BigDecimal valorMaoObra;

        public Long getCarroId() { return carroId; }
        public void setCarroId(Long carroId) { this.carroId = carroId; }

        public Long getResponsavelId() { return responsavelId; }
        public void setResponsavelId(Long responsavelId) { this.responsavelId = responsavelId; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public BigDecimal getValorMaoObra() { return valorMaoObra; }
        public void setValorMaoObra(BigDecimal valorMaoObra) { this.valorMaoObra = valorMaoObra; }
    }

    public static class Resposta {
        private Long id;
        private String numeroOs;
        private Long carroId;
        private String placaCarro;
        private String modeloCarro;
        private String nomeCliente;
        private String nomeResponsavel;
        private StatusOrdemServico status;
        private String descricao;
        private BigDecimal valorMaoObra;
        private BigDecimal valorTotal;
        private String criadoEm;
        private String concluidoEm;
        private List<ItemOrdemServicoDTO.Resposta> itens;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNumeroOs() { return numeroOs; }
        public void setNumeroOs(String numeroOs) { this.numeroOs = numeroOs; }

        public Long getCarroId() { return carroId; }
        public void setCarroId(Long carroId) { this.carroId = carroId; }

        public String getPlacaCarro() { return placaCarro; }
        public void setPlacaCarro(String placaCarro) { this.placaCarro = placaCarro; }

        public String getModeloCarro() { return modeloCarro; }
        public void setModeloCarro(String modeloCarro) { this.modeloCarro = modeloCarro; }

        public String getNomeCliente() { return nomeCliente; }
        public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

        public String getNomeResponsavel() { return nomeResponsavel; }
        public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }

        public StatusOrdemServico getStatus() { return status; }
        public void setStatus(StatusOrdemServico status) { this.status = status; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public BigDecimal getValorMaoObra() { return valorMaoObra; }
        public void setValorMaoObra(BigDecimal valorMaoObra) { this.valorMaoObra = valorMaoObra; }

        public BigDecimal getValorTotal() { return valorTotal; }
        public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

        public String getCriadoEm() { return criadoEm; }
        public void setCriadoEm(String criadoEm) { this.criadoEm = criadoEm; }

        public String getConcluidoEm() { return concluidoEm; }
        public void setConcluidoEm(String concluidoEm) { this.concluidoEm = concluidoEm; }

        public List<ItemOrdemServicoDTO.Resposta> getItens() { return itens; }
        public void setItens(List<ItemOrdemServicoDTO.Resposta> itens) { this.itens = itens; }
    }
}
