package com.senai.automotiva.utils;

import com.senai.automotiva.entities.Peca;

import java.util.ArrayList;
import java.util.List;

public class MergeSortEstoque {

    public List<Peca> ordenar(List<Peca> pecas, CriterioOrdenacao criterio) {
        if (pecas == null) {
            throw new IllegalArgumentException("A lista de peças não pode ser nula.");
        }
        if (criterio == null) {
            throw new IllegalArgumentException("O critério de ordenação não pode ser nulo.");
        }
        if (pecas.size() <= 1) {
            return new ArrayList<>(pecas); // já ordenada por definição
        }

        // Trabalha sobre uma cópia para não modificar a lista original
        List<Peca> copia = new ArrayList<>(pecas);
        return mergeSort(copia, criterio);
    }

    // -----------------------------------------------------------------------
    // ETAPA 1 — DIVIDIR
    // Divide recursivamente a lista ao meio até restar sublistas unitárias.
    // -----------------------------------------------------------------------

    private List<Peca> mergeSort(List<Peca> lista, CriterioOrdenacao criterio) {
        // Caso-base: lista com 0 ou 1 elemento já está ordenada
        if (lista.size() <= 1) {
            return lista;
        }

        // Calcula o índice do ponto médio
        int meio = lista.size() / 2;

        // Divide em metade esquerda e metade direita
        List<Peca> esquerda = new ArrayList<>(lista.subList(0, meio));
        List<Peca> direita  = new ArrayList<>(lista.subList(meio, lista.size()));

        // Chamadas recursivas para ordenar cada metade
        esquerda = mergeSort(esquerda, criterio);
        direita  = mergeSort(direita,  criterio);

        // Mescla as duas metades já ordenadas
        return mesclar(esquerda, direita, criterio);
    }

    // -----------------------------------------------------------------------
    // ETAPA 2 — MESCLAR (MERGE)
    // Combina duas sublistas ordenadas em uma única lista ordenada.
    // -----------------------------------------------------------------------

    private List<Peca> mesclar(List<Peca> esquerda, List<Peca> direita, CriterioOrdenacao criterio) {
        List<Peca> resultado = new ArrayList<>();

        int indiceEsquerda = 0;
        int indiceDireita  = 0;

        // Enquanto houver elementos em ambas as sublistas, compara e insere o correto
        while (indiceEsquerda < esquerda.size() && indiceDireita < direita.size()) {

            Peca pecaEsquerda = esquerda.get(indiceEsquerda);
            Peca pecaDireita  = direita.get(indiceDireita);

            // vaiParaEsquerda = true → o elemento da esquerda deve vir primeiro
            boolean vaiParaEsquerda = comparar(pecaEsquerda, pecaDireita, criterio) <= 0;

            if (vaiParaEsquerda) {
                resultado.add(pecaEsquerda);
                indiceEsquerda++;
            } else {
                resultado.add(pecaDireita);
                indiceDireita++;
            }
        }

        // Anexa os elementos restantes da metade esquerda (se houver)
        while (indiceEsquerda < esquerda.size()) {
            resultado.add(esquerda.get(indiceEsquerda));
            indiceEsquerda++;
        }

        // Anexa os elementos restantes da metade direita (se houver)
        while (indiceDireita < direita.size()) {
            resultado.add(direita.get(indiceDireita));
            indiceDireita++;
        }

        return resultado;
    }

    // -----------------------------------------------------------------------
    // ETAPA 3 — COMPARAR
    // Retorna negativo se pecaA deve vir antes de pecaB, positivo se depois.
    // -----------------------------------------------------------------------

    private int comparar(Peca pecaA, Peca pecaB, CriterioOrdenacao criterio) {
        switch (criterio) {

            case NOME_CRESCENTE:
                // Comparação alfabética ignorando diferença entre maiúsculas e minúsculas
                return pecaA.getNome().compareToIgnoreCase(pecaB.getNome());

            case NOME_DECRESCENTE:
                // Inverte o resultado do crescente para obter Z → A
                return pecaB.getNome().compareToIgnoreCase(pecaA.getNome());

            case QUANTIDADE_CRESCENTE:
                // Menor quantidade primeiro (ordem natural dos inteiros)
                return Integer.compare(pecaA.getQuantidadeEstoque(), pecaB.getQuantidadeEstoque());

            case QUANTIDADE_DECRESCENTE:
                // Maior quantidade primeiro (ordem inversa)
                return Integer.compare(pecaB.getQuantidadeEstoque(), pecaA.getQuantidadeEstoque());

            default:
                throw new IllegalArgumentException("Critério de ordenação não suportado: " + criterio);
        }
    }
}
