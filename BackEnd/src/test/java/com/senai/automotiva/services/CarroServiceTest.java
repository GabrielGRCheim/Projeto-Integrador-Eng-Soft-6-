package com.senai.automotiva.services;

import com.senai.automotiva.dtos.CarroDTO;
import com.senai.automotiva.entities.Carro;
import com.senai.automotiva.entities.Cliente;
import com.senai.automotiva.entities.OrdemServico;
import com.senai.automotiva.exceptions.RegraNegocioException;
import com.senai.automotiva.repositories.CarroRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Testes de unidade para CarroService.
 *
 * Valida o cadastro de veículos com normalização de placa,
 * restrições de duplicidade e regras de exclusão.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Unidade - CarroService")
class CarroServiceTest {

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CarroService carroService;

    private Cliente clienteAtivo;
    private Cliente clienteInativo;
    private Carro carroSalvo;

    @BeforeEach
    void configurar() {
        clienteAtivo = new Cliente();
        clienteAtivo.setId(1L);
        clienteAtivo.setNome("Ana Lima");
        clienteAtivo.setAtivo(true);
        clienteAtivo.setCarros(new ArrayList<>());

        clienteInativo = new Cliente();
        clienteInativo.setId(2L);
        clienteInativo.setNome("Carlos Souza");
        clienteInativo.setAtivo(false);
        clienteInativo.setCarros(new ArrayList<>());

        carroSalvo = new Carro();
        carroSalvo.setId(1L);
        carroSalvo.setMarca("Toyota");
        carroSalvo.setModelo("Corolla");
        carroSalvo.setAno(2022);
        carroSalvo.setPlaca("TYT1A23");
        carroSalvo.setCor("Branco");
        carroSalvo.setCliente(clienteAtivo);
        carroSalvo.setOrdensServico(new ArrayList<>());
    }

    // ------------------------------------------------------------------
    // TESTE 4A — Cadastro com sucesso e normalização da placa (maiúsculo)
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve cadastrar carro com sucesso e converter placa para maiúsculo sem caracteres especiais")
    void deveCadastrarCarroNormalizandoPlaca() {
        // Arrange — placa enviada em minúsculo com hífen (formato Mercosul)
        CarroDTO.RequisicaoCarro dto = new CarroDTO.RequisicaoCarro();
        dto.setMarca("Toyota");
        dto.setModelo("Corolla");
        dto.setAno(2022);
        dto.setPlaca("tyt-1a23"); // entrada com hífen e minúsculas
        dto.setCor("Branco");
        dto.setClienteId(1L);

        when(clienteService.buscarEntidade(1L)).thenReturn(clienteAtivo);
        when(carroRepository.existsByPlaca("TYT1A23")).thenReturn(false);
        when(carroRepository.save(any(Carro.class))).thenReturn(carroSalvo);

        CarroDTO.RespostaCarro respostaCarro = carroService.criar(dto);

        assertNotNull(respostaCarro);
        assertEquals("TYT1A23", respostaCarro.getPlaca(),
                "Placa deve estar em maiúsculo e sem caracteres especiais");
        assertEquals("Ana Lima", respostaCarro.getNomeCliente(),
                "Nome do cliente deve estar vinculado ao carro");

        verify(carroRepository, times(1)).save(any(Carro.class));

    }

    // ------------------------------------------------------------------
    // TESTE 4B — Placa duplicada deve impedir o cadastro
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException ao tentar cadastrar carro com placa já existente")
    void deveLancarExcecaoParaPlacaDuplicada() {
        // Arrange
        CarroDTO.RequisicaoCarro dto = new CarroDTO.RequisicaoCarro();
        dto.setMarca("Honda");
        dto.setModelo("Civic");
        dto.setAno(2020);
        dto.setPlaca("TYT1A23"); // mesma placa já cadastrada
        dto.setClienteId(1L);

        when(carroRepository.existsByPlaca("TYT1A23")).thenReturn(true);

        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> carroService.criar(dto),
                "Deveria lançar exceção para placa duplicada"
        );

        assertTrue(excecao.getMessage().contains("TYT1A23"),
                "Mensagem de erro deve conter a placa duplicada");

        verify(carroRepository, never()).save(any(Carro.class));
    }

    // ------------------------------------------------------------------
    // TESTE 4C — Não cadastrar carro para cliente inativo
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException ao tentar cadastrar carro para cliente inativo")
    void deveLancarExcecaoParaClienteInativo() {
        // Arrange
        CarroDTO.RequisicaoCarro dto = new CarroDTO.RequisicaoCarro();
        dto.setMarca("Fiat");
        dto.setModelo("Palio");
        dto.setAno(2019);
        dto.setPlaca("FIA9Z99");
        dto.setClienteId(2L);

        when(clienteService.buscarEntidade(2L)).thenReturn(clienteInativo);
        when(carroRepository.existsByPlaca("FIA9Z99")).thenReturn(false);

        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> carroService.criar(dto),
                "Deveria lançar exceção para cliente inativo"
        );

        assertTrue(excecao.getMessage().toLowerCase().contains("inativo"),
                "Mensagem deve indicar que o cliente está inativo");

        verify(carroRepository, never()).save(any(Carro.class));
    }

    // ------------------------------------------------------------------
    // TESTE 4D — Não deve excluir carro com ordens de serviço vinculadas
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar RegraNegocioException ao tentar excluir carro com ordens de serviço")
    void deveLancarExcecaoAoDeletarCarroComOrdensServico() {
        // Arrange — carro com 1 OS vinculada
        OrdemServico os = new OrdemServico();
        os.setId(100L);
        carroSalvo.setOrdensServico(List.of(os));

        when(carroRepository.findById(1L)).thenReturn(Optional.of(carroSalvo));

        RegraNegocioException excecao = assertThrows(
                RegraNegocioException.class,
                () -> carroService.deletar(1L),
                "Deveria lançar exceção ao deletar carro com OS vinculada"
        );

        assertTrue(excecao.getMessage().toLowerCase().contains("ordens"),
                "Mensagem deve mencionar ordens de serviço");

        verify(carroRepository, never()).deleteById(anyLong());
    }
}
