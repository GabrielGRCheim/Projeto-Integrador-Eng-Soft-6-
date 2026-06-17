import {ItemOrdemServico} from "./ItemOrdemServico";
import {StatusOrdemServico} from "./StatusOrdemServico";

// Espelha com.senai.automotiva.dtos.OrdemServicoDTO.RespostaOrdem do back-end.
export type OrdemServico = {
    id: number;
    numeroOs: string;

    carroId: number;
    placaCarro: string;
    modeloCarro: string;
    corCarro?: string;
    chassiCarro?: string;
    quilometragem?: number;

    nomeCliente: string;
    nomeResponsavel?: string;

    status: StatusOrdemServico;

    diagnostico?: string;
    queixaCliente?: string;

    valorMaoObra: number;
    valorTotal: number;

    criadoEm: string;
    concluidoEm?: string;

    itens: ItemOrdemServico[];
};

// Corresponde a OrdemServicoDTO.RequisicaoOrdem (corpo de POST e PUT /ordens-servico).
// Usado tanto para criar quanto para atualizar uma OS — carroId é obrigatório em ambos.
export type OrdemServicoInput = {
    carroId: number;
    diagnostico?: string;
    responsavelId?: number;
    queixaCliente?: string;
    valorMaoObra?: number;
};
