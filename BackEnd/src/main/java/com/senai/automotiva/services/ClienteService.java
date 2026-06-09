package com.senai.automotiva.services;

import com.senai.automotiva.dtos.ClienteDTO;
import com.senai.automotiva.entities.Cliente;
import com.senai.automotiva.exceptions.RecursoNaoEncontradoException;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public ClienteDTO.Resposta criar(ClienteDTO.Requisicao dto) {
        String cpfLimpo = dto.getCpf().replaceAll("[^0-9]", "");
        if (clienteRepository.existsByCpf(cpfLimpo)) {
            throw new RegraNegocioException("Já existe um cliente cadastrado com o CPF informado.");
        }
        Cliente cliente = new Cliente();
        preencherEntidade(cliente, dto, cpfLimpo);
        return toResposta(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteDTO.Resposta atualizar(Long id, ClienteDTO.Requisicao dto) {
        Cliente cliente = buscarEntidade(id);
        String cpfLimpo = dto.getCpf().replaceAll("[^0-9]", "");
        if (!cliente.getCpf().equals(cpfLimpo) && clienteRepository.existsByCpf(cpfLimpo)) {
            throw new RegraNegocioException("CPF já está cadastrado para outro cliente.");
        }
        preencherEntidade(cliente, dto, cpfLimpo);
        return toResposta(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO.Resposta> listarTodos() {
        return clienteRepository.findAll().stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO.Resposta> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO.Resposta buscarPorId(Long id) {
        return toResposta(buscarEntidade(id));
    }

    @Transactional
    public void ativarDesativar(Long id) {
        Cliente cliente = buscarEntidade(id);
        cliente.setAtivo(!cliente.getAtivo());
        clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = buscarEntidade(id);
        if (!cliente.getCarros().isEmpty()) {
            throw new RegraNegocioException("Não é possível excluir cliente com carros cadastrados.");
        }
        clienteRepository.deleteById(id);
    }

    public Cliente buscarEntidade(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com id: " + id));
    }

    private void preencherEntidade(Cliente cliente, ClienteDTO.Requisicao dto, String cpfLimpo) {
        cliente.setNome(dto.getNome());
        cliente.setCpf(cpfLimpo);
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        cliente.setEndereco(dto.getEndereco());
    }

    private ClienteDTO.Resposta toResposta(Cliente c) {
        ClienteDTO.Resposta dto = new ClienteDTO.Resposta();
        dto.setId(c.getId());
        dto.setNome(c.getNome());
        dto.setCpf(c.getCpf());
        dto.setTelefone(c.getTelefone());
        dto.setEmail(c.getEmail());
        dto.setEndereco(c.getEndereco());
        dto.setAtivo(c.getAtivo());
        dto.setTotalCarros(c.getCarros() != null ? c.getCarros().size() : 0);
        return dto;
    }
}
