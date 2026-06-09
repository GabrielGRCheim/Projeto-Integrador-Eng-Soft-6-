package com.senai.automotiva.services;

import com.senai.automotiva.dtos.ItemOrdemServicoDTO;
import com.senai.automotiva.dtos.OrdemServicoDTO;
import com.senai.automotiva.entities.*;
import com.senai.automotiva.enums.StatusOrdemServico;
import com.senai.automotiva.exceptions.RecursoNaoEncontradoException;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.ItemOrdemServicoRepository;
import com.senai.automotiva.repositories.OrdemServicoRepository;
import com.senai.automotiva.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ItemOrdemServicoRepository itemRepository;

    @Autowired
    private CarroService carroService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private EstoqueService estoqueService;

    @Transactional
    public OrdemServicoDTO.Resposta criar(OrdemServicoDTO.Requisicao dto) {
        Carro carro = carroService.buscarEntidade(dto.getCarroId());
        OrdemServico os = new OrdemServico();
        os.setCarro(carro);
        os.setDescricao(dto.getDescricao());
        os.setValorMaoObra(dto.getValorMaoObra() != null ? dto.getValorMaoObra() : BigDecimal.ZERO);
        os.setStatus(StatusOrdemServico.ABERTA);

        if (dto.getResponsavelId() != null) {
            Usuario responsavel = usuarioRepository.findById(dto.getResponsavelId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Responsável não encontrado."));
            os.setResponsavel(responsavel);
        }
        return toResposta(ordemServicoRepository.save(os));
    }

    @Transactional
    public OrdemServicoDTO.Resposta atualizar(Long id, OrdemServicoDTO.Requisicao dto) {
        OrdemServico os = buscarEntidade(id);
        validarEdicao(os);
        os.setDescricao(dto.getDescricao());
        os.setValorMaoObra(dto.getValorMaoObra() != null ? dto.getValorMaoObra() : BigDecimal.ZERO);
        if (dto.getResponsavelId() != null) {
            Usuario responsavel = usuarioRepository.findById(dto.getResponsavelId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Responsável não encontrado."));
            os.setResponsavel(responsavel);
        }
        recalcularTotal(os);
        return toResposta(ordemServicoRepository.save(os));
    }

    @Transactional
    public OrdemServicoDTO.Resposta atualizarStatus(Long id, StatusOrdemServico novoStatus) {
        OrdemServico os = buscarEntidade(id);
        validarTransicaoStatus(os.getStatus(), novoStatus);
        os.setStatus(novoStatus);
        if (novoStatus == StatusOrdemServico.CONCLUIDA) {
            os.setConcluidoEm(LocalDateTime.now());
        }
        return toResposta(ordemServicoRepository.save(os));
    }

    @Transactional
    public ItemOrdemServicoDTO.Resposta adicionarItem(ItemOrdemServicoDTO.Requisicao dto) {
        OrdemServico os = buscarEntidade(dto.getOrdemServicoId());
        validarEdicao(os);

        ItemOrdemServico item = new ItemOrdemServico();
        item.setOrdemServico(os);
        item.setQuantidade(dto.getQuantidade());
        item.setValorUnitario(dto.getValorUnitario());
        item.setObservacao(dto.getObservacao());

        if (dto.getServicoId() != null) {
            Servico servico = servicoService.buscarEntidade(dto.getServicoId());
            item.setServico(servico);
        }

        if (dto.getPecaId() != null) {
            Peca peca = estoqueService.buscarEntidade(dto.getPecaId());
            estoqueService.baixarEstoqueParaOs(peca, dto.getQuantidade(), os.getId());
            item.setPeca(peca);
        }

        item.calcularValorTotal();
        ItemOrdemServico salvo = itemRepository.save(item);
        recalcularTotal(os);
        ordemServicoRepository.save(os);
        return toItemResposta(salvo);
    }

    @Transactional
    public void removerItem(Long itemId) {
        ItemOrdemServico item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado com id: " + itemId));
        validarEdicao(item.getOrdemServico());
        OrdemServico os = item.getOrdemServico();
        itemRepository.deleteById(itemId);
        recalcularTotal(os);
        ordemServicoRepository.save(os);
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoDTO.Resposta> listarTodas() {
        return ordemServicoRepository.findAll().stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoDTO.Resposta> listarAbertas() {
        return ordemServicoRepository.findOrdensAbertas().stream().map(this::toResposta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrdemServicoDTO.Resposta buscarPorId(Long id) {
        return toResposta(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoDTO.Resposta> listarPorCliente(Long clienteId) {
        return ordemServicoRepository.findByCarroClienteId(clienteId)
                .stream().map(this::toResposta).collect(Collectors.toList());
    }

    private void recalcularTotal(OrdemServico os) {
        List<ItemOrdemServico> itens = itemRepository.findByOrdemServicoId(os.getId());
        BigDecimal totalItens = itens.stream()
                .map(ItemOrdemServico::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        os.setValorTotal(totalItens.add(os.getValorMaoObra()));
    }

    private void validarEdicao(OrdemServico os) {
        if (os.getStatus() == StatusOrdemServico.CONCLUIDA) {
            throw new RegraNegocioException("Não é permitido alterar uma ordem de serviço concluída.");
        }
        if (os.getStatus() == StatusOrdemServico.CANCELADA) {
            throw new RegraNegocioException("Não é permitido alterar uma ordem de serviço cancelada.");
        }
    }

    private void validarTransicaoStatus(StatusOrdemServico atual, StatusOrdemServico novo) {
        if (atual == StatusOrdemServico.CONCLUIDA || atual == StatusOrdemServico.CANCELADA) {
            throw new RegraNegocioException("Não é possível alterar o status de uma OS já " + atual.name().toLowerCase() + ".");
        }
        if (novo == StatusOrdemServico.ABERTA && atual != StatusOrdemServico.ABERTA) {
            throw new RegraNegocioException("Não é possível reabrir uma OS que já passou para o status: " + atual.name());
        }
    }

    public OrdemServico buscarEntidade(Long id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada com id: " + id));
    }

    private OrdemServicoDTO.Resposta toResposta(OrdemServico os) {
        OrdemServicoDTO.Resposta dto = new OrdemServicoDTO.Resposta();
        dto.setId(os.getId());
        dto.setNumeroOs(os.getNumeroOs());
        dto.setCarroId(os.getCarro().getId());
        dto.setPlacaCarro(os.getCarro().getPlaca());
        dto.setModeloCarro(os.getCarro().getMarca() + " " + os.getCarro().getModelo());
        dto.setNomeCliente(os.getCarro().getCliente().getNome());
        dto.setStatus(os.getStatus());
        dto.setDescricao(os.getDescricao());
        dto.setValorMaoObra(os.getValorMaoObra());
        dto.setValorTotal(os.getValorTotal());
        dto.setCriadoEm(os.getCriadoEm() != null ? os.getCriadoEm().toString() : null);
        dto.setConcluidoEm(os.getConcluidoEm() != null ? os.getConcluidoEm().toString() : null);
        if (os.getResponsavel() != null) {
            dto.setNomeResponsavel(os.getResponsavel().getNome());
        }
        List<ItemOrdemServicoDTO.Resposta> itens = itemRepository.findByOrdemServicoId(os.getId())
                .stream().map(this::toItemResposta).collect(Collectors.toList());
        dto.setItens(itens);
        return dto;
    }

    private ItemOrdemServicoDTO.Resposta toItemResposta(ItemOrdemServico item) {
        ItemOrdemServicoDTO.Resposta dto = new ItemOrdemServicoDTO.Resposta();
        dto.setId(item.getId());
        dto.setOrdemServicoId(item.getOrdemServico().getId());
        dto.setQuantidade(item.getQuantidade());
        dto.setValorUnitario(item.getValorUnitario());
        dto.setValorTotal(item.getValorTotal());
        dto.setObservacao(item.getObservacao());
        if (item.getServico() != null) {
            dto.setServicoId(item.getServico().getId());
            dto.setNomeServico(item.getServico().getNome());
        }
        if (item.getPeca() != null) {
            dto.setPecaId(item.getPeca().getId());
            dto.setNomePeca(item.getPeca().getNome());
        }
        return dto;
    }
}
