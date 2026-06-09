package com.senai.automotiva.services;

import com.senai.automotiva.dtos.PecaDTO;
import com.senai.automotiva.entities.MovimentacaoEstoque;
import com.senai.automotiva.entities.Peca;
import com.senai.automotiva.exceptions.RecursoNaoEncontradoException;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.MovimentacaoEstoqueRepository;
import com.senai.automotiva.repositories.PecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @Transactional
    public PecaDTO.Resposta criarPeca(PecaDTO.Requisicao dto) {
        if (dto.getCodigo() != null && !dto.getCodigo().isBlank() && pecaRepository.existsByCodigo(dto.getCodigo())) {
            throw new RegraNegocioException("Já existe uma peça cadastrada com o código: " + dto.getCodigo());
        }
        if (dto.getPrecoVenda().compareTo(dto.getPrecoCusto()) < 0) {
            throw new RegraNegocioException("Preço de venda não pode ser menor que o preço de custo.");
        }
        Peca peca = new Peca();
        preencherEntidade(peca, dto);
        return toResposta(pecaRepository.save(peca));
    }

    @Transactional
    public PecaDTO.Resposta atualizarPeca(Long id, PecaDTO.Requisicao dto) {
        Peca peca = buscarEntidade(id);
        if (dto.getCodigo() != null && !dto.getCodigo().isBlank()
                && !dto.getCodigo().equals(peca.getCodigo())
                && pecaRepository.existsByCodigo(dto.getCodigo())) {
            throw new RegraNegocioException("Código já está em uso por outra peça.");
        }
        if (dto.getPrecoVenda().compareTo(dto.getPrecoCusto()) < 0) {
            throw new RegraNegocioException("Preço de venda não pode ser menor que o preço de custo.");
        }
        preencherEntidade(peca, dto);
        return toResposta(pecaRepository.save(peca));
    }

    @Transactional(readOnly = true)
    public List<PecaDTO.Resposta> listarTodas() {
        return pecaRepository.findAll().stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PecaDTO.Resposta> listarComEstoqueBaixo() {
        return pecaRepository.findPecasComEstoqueBaixo().stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PecaDTO.Resposta buscarPorId(Long id) {
        return toResposta(buscarEntidade(id));
    }

    @Transactional
    public PecaDTO.Resposta ajustarEstoque(Long pecaId, PecaDTO.AjusteEstoque dto) {
        Peca peca = buscarEntidade(pecaId);
        int quantidadeAnterior = peca.getQuantidadeEstoque();

        switch (dto.getTipoMovimentacao().toUpperCase()) {
            case "ENTRADA":
                peca.setQuantidadeEstoque(quantidadeAnterior + dto.getQuantidade());
                break;
            case "SAIDA":
                if (peca.getQuantidadeEstoque() < dto.getQuantidade()) {
                    throw new RegraNegocioException("Estoque insuficiente. Disponível: " + peca.getQuantidadeEstoque());
                }
                peca.setQuantidadeEstoque(quantidadeAnterior - dto.getQuantidade());
                break;
            case "AJUSTE":
                peca.setQuantidadeEstoque(dto.getQuantidade());
                break;
            default:
                throw new RegraNegocioException("Tipo de movimentação inválido: " + dto.getTipoMovimentacao());
        }

        pecaRepository.save(peca);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setPeca(peca);
        movimentacao.setTipoMovimentacao(dto.getTipoMovimentacao().toUpperCase());
        movimentacao.setQuantidade(dto.getQuantidade());
        movimentacao.setQuantidadeAnterior(quantidadeAnterior);
        movimentacao.setQuantidadePosterior(peca.getQuantidadeEstoque());
        movimentacao.setMotivo(dto.getMotivo());
        movimentacaoRepository.save(movimentacao);

        return toResposta(peca);
    }

    @Transactional
    public void ativarDesativar(Long id) {
        Peca peca = buscarEntidade(id);
        peca.setAtivo(!peca.getAtivo());
        pecaRepository.save(peca);
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidade(id);
        pecaRepository.deleteById(id);
    }

    public Peca buscarEntidade(Long id) {
        return pecaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada com id: " + id));
    }

    // Método interno usado pelo OrdemServicoService
    @Transactional
    public void baixarEstoqueParaOs(Peca peca, int quantidade, Long ordemServicoId) {
        if (peca.getQuantidadeEstoque() < quantidade) {
            throw new RegraNegocioException(
                    "Estoque insuficiente para a peça '" + peca.getNome() + "'. Disponível: " + peca.getQuantidadeEstoque());
        }
        int quantidadeAnterior = peca.getQuantidadeEstoque();
        peca.setQuantidadeEstoque(quantidadeAnterior - quantidade);
        pecaRepository.save(peca);

        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setPeca(peca);
        mov.setTipoMovimentacao("SAIDA");
        mov.setQuantidade(quantidade);
        mov.setQuantidadeAnterior(quantidadeAnterior);
        mov.setQuantidadePosterior(peca.getQuantidadeEstoque());
        mov.setMotivo("Utilização em Ordem de Serviço");
        movimentacaoRepository.save(mov);
    }

    private void preencherEntidade(Peca peca, PecaDTO.Requisicao dto) {
        peca.setNome(dto.getNome());
        peca.setCodigo(dto.getCodigo());
        peca.setDescricao(dto.getDescricao());
        peca.setPrecoCusto(dto.getPrecoCusto());
        peca.setPrecoVenda(dto.getPrecoVenda());
        peca.setQuantidadeEstoque(dto.getQuantidadeEstoque() != null ? dto.getQuantidadeEstoque() : 0);
        peca.setQuantidadeMinima(dto.getQuantidadeMinima() != null ? dto.getQuantidadeMinima() : 1);
        peca.setUnidade(dto.getUnidade() != null ? dto.getUnidade() : "UN");
        peca.setFornecedor(dto.getFornecedor());
    }

    public PecaDTO.Resposta toResposta(Peca p) {
        PecaDTO.Resposta dto = new PecaDTO.Resposta();
        dto.setId(p.getId());
        dto.setNome(p.getNome());
        dto.setCodigo(p.getCodigo());
        dto.setDescricao(p.getDescricao());
        dto.setPrecoCusto(p.getPrecoCusto());
        dto.setPrecoVenda(p.getPrecoVenda());
        dto.setQuantidadeEstoque(p.getQuantidadeEstoque());
        dto.setQuantidadeMinima(p.getQuantidadeMinima());
        dto.setUnidade(p.getUnidade());
        dto.setFornecedor(p.getFornecedor());
        dto.setAtivo(p.getAtivo());
        dto.setEstoqueBaixo(p.estoqueBaixo());
        return dto;
    }
}
