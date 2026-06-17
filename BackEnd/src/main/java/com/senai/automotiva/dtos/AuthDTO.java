package com.senai.automotiva.dtos;

import com.senai.automotiva.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDTO {

    /** Payload enviado pelo cliente para realizar login. */
    public static class LoginRequisicao {

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        private String email;

        @NotBlank(message = "Senha é obrigatória")
        private String senha;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    /** Resposta retornada ao cliente após login bem-sucedido. */
    public static class LoginResposta {

        private String token;
        private String tipo = "Bearer";
        private Long usuarioId;
        private String nome;
        private String email;
        private PerfilUsuario perfil;
        private long expiracaoMs;

        public LoginResposta(String token, Long usuarioId, String nome,
                             String email, PerfilUsuario perfil, long expiracaoMs) {
            this.token = token;
            this.usuarioId = usuarioId;
            this.nome = nome;
            this.email = email;
            this.perfil = perfil;
            this.expiracaoMs = expiracaoMs;
        }

        public String getToken() { return token; }
        public String getTipo() { return tipo; }
        public Long getUsuarioId() { return usuarioId; }
        public String getNome() { return nome; }
        public String getEmail() { return email; }
        public PerfilUsuario getPerfil() { return perfil; }
        public long getExpiracaoMs() { return expiracaoMs; }
    }
}
