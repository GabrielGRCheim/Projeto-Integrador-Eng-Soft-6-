package com.senai.automotiva.config;

import com.senai.automotiva.entities.*;
import com.senai.automotiva.enums.PerfilUsuario;
import com.senai.automotiva.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private CarroRepository carroRepository;
    @Autowired private ServicoRepository servicoRepository;
    @Autowired private PecaRepository pecaRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        // Usuário administrador padrão
        if (!usuarioRepository.existsByEmail("admin@senai.br")) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@senai.br");
            admin.setSenha(encoder.encode("admin123"));
            admin.setPerfil(PerfilUsuario.ADMINISTRADOR);
            usuarioRepository.save(admin);
        }

        // Serviços base
        if (servicoRepository.count() == 0) {
            Servico trocaOleo = new Servico();
            trocaOleo.setNome("Troca de Óleo");
            trocaOleo.setDescricao("Troca de óleo do motor com filtro");
            trocaOleo.setPrecoBase(new BigDecimal("80.00"));
            trocaOleo.setTempoEstimadoHoras(0.5);
            servicoRepository.save(trocaOleo);

            Servico alinhamento = new Servico();
            alinhamento.setNome("Alinhamento e Balanceamento");
            alinhamento.setDescricao("Alinhamento das quatro rodas e balanceamento");
            alinhamento.setPrecoBase(new BigDecimal("120.00"));
            alinhamento.setTempoEstimadoHoras(1.0);
            servicoRepository.save(alinhamento);

            Servico revisao = new Servico();
            revisao.setNome("Revisão Completa");
            revisao.setDescricao("Revisão geral do veículo");
            revisao.setPrecoBase(new BigDecimal("350.00"));
            revisao.setTempoEstimadoHoras(4.0);
            servicoRepository.save(revisao);
        }

        // Peças no estoque
        if (pecaRepository.count() == 0) {
            Peca filtroOleo = new Peca();
            filtroOleo.setNome("Filtro de Óleo Universal");
            filtroOleo.setCodigo("FO-001");
            filtroOleo.setPrecoCusto(new BigDecimal("18.00"));
            filtroOleo.setPrecoVenda(new BigDecimal("35.00"));
            filtroOleo.setQuantidadeEstoque(20);
            filtroOleo.setQuantidadeMinima(5);
            pecaRepository.save(filtroOleo);

            Peca pastilhaFreio = new Peca();
            pastilhaFreio.setNome("Pastilha de Freio Dianteira");
            pastilhaFreio.setCodigo("PF-002");
            pastilhaFreio.setPrecoCusto(new BigDecimal("45.00"));
            pastilhaFreio.setPrecoVenda(new BigDecimal("90.00"));
            pastilhaFreio.setQuantidadeEstoque(3);
            pastilhaFreio.setQuantidadeMinima(4);
            pecaRepository.save(pastilhaFreio);
        }

        // Cliente e carro de exemplo
        if (clienteRepository.count() == 0) {
            Cliente cliente = new Cliente();
            cliente.setNome("João da Silva");
            cliente.setCpf("12345678901");
            cliente.setTelefone("(62) 99999-0001");
            cliente.setEmail("joao@email.com");
            cliente = clienteRepository.save(cliente);

            Carro carro = new Carro();
            carro.setMarca("Chevrolet");
            carro.setModelo("Onix");
            carro.setAno(2021);
            carro.setPlaca("ABC1D23");
            carro.setCor("Prata");
            carro.setCliente(cliente);
            carroRepository.save(carro);
        }
    }
}
