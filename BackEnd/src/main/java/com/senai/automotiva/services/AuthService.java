package com.senai.automotiva.services;

import com.senai.automotiva.dtos.AuthDTO;
import com.senai.automotiva.entities.Usuario;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.UsuarioRepository;
import com.senai.automotiva.security.JwtUtil;
import com.senai.automotiva.security.UsuarioDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela autenticação de usuários.
 * Valida as credenciais e emite o token JWT em caso de sucesso.
 */
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.expiracao-ms}")
    private long expiracaoMs;

    private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    /**
     * Autentica o usuário e retorna um {@link AuthDTO.LoginResposta} contendo o token JWT.
     *
     * @param requisicao DTO com e-mail e senha.
     * @return Resposta com token e dados do usuário autenticado.
     * @throws RegraNegocioException se as credenciais forem inválidas ou o usuário estiver inativo.
     */
    public AuthDTO.LoginResposta login(AuthDTO.LoginRequisicao requisicao) {
        Usuario usuario = usuarioRepository.findByEmail(requisicao.getEmail())
                .orElseThrow(() -> new RegraNegocioException("Credenciais inválidas."));

        if (!usuario.getAtivo()) {
            throw new RegraNegocioException("Usuário inativo. Entre em contato com o administrador.");
        }

        if (!codificador.matches(requisicao.getSenha(), usuario.getSenha())) {
            throw new RegraNegocioException("Credenciais inválidas.");
        }

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtUtil.gerarToken(userDetails);

        return new AuthDTO.LoginResposta(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                expiracaoMs
        );
    }
}
