package com.senai.automotiva.services;

import com.senai.automotiva.dtos.ClienteDTO;
import com.senai.automotiva.entities.Carro;
import com.senai.automotiva.entities.Cliente;
import com.senai.automotiva.exceptions.RecursoNaoEncontradoException;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes de unidade para ClienteService.
 *
 * Utiliza Mockito para isolar o service dos seus repositórios,
 * testando exclusivamente as regras de negócio da camada de serviço.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Unidade - ClienteService")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteDTO.Requisicao requisicaoValida;
    private Cliente clienteSalvo;

    @BeforeEach
    void configurar() {
        // DTO de entrada com dados válidos reutilizado nos testes
        requisicaoValida = new ClienteDTO.Requisicao();
        requisicaoValida.setNome("Maria Oliveira");
        requisicaoValida.setCpf("529.982.247-25"); // CPF formatado (com máscara)
        requisicaoValida.setTelefone("(62) 98888-1111");
        requisicaoValida.setEmail("maria@email.com");
        requisicaoValida.setEndereco("Rua das Flores, 100");

        // Entidade que simula o retorno do banco após o save()
        clienteSalvo = new Cliente();
        clienteSalvo.setId(1L);
        clienteSalvo.setNome("Maria Oliveira");
        clienteSalvo.setCpf("52998224725"); // CPF limpo (somente dígitos)
        clienteSalvo.setTelefone("(62) 98888-1111");
        clienteSalvo.setEmail("maria@email.com");
        clienteSalvo.setEndereco("Rua das Flores, 100");
        clienteSalvo.setAtivo(true);
        clienteSalvo.setCarros(new ArrayList<>());
    }

    // ------------------------------------------------------------------
    // TESTE 1A — Cenário de sucesso: criar cliente com CPF formatado
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve criar cliente com sucesso e limpar a máscara do CPF")
    void deveCriarClienteComSucesso() {
        // Arrange
        when(clienteRepository.existsByCpf("52998224725")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        // Act
        ClienteDTO.Resposta resposta = clienteService.criar(requisicaoValida);

        // Assert
        assertNotNull(resposta, "A resposta não deve ser nula");
        assertEquals(1L, resposta.getId());
        assertEquals("Maria Oliveira", resposta.getNome());
        assertEquals("52998224725", resposta.getCpf()); // CPF deve estar limpo
        assertTrue(resposta.getAtivo());

        // Verifica que o repositório foi chamado uma vez para salvar
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    // ------------------------------------------------------------------
    // TESTE 1B — Regra de negócio: CPF duplicado deve lançar exceção
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException ao tentar cadastrar CPF já existente")
    void deveLancarExcecaoAoCriarClienteComCpfDuplicado() {
        // Arrange — simula que o CPF já está na base
        when(clienteRepository.existsByCpf("52998224725")).thenReturn(true);

        // Act & Assert
        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> clienteService.criar(requisicaoValida),
                "Deveria lançar RegraNegocioException para CPF duplicado"
        );

        assertTrue(excecao.getMessage().contains("CPF"),
                "A mensagem de erro deve mencionar CPF");

        // Garante que save() nunca foi chamado quando a regra falha
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    // ------------------------------------------------------------------
    // TESTE 1C — Regra de negócio: deletar cliente com carros vinculados
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException ao tentar excluir cliente com carros cadastrados")
    void deveLancarExcecaoAoDeletarClienteComCarros() {
        // Arrange — cliente possui 1 carro vinculado
        Carro carro = new Carro();
        carro.setId(10L);
        carro.setPlaca("ABC1D23");
        clienteSalvo.setCarros(List.of(carro));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteSalvo));

        // Act & Assert
        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> clienteService.deletar(1L),
                "Deveria impedir exclusão de cliente com carros"
        );

        assertTrue(excecao.getMessage().toLowerCase().contains("carro"),
                "A mensagem deve indicar que há carros cadastrados");

        verify(clienteRepository, never()).deleteById(anyLong());
    }

    // ------------------------------------------------------------------
    // TESTE 1D — Recurso não encontrado ao buscar por ID inexistente
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao buscar cliente com ID inexistente")
    void deveLancarExcecaoAoBuscarClienteInexistente() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNaoEncontradoException excecao = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> clienteService.buscarPorId(99L),
                "Deveria lançar RecursoNaoEncontradoException para ID inexistente"
        );

        assertTrue(excecao.getMessage().contains("99"),
                "A mensagem deve indicar o ID não encontrado");
    }
}
