package com.senai.automotiva.controllers;

import com.senai.automotiva.dtos.CarroDTO;
import com.senai.automotiva.services.CarroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/carros")
@Tag(name = "Carros", description = "Gerenciamento de veículos")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @PostMapping
    @Operation(summary = "Cadastrar novo carro")
    public ResponseEntity<CarroDTO.RespostaCarro> criar(@RequestBody @Valid CarroDTO.RequisicaoCarro dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carroService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar carro")
    public ResponseEntity<CarroDTO.RespostaCarro> atualizar(@PathVariable Long id, @RequestBody @Valid CarroDTO.RequisicaoCarro dto) {
        return ResponseEntity.ok(carroService.atualizar(id, dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos os carros")
    public ResponseEntity<List<CarroDTO.RespostaCarro>> listarTodos() {
        return ResponseEntity.ok(carroService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar carro por ID")
    public ResponseEntity<CarroDTO.RespostaCarro> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carroService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar carros por cliente")
    public ResponseEntity<List<CarroDTO.RespostaCarro>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(carroService.listarPorCliente(clienteId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar carro")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        carroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
