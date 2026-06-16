package com.senai.automotiva.services;

import com.senai.automotiva.dtos.UsuarioDTO;
import com.senai.automotiva.entities.Usuario;
import com.senai.automotiva.exceptions.RecursoNaoEncontradoException;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    @Transactional
    public UsuarioDTO.RespostaUsuario criar(UsuarioDTO.RequisicaoUsuario dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com o e-mail: " + dto.getEmail());
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(codificador.encode(dto.getSenha()));
        usuario.setPerfil(dto.getPerfil());
        usuario.setAtivo(true);
        return toResposta(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDTO.RespostaUsuario atualizar(Long id, UsuarioDTO.RequisicaoUsuario dto) {
        Usuario usuario = buscarEntidade(id);
        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RegraNegocioException("E-mail já está em uso por outro usuário.");
        }
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(codificador.encode(dto.getSenha()));
        }
        usuario.setPerfil(dto.getPerfil());
        return toResposta(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO.RespostaUsuario> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO.RespostaUsuario buscarPorId(Long id) {
        return toResposta(buscarEntidade(id));
    }

    @Transactional
    public void ativarDesativar(Long id) {
        Usuario usuario = buscarEntidade(id);
        usuario.setAtivo(!usuario.getAtivo());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidade(id);
        usuarioRepository.deleteById(id);
    }

    private Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id: " + id));
    }

    private UsuarioDTO.RespostaUsuario toResposta(Usuario u) {
        UsuarioDTO.RespostaUsuario dto = new UsuarioDTO.RespostaUsuario();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());
        dto.setPerfil(u.getPerfil());
        dto.setAtivo(u.getAtivo());
        dto.setCriadoEm(u.getCriadoEm() != null ? u.getCriadoEm().toString() : null);
        return dto;
    }
}
