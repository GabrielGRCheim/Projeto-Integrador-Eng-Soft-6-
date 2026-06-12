package com.senai.automotiva.services;

import com.senai.automotiva.dtos.PecaDTO;
import com.senai.automotiva.entities.MovimentacaoEstoque;
import com.senai.automotiva.entities.Peca;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.MovimentacaoEstoqueRepository;
import com.senai.automotiva.repositories.PecaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes de unidade para EstoqueService.
 *
 * Foca nas regras de movimentação de estoque (entrada, saída, estoque insuficiente)
 * e na validação de preços ao cadastrar peças.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Unidade - EstoqueService")
class EstoqueServiceTest {

    @Mock
    private PecaRepository pecaRepository;

    @Mock
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @InjectMocks
    private EstoqueService estoqueService;

    private Peca pecaExistente;

    @BeforeEach
    void configurar() {
        pecaExistente = new Peca();
        pecaExistente.setId(1L);
        pecaExistente.setNome("Filtro de Óleo");
        pecaExistente.setCodigo("FO-001");
        pecaExistente.setPrecoCusto(new BigDecimal("18.00"));
        pecaExistente.setPrecoVenda(new BigDecimal("35.00"));
        pecaExistente.setQuantidadeEstoque(10);
        pecaExistente.setQuantidadeMinima(3);
        pecaExistente.setAtivo(true);
        pecaExistente.setUnidade("UN");
    }

    // ------------------------------------------------------------------
    // TESTE 2A — Entrada de estoque incrementa a quantidade corretamente
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve incrementar o estoque ao registrar uma entrada")
    void deveIncrementarEstoqueComEntrada() {
        // Arrange
        when(pecaRepository.findById(1L)).thenReturn(Optional.of(pecaExistente));
        when(pecaRepository.save(any(Peca.class))).thenAnswer(inv -> inv.getArgument(0));
        when(movimentacaoRepository.save(any(MovimentacaoEstoque.class))).thenAnswer(inv -> inv.getArgument(0));

        PecaDTO.AjusteEstoque ajuste = new PecaDTO.AjusteEstoque();
        ajuste.setTipoMovimentacao("ENTRADA");
        ajuste.setQuantidade(5);
        ajuste.setMotivo("Reposição de estoque");

        // Act
        PecaDTO.Resposta resposta = estoqueService.ajustarEstoque(1L, ajuste);

        // Assert
        assertEquals(15, resposta.getQuantidadeEstoque(),
                "Estoque de 10 + entrada de 5 deve resultar em 15");

        // Verifica que a movimentação foi registrada com os dados corretos
        ArgumentCaptor<MovimentacaoEstoque> captor = ArgumentCaptor.forClass(MovimentacaoEstoque.class);
        verify(movimentacaoRepository, times(1)).save(captor.capture());

        MovimentacaoEstoque movRegistrada = captor.getValue();
        assertEquals("ENTRADA", movRegistrada.getTipoMovimentacao());
        assertEquals(10, movRegistrada.getQuantidadeAnterior());
        assertEquals(15, movRegistrada.getQuantidadePosterior());
    }

    // ------------------------------------------------------------------
    // TESTE 2B — Saída com estoque insuficiente deve lançar exceção
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException ao tentar saída com estoque insuficiente")
    void deveLancarExcecaoEmSaidaComEstoqueInsuficiente() {
        // Arrange — estoque tem 10, tentativa de retirar 15
        when(pecaRepository.findById(1L)).thenReturn(Optional.of(pecaExistente));

        PecaDTO.AjusteEstoque ajuste = new PecaDTO.AjusteEstoque();
        ajuste.setTipoMovimentacao("SAIDA");
        ajuste.setQuantidade(15);
        ajuste.setMotivo("Uso em OS");

        // Act & Assert
        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> estoqueService.ajustarEstoque(1L, ajuste),
                "Deveria lançar exceção por estoque insuficiente"
        );

        assertTrue(excecao.getMessage().toLowerCase().contains("insuficiente"),
                "Mensagem deve indicar estoque insuficiente");

        // Nem estoque nem movimentação devem ter sido alterados
        verify(pecaRepository, never()).save(any(Peca.class));
        verify(movimentacaoRepository, never()).save(any(MovimentacaoEstoque.class));
    }

    // ------------------------------------------------------------------
    // TESTE 2C — Preço de venda menor que custo deve lançar exceção
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException ao cadastrar peça com preço de venda menor que o custo")
    void deveLancarExcecaoQuandoPrecoVendaMenorQueCusto() {
        // Arrange
        PecaDTO.Requisicao dto = new PecaDTO.Requisicao();
        dto.setNome("Pastilha de Freio");
        dto.setCodigo("PF-100");
        dto.setPrecoCusto(new BigDecimal("80.00"));
        dto.setPrecoVenda(new BigDecimal("50.00")); // venda < custo: inválido
        dto.setQuantidadeEstoque(5);

        when(pecaRepository.existsByCodigo("PF-100")).thenReturn(false);

        // Act & Assert
        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> estoqueService.criarPeca(dto),
                "Deveria lançar exceção ao cadastrar com preço de venda inferior ao custo"
        );

        assertTrue(excecao.getMessage().toLowerCase().contains("preço de venda"),
                "Mensagem deve mencionar preço de venda");

        verify(pecaRepository, never()).save(any(Peca.class));
    }

    // ------------------------------------------------------------------
    // TESTE 2D — Tipo de movimentação inválido deve lançar exceção
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException para tipo de movimentação desconhecido")
    void deveLancarExcecaoParaTipoMovimentacaoInvalido() {
        // Arrange
        when(pecaRepository.findById(1L)).thenReturn(Optional.of(pecaExistente));

        PecaDTO.AjusteEstoque ajuste = new PecaDTO.AjusteEstoque();
        ajuste.setTipoMovimentacao("TRANSFERENCIA"); // tipo inválido
        ajuste.setQuantidade(2);
        ajuste.setMotivo("Teste tipo inválido");

        // Act & Assert
        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> estoqueService.ajustarEstoque(1L, ajuste),
                "Deveria lançar exceção para tipo de movimentação não reconhecido"
        );

        assertTrue(excecao.getMessage().toLowerCase().contains("inválido"),
                "Mensagem deve indicar tipo inválido");
    }
}
