package com.senai.automotiva.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClienteDTO {

    public static class Requisicao {
        @NotBlank(message = "Nome é obrigatório")
        private String nome;

        @NotBlank(message = "CPF é obrigatório")
        @Size(min = 11, max = 14, message = "CPF inválido")
        private String cpf;

        private String telefone;
        private String email;
        private String endereco;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }

        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }
    }

    public static class Resposta {
        private Long id;
        private String nome;
        private String cpf;
        private String telefone;
        private String email;
        private String endereco;
        private Boolean ativo;
        private int totalCarros;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }

        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }

        public Boolean getAtivo() { return ativo; }
        public void setAtivo(Boolean ativo) { this.ativo = ativo; }

        public int getTotalCarros() { return totalCarros; }
        public void setTotalCarros(int totalCarros) { this.totalCarros = totalCarros; }
    }
}
