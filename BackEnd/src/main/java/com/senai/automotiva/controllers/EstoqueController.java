package com.senai.automotiva.controllers;

import com.senai.automotiva.dtos.PecaDTO;
import com.senai.automotiva.services.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estoque")
@Tag(name = "Estoque", description = "Gerenciamento de peças e estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/pecas")
    @Operation(summary = "Cadastrar nova peça")
    public ResponseEntity<PecaDTO.RespostaPeca> criarPeca(@RequestBody @Valid PecaDTO.RequisicaoPeca dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueService.criarPeca(dto));
    }

    @PutMapping("/pecas/{id}")
    @Operation(summary = "Atualizar peça")
    public ResponseEntity<PecaDTO.RespostaPeca> atualizarPeca(@PathVariable Long id, @RequestBody @Valid PecaDTO.RequisicaoPeca dto) {
        return ResponseEntity.ok(estoqueService.atualizarPeca(id, dto));
    }

    @GetMapping("/pecas")
    @Operation(summary = "Listar todas as peças")
    public ResponseEntity<List<PecaDTO.RespostaPeca>> listarTodas() {
        return ResponseEntity.ok(estoqueService.listarTodas());
    }

    @GetMapping("/pecas/{id}")
    @Operation(summary = "Buscar peça por ID")
    public ResponseEntity<PecaDTO.RespostaPeca> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estoqueService.buscarPorId(id));
    }

    @GetMapping("/pecas/estoque-baixo")
    @Operation(summary = "Listar peças com estoque abaixo do mínimo")
    public ResponseEntity<List<PecaDTO.RespostaPeca>> listarComEstoqueBaixo() {
        return ResponseEntity.ok(estoqueService.listarComEstoqueBaixo());
    }

    @PatchMapping("/pecas/{id}/ajustar")
    @Operation(summary = "Ajustar estoque de uma peça (entrada, saída ou ajuste manual)")
    public ResponseEntity<PecaDTO.RespostaPeca> ajustarEstoque(@PathVariable Long id, @RequestBody @Valid PecaDTO.AjusteEstoque dto) {
        return ResponseEntity.ok(estoqueService.ajustarEstoque(id, dto));
    }

    @PatchMapping("/pecas/{id}/ativar-desativar")
    @Operation(summary = "Ativar ou desativar peça")
    public ResponseEntity<Void> ativarDesativar(@PathVariable Long id) {
        estoqueService.ativarDesativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pecas/{id}")
    @Operation(summary = "Deletar peça")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estoqueService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
