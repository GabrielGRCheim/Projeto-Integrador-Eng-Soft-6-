import {api} from "../api/api";
import {Carro, CarroInput} from "../@types/Carro";

// Espelha com.senai.automotiva.controllers.CarroController (@RequestMapping "/api/carros").

export async function listarCarros(): Promise<Carro[]> {
    const response = await api.get<Carro[]>("/carros");
    return response.data;
}

export async function buscarCarroPorId(id: number): Promise<Carro> {
    const response = await api.get<Carro>(`/carros/${id}`);
    return response.data;
}

export async function listarCarrosPorCliente(clienteId: number): Promise<Carro[]> {
    const response = await api.get<Carro[]>(`/carros/cliente/${clienteId}`);
    return response.data;
}

export async function criarCarro(dto: CarroInput): Promise<Carro> {
    const response = await api.post<Carro>("/carros", dto);
    return response.data;
}

export async function atualizarCarro(id: number, dto: CarroInput): Promise<Carro> {
    const response = await api.put<Carro>(`/carros/${id}`, dto);
    return response.data;
}

export async function deletarCarro(id: number): Promise<void> {
    await api.delete(`/carros/${id}`);
}
