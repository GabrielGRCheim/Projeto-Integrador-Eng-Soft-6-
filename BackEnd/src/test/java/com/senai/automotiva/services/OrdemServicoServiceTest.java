package com.senai.automotiva.services;

import com.senai.automotiva.dtos.OrdemServicoDTO;
import com.senai.automotiva.entities.Carro;
import com.senai.automotiva.entities.Cliente;
import com.senai.automotiva.entities.OrdemServico;
import com.senai.automotiva.enums.StatusOrdemServico;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.ItemOrdemServicoRepository;
import com.senai.automotiva.repositories.OrdemServicoRepository;
import com.senai.automotiva.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Testes de unidade para OrdemServicoService.
 *
 * Valida as transições de status da OS e as regras que impedem
 * alterações em ordens já concluídas ou canceladas.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Unidade - OrdemServicoService")
class OrdemServicoServiceTest {

    @Mock
    private OrdemServicoRepository ordemServicoRepository;

    @Mock
    private ItemOrdemServicoRepository itemRepository;

    @Mock
    private CarroService carroService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ServicoService servicoService;

    @Mock
    private EstoqueService estoqueService;

    @InjectMocks
    private OrdemServicoService ordemServicoService;

    private OrdemServico osConcluida;
    private OrdemServico osAberta;
    private Carro carro;

    @BeforeEach
    void configurar() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Pedro Santos");
        cliente.setCarros(new ArrayList<>());

        carro = new Carro();
        carro.setId(1L);
        carro.setPlaca("XYZ9W88");
        carro.setMarca("Ford");
        carro.setModelo("Ka");
        carro.setCliente(cliente);

        osAberta = new OrdemServico();
        osAberta.setId(1L);
        osAberta.setNumeroOs("OS1234");
        osAberta.setCarro(carro);
        osAberta.setStatus(StatusOrdemServico.ABERTA);
        osAberta.setValorMaoObra(BigDecimal.ZERO);
        osAberta.setValorTotal(BigDecimal.ZERO);

        osConcluida = new OrdemServico();
        osConcluida.setId(2L);
        osConcluida.setNumeroOs("OS5678");
        osConcluida.setCarro(carro);
        osConcluida.setStatus(StatusOrdemServico.CONCLUIDA);
        osConcluida.setValorMaoObra(new BigDecimal("200.00"));
        osConcluida.setValorTotal(new BigDecimal("500.00"));
    }

    // ------------------------------------------------------------------
    // TESTE 3A — Criar OS vinculando ao carro corretamente
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve criar uma Ordem de Serviço com status ABERTA ao ser criada")
    void deveCriarOrdemServicoComStatusAberta() {
        // Arrange
        OrdemServicoDTO.RequisicaoOrdem dto = new OrdemServicoDTO.RequisicaoOrdem();
        dto.setCarroId(1L);
        dto.setQueixaCliente("Revisão geral do veículo");
        dto.setValorMaoObra(new BigDecimal("150.00"));

        when(carroService.buscarEntidade(1L)).thenReturn(carro);
        when(ordemServicoRepository.save(any(OrdemServico.class))).thenReturn(osAberta);
        when(itemRepository.findByOrdemServicoId(anyLong())).thenReturn(new ArrayList<>());

        // Act
        OrdemServicoDTO.RespostaOrdem respostaOrdem = ordemServicoService.criar(dto);

        // Assert
        assertNotNull(respostaOrdem, "Resposta não deve ser nula");
        assertEquals(StatusOrdemServico.ABERTA, respostaOrdem.getStatus(),
                "Nova OS deve iniciar com status ABERTA");
        assertEquals("XYZ9W88", respostaOrdem.getPlacaCarro(),
                "Placa do carro deve estar correta na resposta");

        verify(ordemServicoRepository, times(1)).save(any(OrdemServico.class));
    }

    // ------------------------------------------------------------------
    // TESTE 3B — Transição de status: ABERTA → EM_ANDAMENTO (válida)
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve atualizar status de ABERTA para EM_ANDAMENTO com sucesso")
    void deveAtualizarStatusDeAbertaParaEmAndamento() {
        // Arrange
        OrdemServico osEmAndamento = new OrdemServico();
        osEmAndamento.setId(1L);
        osEmAndamento.setNumeroOs("OS1234");
        osEmAndamento.setCarro(carro);
        osEmAndamento.setStatus(StatusOrdemServico.EM_ANDAMENTO);
        osEmAndamento.setValorMaoObra(BigDecimal.ZERO);
        osEmAndamento.setValorTotal(BigDecimal.ZERO);

        when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(osAberta));
        when(ordemServicoRepository.save(any(OrdemServico.class))).thenReturn(osEmAndamento);
        when(itemRepository.findByOrdemServicoId(anyLong())).thenReturn(new ArrayList<>());

        // Act
        OrdemServicoDTO.RespostaOrdem respostaOrdem = ordemServicoService.atualizarStatus(1L, StatusOrdemServico.EM_ANDAMENTO);

        // Assert
        assertEquals(StatusOrdemServico.EM_ANDAMENTO, respostaOrdem.getStatus(),
                "Status deve ter sido atualizado para EM_ANDAMENTO");
    }

    // ------------------------------------------------------------------
    // TESTE 3C — Não deve permitir alterar OS já concluída
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException ao tentar alterar status de OS já concluída")
    void deveLancarExcecaoAoAlterarStatusDeOsConcluida() {
        // Arrange
        when(ordemServicoRepository.findById(2L)).thenReturn(Optional.of(osConcluida));

        // Act & Assert
        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> ordemServicoService.atualizarStatus(2L, StatusOrdemServico.EM_ANDAMENTO),
                "Deveria lançar exceção ao tentar modificar OS concluída"
        );

        assertTrue(excecao.getMessage().toLowerCase().contains("conclu"),
                "Mensagem deve indicar que a OS está concluída");

        verify(ordemServicoRepository, never()).save(any(OrdemServico.class));
    }

    // ------------------------------------------------------------------
    // TESTE 3D — Não deve permitir editar OS já cancelada
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException ao tentar atualizar dados de OS cancelada")
    void deveLancarExcecaoAoAtualizarOsCancelada() {
        // Arrange
        OrdemServico osCancelada = new OrdemServico();
        osCancelada.setId(3L);
        osCancelada.setNumeroOs("OS9999");
        osCancelada.setCarro(carro);
        osCancelada.setStatus(StatusOrdemServico.CANCELADA);
        osCancelada.setValorMaoObra(BigDecimal.ZERO);
        osCancelada.setValorTotal(BigDecimal.ZERO);

        when(ordemServicoRepository.findById(3L)).thenReturn(Optional.of(osCancelada));

        OrdemServicoDTO.RequisicaoOrdem dto = new OrdemServicoDTO.RequisicaoOrdem();
        dto.setCarroId(1L);
        dto.setQueixaCliente("Tentativa de edição indevida");

        // Act & Assert
        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> ordemServicoService.atualizar(3L, dto),
                "Deveria lançar exceção ao editar OS cancelada"
        );

        assertTrue(excecao.getMessage().toLowerCase().contains("cancel"),
                "Mensagem deve mencionar que a OS está cancelada");

        verify(ordemServicoRepository, never()).save(any(OrdemServico.class));
    }
}
