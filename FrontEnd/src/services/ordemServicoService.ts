import {api} from "../api/api";
import {OrdemServico, OrdemServicoInput} from "../@types/OrdemServico";
import {ItemOrdemServico, ItemOrdemServicoInput} from "../@types/ItemOrdemServico";
import {StatusOrdemServico} from "../@types/StatusOrdemServico";

// Espelha com.senai.automotiva.controllers.OrdemServicoController
// (@RequestMapping "/api/ordens-servico").

export async function listarOrdens(): Promise<OrdemServico[]> {
    const response = await api.get<OrdemServico[]>("/ordens-servico");
    return response.data;
}

export async function listarOrdensAbertas(): Promise<OrdemServico[]> {
    const response = await api.get<OrdemServico[]>("/ordens-servico/abertas");
    return response.data;
}

export async function buscarOrdemPorId(id: number): Promise<OrdemServico> {
    const response = await api.get<OrdemServico>(`/ordens-servico/${id}`);
    return response.data;
}

export async function listarOrdensPorCliente(clienteId: number): Promise<OrdemServico[]> {
    const response = await api.get<OrdemServico[]>(`/ordens-servico/cliente/${clienteId}`);
    return response.data;
}

export async function criarOrdem(dto: OrdemServicoInput): Promise<OrdemServico> {
    const response = await api.post<OrdemServico>("/ordens-servico", dto);
    return response.data;
}

export async function atualizarOrdem(id: number, dto: OrdemServicoInput): Promise<OrdemServico> {
    const response = await api.put<OrdemServico>(`/ordens-servico/${id}`, dto);
    return response.data;
}

// O back-end espera o novo status como query param (?novoStatus=...), não no corpo.
export async function atualizarStatusOrdem(
    id: number,
    novoStatus: StatusOrdemServico
): Promise<OrdemServico> {
    const response = await api.patch<OrdemServico>(`/ordens-servico/${id}/status`, null, {
        params: {novoStatus},
    });
    return response.data;
}

export async function adicionarItem(dto: ItemOrdemServicoInput): Promise<ItemOrdemServico> {
    const response = await api.post<ItemOrdemServico>("/ordens-servico/itens", dto);
    return response.data;
}

export async function removerItem(itemId: number): Promise<void> {
    await api.delete(`/ordens-servico/itens/${itemId}`);
}
