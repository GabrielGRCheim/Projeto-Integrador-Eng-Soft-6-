package com.senai.automotiva.dtos;

import com.senai.automotiva.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    public static class RequisicaoUsuario {
        @NotBlank(message = "Nome é obrigatório")
        private String nome;

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        private String email;

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        private String senha;

        @NotNull(message = "Perfil é obrigatório")
        private PerfilUsuario perfil;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }

        public PerfilUsuario getPerfil() { return perfil; }
        public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }
    }


    public static class RespostaUsuario {
        private Long id;
        private String nome;
        private String email;
        private PerfilUsuario perfil;
        private Boolean ativo;
        private String criadoEm;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public PerfilUsuario getPerfil() { return perfil; }
        public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }

        public Boolean getAtivo() { return ativo; }
        public void setAtivo(Boolean ativo) { this.ativo = ativo; }

        public String getCriadoEm() { return criadoEm; }
        public void setCriadoEm(String criadoEm) { this.criadoEm = criadoEm; }
    }
}
