package com.senai.automotiva.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CarroDTO {

    public static class Requisicao {
        @NotBlank(message = "Marca é obrigatória")
        private String marca;

        @NotBlank(message = "Modelo é obrigatório")
        private String modelo;

        @NotNull(message = "Ano é obrigatório")
        private Integer ano;

        @NotBlank(message = "Placa é obrigatória")
        private String placa;

        private String cor;
        private String chassi;

        @NotNull(message = "Cliente é obrigatório")
        private Long clienteId;

        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }

        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }

        public Integer getAno() { return ano; }
        public void setAno(Integer ano) { this.ano = ano; }

        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }

        public String getCor() { return cor; }
        public void setCor(String cor) { this.cor = cor; }

        public String getChassi() { return chassi; }
        public void setChassi(String chassi) { this.chassi = chassi; }

        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    }

    public static class Resposta {
        private Long id;
        private String marca;
        private String modelo;
        private Integer ano;
        private String placa;
        private String cor;
        private String chassi;
        private Long clienteId;
        private String nomeCliente;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }

        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }

        public Integer getAno() { return ano; }
        public void setAno(Integer ano) { this.ano = ano; }

        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }

        public String getCor() { return cor; }
        public void setCor(String cor) { this.cor = cor; }

        public String getChassi() { return chassi; }
        public void setChassi(String chassi) { this.chassi = chassi; }

        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

        public String getNomeCliente() { return nomeCliente; }
        public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    }
}
