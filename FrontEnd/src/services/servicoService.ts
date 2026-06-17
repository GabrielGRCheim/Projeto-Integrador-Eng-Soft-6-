import {api} from "../api/api";
import {Servico, ServicoInput} from "../@types/Servico";

// Espelha com.senai.automotiva.controllers.ServicoController (@RequestMapping "/api/servicos").

export async function listarServicos(): Promise<Servico[]> {
    const response = await api.get<Servico[]>("/servicos");
    return response.data;
}

export async function listarServicosAtivos(): Promise<Servico[]> {
    const response = await api.get<Servico[]>("/servicos/ativos");
    return response.data;
}

export async function buscarServicoPorId(id: number): Promise<Servico> {
    const response = await api.get<Servico>(`/servicos/${id}`);
    return response.data;
}

export async function criarServico(dto: ServicoInput): Promise<Servico> {
    const response = await api.post<Servico>("/servicos", dto);
    return response.data;
}

export async function atualizarServico(id: number, dto: ServicoInput): Promise<Servico> {
    const response = await api.put<Servico>(`/servicos/${id}`, dto);
    return response.data;
}

export async function ativarDesativarServico(id: number): Promise<void> {
    await api.patch(`/servicos/${id}/ativar-desativar`);
}

export async function deletarServico(id: number): Promise<void> {
    await api.delete(`/servicos/${id}`);
}
