import {api} from "../api/api";
import {Cliente, ClienteInput} from "../@types/Cliente";

// Espelha com.senai.automotiva.controllers.ClienteController (@RequestMapping "/api/clientes").

export async function listarClientes(): Promise<Cliente[]> {
    const response = await api.get<Cliente[]>("/clientes");
    return response.data;
}

export async function buscarClientePorId(id: number): Promise<Cliente> {
    const response = await api.get<Cliente>(`/clientes/${id}`);
    return response.data;
}

export async function buscarClientesPorNome(nome: string): Promise<Cliente[]> {
    const response = await api.get<Cliente[]>("/clientes/buscar", {
        params: {nome},
    });
    return response.data;
}

export async function criarCliente(dto: ClienteInput): Promise<Cliente> {
    const response = await api.post<Cliente>("/clientes", dto);
    return response.data;
}

export async function atualizarCliente(id: number, dto: ClienteInput): Promise<Cliente> {
    const response = await api.put<Cliente>(`/clientes/${id}`, dto);
    return response.data;
}

export async function ativarDesativarCliente(id: number): Promise<void> {
    await api.patch(`/clientes/${id}/ativar-desativar`);
}

export async function deletarCliente(id: number): Promise<void> {
    await api.delete(`/clientes/${id}`);
}
