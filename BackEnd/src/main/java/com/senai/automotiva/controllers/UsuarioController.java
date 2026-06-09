package com.senai.automotiva.controllers;

import com.senai.automotiva.dtos.UsuarioDTO;
import com.senai.automotiva.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Criar novo usuário")
    public ResponseEntity<UsuarioDTO.Resposta> criar(@RequestBody @Valid UsuarioDTO.Requisicao dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<UsuarioDTO.Resposta> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioDTO.Requisicao dto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<UsuarioDTO.Resposta>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioDTO.Resposta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PatchMapping("/{id}/ativar-desativar")
    @Operation(summary = "Ativar ou desativar usuário")
    public ResponseEntity<Void> ativarDesativar(@PathVariable Long id) {
        usuarioService.ativarDesativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
