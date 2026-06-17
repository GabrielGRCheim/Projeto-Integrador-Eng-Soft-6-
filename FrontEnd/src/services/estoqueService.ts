import {api} from "../api/api";
import {AjusteEstoqueInput, Peca, PecaInput} from "../@types/Peca";

// Espelha com.senai.automotiva.controllers.EstoqueController (@RequestMapping "/api/estoque").
// Atenção: a rota de peças é "/estoque/pecas", e não "/pecas" — esse era um bug
// real no front original (ServiceTab chamava api.get("/pecas"), que resultava em 404).

export type CriterioOrdenacaoEstoque =
    | "NOME_CRESCENTE"
    | "NOME_DECRESCENTE"
    | "QUANTIDADE_CRESCENTE"
    | "QUANTIDADE_DECRESCENTE";

export async function listarPecas(): Promise<Peca[]> {
    const response = await api.get<Peca[]>("/estoque/pecas");
    return response.data;
}

export async function listarPecasOrdenadas(criterio: CriterioOrdenacaoEstoque): Promise<Peca[]> {
    const response = await api.get<Peca[]>("/estoque/pecas/ordenadas", {
        params: {criterio},
    });
    return response.data;
}

export async function listarPecasComEstoqueBaixo(): Promise<Peca[]> {
    const response = await api.get<Peca[]>("/estoque/pecas/estoque-baixo");
    return response.data;
}

export async function buscarPecaPorId(id: number): Promise<Peca> {
    const response = await api.get<Peca>(`/estoque/pecas/${id}`);
    return response.data;
}

export async function criarPeca(dto: PecaInput): Promise<Peca> {
    const response = await api.post<Peca>("/estoque/pecas", dto);
    return response.data;
}

export async function atualizarPeca(id: number, dto: PecaInput): Promise<Peca> {
    const response = await api.put<Peca>(`/estoque/pecas/${id}`, dto);
    return response.data;
}

export async function ajustarEstoque(id: number, dto: AjusteEstoqueInput): Promise<Peca> {
    const response = await api.patch<Peca>(`/estoque/pecas/${id}/ajustar`, dto);
    return response.data;
}

export async function ativarDesativarPeca(id: number): Promise<void> {
    await api.patch(`/estoque/pecas/${id}/ativar-desativar`);
}

export async function deletarPeca(id: number): Promise<void> {
    await api.delete(`/estoque/pecas/${id}`);
}
