package com.senai.automotiva.services;

import com.senai.automotiva.dtos.ServicoDTO;
import com.senai.automotiva.entities.Servico;
import com.senai.automotiva.exceptions.RecursoNaoEncontradoException;
import com.senai.automotiva.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Transactional
    public ServicoDTO.Resposta criar(ServicoDTO.Requisicao dto) {
        Servico servico = new Servico();
        preencherEntidade(servico, dto);
        return toResposta(servicoRepository.save(servico));
    }

    @Transactional
    public ServicoDTO.Resposta atualizar(Long id, ServicoDTO.Requisicao dto) {
        Servico servico = buscarEntidade(id);
        preencherEntidade(servico, dto);
        return toResposta(servicoRepository.save(servico));
    }

    @Transactional(readOnly = true)
    public List<ServicoDTO.Resposta> listarTodos() {
        return servicoRepository.findAll().stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicoDTO.Resposta> listarAtivos() {
        return servicoRepository.findByAtivo(true).stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicoDTO.Resposta buscarPorId(Long id) {
        return toResposta(buscarEntidade(id));
    }

    @Transactional
    public void ativarDesativar(Long id) {
        Servico servico = buscarEntidade(id);
        servico.setAtivo(!servico.getAtivo());
        servicoRepository.save(servico);
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidade(id);
        servicoRepository.deleteById(id);
    }

    public Servico buscarEntidade(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado com id: " + id));
    }

    private void preencherEntidade(Servico servico, ServicoDTO.Requisicao dto) {
        servico.setNome(dto.getNome());
        servico.setDescricao(dto.getDescricao());
        servico.setPrecoBase(dto.getPrecoBase());
        servico.setTempoEstimadoHoras(dto.getTempoEstimadoHoras());
    }

    private ServicoDTO.Resposta toResposta(Servico s) {
        ServicoDTO.Resposta dto = new ServicoDTO.Resposta();
        dto.setId(s.getId());
        dto.setNome(s.getNome());
        dto.setDescricao(s.getDescricao());
        dto.setPrecoBase(s.getPrecoBase());
        dto.setTempoEstimadoHoras(s.getTempoEstimadoHoras());
        dto.setAtivo(s.getAtivo());
        return dto;
    }
}
