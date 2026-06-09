package com.senai.automotiva.services;

import com.senai.automotiva.dtos.CarroDTO;
import com.senai.automotiva.entities.Carro;
import com.senai.automotiva.entities.Cliente;
import com.senai.automotiva.exceptions.RecursoNaoEncontradoException;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteService clienteService;

    @Transactional
    public CarroDTO.Resposta criar(CarroDTO.Requisicao dto) {
        String placaFormatada = dto.getPlaca().toUpperCase().replaceAll("[^A-Z0-9]", "");
        if (carroRepository.existsByPlaca(placaFormatada)) {
            throw new RegraNegocioException("Já existe um carro cadastrado com a placa: " + placaFormatada);
        }
        Cliente cliente = clienteService.buscarEntidade(dto.getClienteId());
        if (!cliente.getAtivo()) {
            throw new RegraNegocioException("Não é possível cadastrar carro para um cliente inativo.");
        }
        Carro carro = new Carro();
        preencherEntidade(carro, dto, placaFormatada, cliente);
        return toResposta(carroRepository.save(carro));
    }

    @Transactional
    public CarroDTO.Resposta atualizar(Long id, CarroDTO.Requisicao dto) {
        Carro carro = buscarEntidade(id);
        String placaFormatada = dto.getPlaca().toUpperCase().replaceAll("[^A-Z0-9]", "");
        if (!carro.getPlaca().equals(placaFormatada) && carroRepository.existsByPlaca(placaFormatada)) {
            throw new RegraNegocioException("Placa já está cadastrada para outro veículo.");
        }
        Cliente cliente = clienteService.buscarEntidade(dto.getClienteId());
        preencherEntidade(carro, dto, placaFormatada, cliente);
        return toResposta(carroRepository.save(carro));
    }

    @Transactional(readOnly = true)
    public List<CarroDTO.Resposta> listarTodos() {
        return carroRepository.findAll().stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CarroDTO.Resposta> listarPorCliente(Long clienteId) {
        clienteService.buscarEntidade(clienteId);
        return carroRepository.findByClienteId(clienteId).stream()
                .map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CarroDTO.Resposta buscarPorId(Long id) {
        return toResposta(buscarEntidade(id));
    }

    @Transactional
    public void deletar(Long id) {
        Carro carro = buscarEntidade(id);
        if (!carro.getOrdensServico().isEmpty()) {
            throw new RegraNegocioException("Não é possível excluir carro com ordens de serviço vinculadas.");
        }
        carroRepository.deleteById(id);
    }

    public Carro buscarEntidade(Long id) {
        return carroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carro não encontrado com id: " + id));
    }

    private void preencherEntidade(Carro carro, CarroDTO.Requisicao dto, String placa, Cliente cliente) {
        carro.setMarca(dto.getMarca());
        carro.setModelo(dto.getModelo());
        carro.setAno(dto.getAno());
        carro.setPlaca(placa);
        carro.setCor(dto.getCor());
        carro.setChassi(dto.getChassi());
        carro.setCliente(cliente);
    }

    private CarroDTO.Resposta toResposta(Carro c) {
        CarroDTO.Resposta dto = new CarroDTO.Resposta();
        dto.setId(c.getId());
        dto.setMarca(c.getMarca());
        dto.setModelo(c.getModelo());
        dto.setAno(c.getAno());
        dto.setPlaca(c.getPlaca());
        dto.setCor(c.getCor());
        dto.setChassi(c.getChassi());
        dto.setClienteId(c.getCliente().getId());
        dto.setNomeCliente(c.getCliente().getNome());
        return dto;
    }
}
