// Espelha com.senai.automotiva.dtos.PecaDTO do back-end.

export type Peca = {
    id: number;
    nome: string;
    codigo?: string;
    descricao?: string;
    precoCusto: number;
    precoVenda: number;
    quantidadeEstoque: number;
    quantidadeMinima: number;
    unidade?: string;
    fornecedor?: string;
    ativo: boolean;
    estoqueBaixo: boolean;
};

// Corresponde a PecaDTO.RequisicaoPeca.
export type PecaInput = {
    nome: string;
    codigo?: string;
    descricao?: string;
    precoCusto: number;
    precoVenda: number;
    quantidadeEstoque?: number;
    quantidadeMinima?: number;
    unidade?: string;
    fornecedor?: string;
};

export type TipoMovimentacaoEstoque = "ENTRADA" | "SAIDA" | "AJUSTE";

// Corresponde a PecaDTO.AjusteEstoque.
export type AjusteEstoqueInput = {
    quantidade: number;
    motivo: string;
    tipoMovimentacao: TipoMovimentacaoEstoque;
};
