// Espelha com.senai.automotiva.dtos.ItemOrdemServicoDTO do back-end.

export type ItemOrdemServico = {
    id: number;
    ordemServicoId: number;

    servicoId?: number;
    nomeServico?: string;

    pecaId?: number;
    nomePeca?: string;

    quantidade: number;
    valorUnitario: number;
    valorTotal: number;

    observacao?: string;
};

// Corresponde a ItemOrdemServicoDTO.RequisicaoItem (POST /ordens-servico/itens).
// Informe servicoId OU pecaId (nunca os dois, nem nenhum).
export type ItemOrdemServicoInput = {
    ordemServicoId: number;
    servicoId?: number;
    pecaId?: number;
    quantidade: number;
    valorUnitario: number;
    observacao?: string;
};
