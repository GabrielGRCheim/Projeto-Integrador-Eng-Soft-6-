package com.senai.automotiva.utils;

/**
 * Define os critérios disponíveis para ordenação das peças do estoque.
 *
 * <ul>
 *   <li>{@link #NOME_CRESCENTE}      – Ordem alfabética A → Z pelo nome da peça.</li>
 *   <li>{@link #NOME_DECRESCENTE}    – Ordem alfabética Z → A pelo nome da peça.</li>
 *   <li>{@link #QUANTIDADE_CRESCENTE} – Menor quantidade em estoque primeiro.</li>
 *   <li>{@link #QUANTIDADE_DECRESCENTE} – Maior quantidade em estoque primeiro.</li>
 * </ul>
 */
public enum CriterioOrdenacao {

    NOME_CRESCENTE("Ordem alfabética (A → Z)"),
    NOME_DECRESCENTE("Ordem alfabética (Z → A)"),
    QUANTIDADE_CRESCENTE("Quantidade em estoque (menor → maior)"),
    QUANTIDADE_DECRESCENTE("Quantidade em estoque (maior → menor)");

    private final String descricao;

    CriterioOrdenacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
