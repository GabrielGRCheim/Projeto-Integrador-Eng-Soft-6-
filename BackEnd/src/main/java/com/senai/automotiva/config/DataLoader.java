package com.senai.automotiva.config;

import com.senai.automotiva.entities.*;
import com.senai.automotiva.enums.PerfilUsuario;
import com.senai.automotiva.enums.StatusOrdemServico;
import com.senai.automotiva.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Carrega dados iniciais no banco H2 ao subir a aplicação.
 *
 * Dados criados:
 *  - 3 usuários (admin, mecânico, atendente)
 *  - 3 clientes
 *  - 3 carros (um por cliente)
 *  - 3 serviços
 *  - 3 peças no estoque
 *  - 10 ordens de serviço com diferentes status e itens vinculados
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private UsuarioRepository     usuarioRepository;
    @Autowired private ClienteRepository     clienteRepository;
    @Autowired private CarroRepository       carroRepository;
    @Autowired private ServicoRepository     servicoRepository;
    @Autowired private PecaRepository        pecaRepository;
    @Autowired private OrdemServicoRepository ordemServicoRepository;
    @Autowired private ItemOrdemServicoRepository itemRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) return; // idempotente

        // ─────────────────────────────────────────────────────────────
        // USUÁRIOS
        // ─────────────────────────────────────────────────────────────
        Usuario admin = novoUsuario("Carlos Administrador", "admin@senai.br",
                "admin123", PerfilUsuario.ADMINISTRADOR);

        Usuario mecanico = novoUsuario("Roberto Mecânico", "mecanico@senai.br",
                "mec123", PerfilUsuario.ADMINISTRADOR);

        Usuario atendente = novoUsuario("Fernanda Atendente", "atendente@senai.br",
                "ate123", PerfilUsuario.ADMINISTRADOR);

        // ─────────────────────────────────────────────────────────────
        // CLIENTES
        // ─────────────────────────────────────────────────────────────
        Cliente joao = novoCliente("João da Silva",     "12345678901",
                "(62) 99999-0001", "joao@email.com",    "Rua das Acácias, 10 — Goiânia/GO");

        Cliente maria = novoCliente("Maria Oliveira",   "98765432100",
                "(62) 98888-0002", "maria@email.com",   "Av. Goiás, 500 — Anápolis/GO");

        Cliente pedro = novoCliente("Pedro Almeida",    "45678912300",
                "(62) 97777-0003", "pedro@email.com",   "Rua dos Ipês, 75 — Aparecida de Goiânia/GO");

        // ─────────────────────────────────────────────────────────────
        // CARROS
        // ─────────────────────────────────────────────────────────────
        Carro onix = novoCarro("Chevrolet", "Onix",   2021, "ABC1D23", "Prata", 45_000, "M917402PLW917VT09" , joao);
        Carro hb20 = novoCarro("Hyundai",  "HB20",   2020, "DEF2E34", "Branco", 62_000, "1KUT938BK726LW09P" ,maria);
        Carro gol  = novoCarro("Volkswagen","Gol",    2019, "GHI3F45", "Preto",  88_500, "PIO0917BMR8259BTA" ,pedro);

        // ─────────────────────────────────────────────────────────────
        // SERVIÇOS
        // ─────────────────────────────────────────────────────────────
        Servico trocaOleo = novoServico("Troca de Óleo",
                "Troca de óleo do motor com substituição do filtro de óleo",
                new BigDecimal("80.00"), 0.5);

        Servico trocaPastilha = novoServico("Troca de Pastilha de Freio",
                "Substituição das pastilhas de freio dianteiras e/ou traseiras",
                new BigDecimal("150.00"), 1.5);

        Servico alinhamento = novoServico("Alinhamento e Balanceamento",
                "Alinhamento das quatro rodas e balanceamento com pesos",
                new BigDecimal("120.00"), 1.0);

        // ─────────────────────────────────────────────────────────────
        // PEÇAS
        // ─────────────────────────────────────────────────────────────
        Peca filtroOleo = novaPeca("Filtro de Óleo Universal", "FO-001",
                new BigDecimal("18.00"), new BigDecimal("35.00"), 20, 5,
                "Distribuidora AutoPeças");

        Peca pastilhaFreio = novaPeca("Pastilha de Freio Dianteira", "PF-002",
                new BigDecimal("45.00"), new BigDecimal("90.00"), 12, 4,
                "Distribuidora AutoPeças");

        Peca oleoMotor = novaPeca("Óleo Motor 5W30 Sintético (1L)", "OM-003",
                new BigDecimal("28.00"), new BigDecimal("55.00"), 30, 8,
                "Lubrificantes Brasil");

        // ─────────────────────────────────────────────────────────────
        // ORDENS DE SERVIÇO (10 ao total com status variados)
        // ─────────────────────────────────────────────────────────────

        // OS 1 — Onix do João — CONCLUÍDA — Troca de óleo + filtro + óleo
        OrdemServico os1 = criarOS(onix, mecanico,
                "Veículo com 45.000 km — manutenção preventiva",
                "Troca de óleo e filtro conforme plano de revisão",
                new BigDecimal("80.00"), StatusOrdemServico.CONCLUIDA,
                LocalDateTime.now().minusDays(30));
        adicionarItem(os1, trocaOleo, null, 1, new BigDecimal("80.00"));
        adicionarItem(os1, null, filtroOleo, 1, new BigDecimal("35.00"));
        adicionarItem(os1, null, oleoMotor,  4, new BigDecimal("55.00"));
        recalcularTotal(os1);

        // OS 2 — HB20 da Maria — CONCLUÍDA — Troca de pastilha
        OrdemServico os2 = criarOS(hb20, mecanico,
                "Barulho ao frear — desgaste nas pastilhas",
                "Freio rangendo ao desacelerar",
                new BigDecimal("150.00"), StatusOrdemServico.CONCLUIDA,
                LocalDateTime.now().minusDays(20));
        adicionarItem(os2, trocaPastilha, null, 1, new BigDecimal("150.00"));
        adicionarItem(os2, null, pastilhaFreio, 2, new BigDecimal("90.00"));
        recalcularTotal(os2);

        // OS 3 — Gol do Pedro — CONCLUÍDA — Alinhamento
        OrdemServico os3 = criarOS(gol, mecanico,
                "Direção puxando para a direita",
                "Volante desalinhado e pneus com desgaste irregular",
                new BigDecimal("120.00"), StatusOrdemServico.CONCLUIDA,
                LocalDateTime.now().minusDays(15));
        adicionarItem(os3, alinhamento, null, 1, new BigDecimal("120.00"));
        recalcularTotal(os3);

        // OS 4 — Onix do João — CONCLUÍDA — Troca óleo + alinhamento (revisão completa)
        OrdemServico os4 = criarOS(onix, mecanico,
                "Revisão dos 45.000 km — óleo + geometria",
                "Solicitou revisão completa preventiva",
                new BigDecimal("200.00"), StatusOrdemServico.CONCLUIDA,
                LocalDateTime.now().minusDays(10));
        adicionarItem(os4, trocaOleo,   null, 1, new BigDecimal("80.00"));
        adicionarItem(os4, alinhamento, null, 1, new BigDecimal("120.00"));
        adicionarItem(os4, null, filtroOleo, 1, new BigDecimal("35.00"));
        adicionarItem(os4, null, oleoMotor,  4, new BigDecimal("55.00"));
        recalcularTotal(os4);

        // OS 5 — HB20 da Maria — CONCLUÍDA — Pastilha traseira
        OrdemServico os5 = criarOS(hb20, mecanico,
                "Pastilhas traseiras no limite de desgaste",
                "Pastilhas dianteiras OK, traseiras precisam trocar",
                new BigDecimal("150.00"), StatusOrdemServico.CONCLUIDA,
                LocalDateTime.now().minusDays(5));
        adicionarItem(os5, trocaPastilha, null, 1, new BigDecimal("150.00"));
        adicionarItem(os5, null, pastilhaFreio, 2, new BigDecimal("90.00"));
        recalcularTotal(os5);

        // OS 6 — Gol do Pedro — EM_ANDAMENTO — Troca de óleo em execução
        OrdemServico os6 = criarOS(gol, mecanico,
                "Troca de óleo em andamento",
                "Veículo com cheiro de queimado — suspeita de vazamento",
                new BigDecimal("80.00"), StatusOrdemServico.EM_ANDAMENTO, null);
        adicionarItem(os6, trocaOleo, null, 1, new BigDecimal("80.00"));
        adicionarItem(os6, null, oleoMotor, 4, new BigDecimal("55.00"));
        recalcularTotal(os6);

        // OS 7 — Onix do João — AGUARDANDO_PECA — aguarda filtro
        OrdemServico os7 = criarOS(onix, mecanico,
                "Aguardando chegada do filtro de ar",
                "Filtro de ar entupido — veículo perdendo potência",
                new BigDecimal("50.00"), StatusOrdemServico.AGUARDANDO_PECA, null);
        recalcularTotal(os7);

        // OS 8 — HB20 da Maria — ABERTA — recém-aberta
        OrdemServico os8 = criarOS(hb20, atendente,
                "Barulho na suspensão dianteira",
                "Ouve barulho ao passar por lombada",
                new BigDecimal("0.00"), StatusOrdemServico.ABERTA, null);
        recalcularTotal(os8);

        // OS 9 — Gol do Pedro — ABERTA — alinhamento solicitado
        OrdemServico os9 = criarOS(gol, atendente,
                "Alinhamento solicitado pelo cliente",
                "Carro desviando para a esquerda após trocar pneus",
                new BigDecimal("120.00"), StatusOrdemServico.ABERTA, null);
        adicionarItem(os9, alinhamento, null, 1, new BigDecimal("120.00"));
        recalcularTotal(os9);

        // OS 10 — Onix do João — CANCELADA
        OrdemServico os10 = criarOS(onix, atendente,
                "Cliente desistiu do serviço",
                "Recall do fabricante — cliente optou pela concessionária",
                new BigDecimal("0.00"), StatusOrdemServico.CANCELADA, null);
        recalcularTotal(os10);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Métodos auxiliares de criação
    // ─────────────────────────────────────────────────────────────────────────

    private Usuario novoUsuario(String nome, String email, String senha, PerfilUsuario perfil) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha(encoder.encode(senha));
        u.setPerfil(perfil);
        u.setAtivo(true);
        return usuarioRepository.save(u);
    }

    private Cliente novoCliente(String nome, String cpf, String tel, String email, String end) {
        Cliente c = new Cliente();
        c.setNome(nome);
        c.setCpf(cpf);
        c.setTelefone(tel);
        c.setEmail(email);
        c.setEndereco(end);
        c.setAtivo(true);
        return clienteRepository.save(c);
    }

    private Carro novoCarro(String marca, String modelo, int ano, String placa,
                             String cor, int km,String chassi, Cliente cliente) {
        Carro c = new Carro();
        c.setMarca(marca);
        c.setModelo(modelo);
        c.setAno(ano);
        c.setPlaca(placa);
        c.setCor(cor);
        c.setQuilometragem(km);
        c.setChassi(chassi);
        c.setCliente(cliente);
        return carroRepository.save(c);
    }

    private Servico novoServico(String nome, String desc, BigDecimal preco, double horas) {
        Servico s = new Servico();
        s.setNome(nome);
        s.setDescricao(desc);
        s.setPrecoBase(preco);
        s.setTempoEstimadoHoras(horas);
        s.setAtivo(true);
        return servicoRepository.save(s);
    }

    private Peca novaPeca(String nome, String codigo, BigDecimal custo, BigDecimal venda,
                           int qtd, int qtdMin, String fornecedor) {
        Peca p = new Peca();
        p.setNome(nome);
        p.setCodigo(codigo);
        p.setPrecoCusto(custo);
        p.setPrecoVenda(venda);
        p.setQuantidadeEstoque(qtd);
        p.setQuantidadeMinima(qtdMin);
        p.setFornecedor(fornecedor);
        p.setUnidade("UN");
        p.setAtivo(true);
        return pecaRepository.save(p);
    }

    private OrdemServico criarOS(Carro carro, Usuario responsavel, String diagnostico,
                                  String queixa, BigDecimal maoObra,
                                  StatusOrdemServico status, LocalDateTime concluidoEm) {
        OrdemServico os = new OrdemServico();
        os.setCarro(carro);
        os.setResponsavel(responsavel);
        os.setDiagnostico(diagnostico);
        os.setQueixaCliente(queixa);
        os.setValorMaoObra(maoObra);
        os.setValorTotal(maoObra);
        os.setStatus(status);
        os.setConcluidoEm(concluidoEm);
        return ordemServicoRepository.save(os);
    }

    private void adicionarItem(OrdemServico os, Servico servico, Peca peca,
                                int quantidade, BigDecimal valorUnitario) {
        ItemOrdemServico item = new ItemOrdemServico();
        item.setOrdemServico(os);
        item.setServico(servico);
        item.setPeca(peca);
        item.setQuantidade(quantidade);
        item.setValorUnitario(valorUnitario);
        item.calcularValorTotal();
        itemRepository.save(item);
    }

    private void recalcularTotal(OrdemServico os) {
        BigDecimal totalItens = itemRepository.findByOrdemServicoId(os.getId())
                .stream()
                .map(ItemOrdemServico::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        os.setValorTotal(totalItens.add(os.getValorMaoObra()));
        ordemServicoRepository.save(os);
    }
}
