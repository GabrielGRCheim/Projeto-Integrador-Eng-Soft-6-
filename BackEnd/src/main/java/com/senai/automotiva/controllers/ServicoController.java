package com.senai.automotiva.controllers;

import com.senai.automotiva.dtos.ServicoDTO;
import com.senai.automotiva.services.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@Tag(name = "Serviços", description = "Catálogo de serviços da oficina")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    @Operation(summary = "Cadastrar novo serviço")
    public ResponseEntity<ServicoDTO.Resposta> criar(@RequestBody @Valid ServicoDTO.Requisicao dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar serviço")
    public ResponseEntity<ServicoDTO.Resposta> atualizar(@PathVariable Long id, @RequestBody @Valid ServicoDTO.Requisicao dto) {
        return ResponseEntity.ok(servicoService.atualizar(id, dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos os serviços")
    public ResponseEntity<List<ServicoDTO.Resposta>> listarTodos() {
        return ResponseEntity.ok(servicoService.listarTodos());
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar serviços ativos")
    public ResponseEntity<List<ServicoDTO.Resposta>> listarAtivos() {
        return ResponseEntity.ok(servicoService.listarAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar serviço por ID")
    public ResponseEntity<ServicoDTO.Resposta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/ativar-desativar")
    @Operation(summary = "Ativar ou desativar serviço")
    public ResponseEntity<Void> ativarDesativar(@PathVariable Long id) {
        servicoService.ativarDesativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar serviço")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        servicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
