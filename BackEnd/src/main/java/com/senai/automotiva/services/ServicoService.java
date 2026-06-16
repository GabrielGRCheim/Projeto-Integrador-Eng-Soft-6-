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
    public ServicoDTO.RespostaServico criar(ServicoDTO.RequisicaoServico dto) {
        Servico servico = new Servico();
        preencherEntidade(servico, dto);
        return toResposta(servicoRepository.save(servico));
    }

    @Transactional
    public ServicoDTO.RespostaServico atualizar(Long id, ServicoDTO.RequisicaoServico dto) {
        Servico servico = buscarEntidade(id);
        preencherEntidade(servico, dto);
        return toResposta(servicoRepository.save(servico));
    }

    @Transactional(readOnly = true)
    public List<ServicoDTO.RespostaServico> listarTodos() {
        return servicoRepository.findAll().stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicoDTO.RespostaServico> listarAtivos() {
        return servicoRepository.findByAtivo(true).stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicoDTO.RespostaServico buscarPorId(Long id) {
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

    private void preencherEntidade(Servico servico, ServicoDTO.RequisicaoServico dto) {
        servico.setNome(dto.getNome());
        servico.setDescricao(dto.getDescricao());
        servico.setPrecoBase(dto.getPrecoBase());
        servico.setTempoEstimadoHoras(dto.getTempoEstimadoHoras());
    }

    private ServicoDTO.RespostaServico toResposta(Servico s) {
        ServicoDTO.RespostaServico dto = new ServicoDTO.RespostaServico();
        dto.setId(s.getId());
        dto.setNome(s.getNome());
        dto.setDescricao(s.getDescricao());
        dto.setPrecoBase(s.getPrecoBase());
        dto.setTempoEstimadoHoras(s.getTempoEstimadoHoras());
        dto.setAtivo(s.getAtivo());
        return dto;
    }
}
