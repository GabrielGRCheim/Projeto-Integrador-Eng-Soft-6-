package com.senai.automotiva.controllers;

import com.senai.automotiva.dtos.ItemOrdemServicoDTO;
import com.senai.automotiva.dtos.OrdemServicoDTO;
import com.senai.automotiva.enums.StatusOrdemServico;
import com.senai.automotiva.services.OrdemServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ordens-servico")
@Tag(name = "Ordens de Serviço", description = "Gerenciamento de ordens de serviço")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @PostMapping
    @Operation(summary = "Abrir nova ordem de serviço")
    public ResponseEntity<OrdemServicoDTO.RespostaOrdem> criar(@RequestBody @Valid OrdemServicoDTO.RequisicaoOrdem dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemServicoService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ordem de serviço")
    public ResponseEntity<OrdemServicoDTO.RespostaOrdem> atualizar(@PathVariable Long id, @RequestBody @Valid OrdemServicoDTO.RequisicaoOrdem dto) {
        return ResponseEntity.ok(ordemServicoService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da OS")
    public ResponseEntity<OrdemServicoDTO.RespostaOrdem> atualizarStatus(
            @PathVariable Long id, @RequestParam StatusOrdemServico novoStatus) {
        return ResponseEntity.ok(ordemServicoService.atualizarStatus(id, novoStatus));
    }

    @GetMapping
    @Operation(summary = "Listar todas as ordens de serviço")
    public ResponseEntity<List<OrdemServicoDTO.RespostaOrdem>> listarTodas() {
        return ResponseEntity.ok(ordemServicoService.listarTodas());
    }

    @GetMapping("/abertas")
    @Operation(summary = "Listar ordens de serviço abertas/em andamento")
    public ResponseEntity<List<OrdemServicoDTO.RespostaOrdem>> listarAbertas() {
        return ResponseEntity.ok(ordemServicoService.listarAbertas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar OS por ID")
    public ResponseEntity<OrdemServicoDTO.RespostaOrdem> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordemServicoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar OS por cliente")
    public ResponseEntity<List<OrdemServicoDTO.RespostaOrdem>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ordemServicoService.listarPorCliente(clienteId));
    }

    @PostMapping("/itens")
    @Operation(summary = "Adicionar item (serviço ou peça) na OS")
    public ResponseEntity<ItemOrdemServicoDTO.RespostaItem> adicionarItem(@RequestBody @Valid ItemOrdemServicoDTO.RequisicaoItem dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemServicoService.adicionarItem(dto));
    }

    @DeleteMapping("/itens/{itemId}")
    @Operation(summary = "Remover item da OS")
    public ResponseEntity<Void> removerItem(@PathVariable Long itemId) {
        ordemServicoService.removerItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
