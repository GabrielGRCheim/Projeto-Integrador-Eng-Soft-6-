// Espelha com.senai.automotiva.dtos.ServicoDTO do back-end.

export type Servico = {
    id: number;
    nome: string;
    descricao?: string;
    precoBase: number;
    tempoEstimadoHoras?: number;
    ativo: boolean;
};

// Corresponde a ServicoDTO.RequisicaoServico.
export type ServicoInput = {
    nome: string;
    descricao?: string;
    precoBase: number;
    tempoEstimadoHoras?: number;
};
