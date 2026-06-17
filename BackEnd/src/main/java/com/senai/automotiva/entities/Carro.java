package com.senai.automotiva.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carros")
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(length = 30)
    private String cor;

    @Column(nullable = false, length = 999999)
    private Integer quilometragem;

    @Column(nullable = false, unique = true, length = 17)
    private String chassi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "carro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrdemServico> ordensServico = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getQuilometragem() { return quilometragem; }
    public void setQuilometragem(Integer quilometragem) { this.quilometragem = quilometragem; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getChassi() { return chassi; }
    public void setChassi(String chassi) { this.chassi = chassi; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<OrdemServico> getOrdensServico() { return ordensServico; }
    public void setOrdensServico(List<OrdemServico> ordensServico) { this.ordensServico = ordensServico; }
}
