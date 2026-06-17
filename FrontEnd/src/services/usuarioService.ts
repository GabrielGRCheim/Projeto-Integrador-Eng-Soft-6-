import {api} from "../api/api";
import {Usuario, UsuarioInput} from "../@types/Usuario";

// Espelha com.senai.automotiva.controllers.UsuarioController (@RequestMapping "/api/usuarios").
// Importante: isto é apenas CRUD de usuário. O back-end NÃO possui endpoint de
// login/autenticação hoje (sem JWT, sem sessão) — por isso não existe um
// "fazerLogin" aqui. A tela de Login continua sem chamar API real até essa
// peça ser construída no back-end.

export async function listarUsuarios(): Promise<Usuario[]> {
    const response = await api.get<Usuario[]>("/usuarios");
    return response.data;
}

export async function buscarUsuarioPorId(id: number): Promise<Usuario> {
    const response = await api.get<Usuario>(`/usuarios/${id}`);
    return response.data;
}

export async function criarUsuario(dto: UsuarioInput): Promise<Usuario> {
    const response = await api.post<Usuario>("/usuarios", dto);
    return response.data;
}

export async function atualizarUsuario(id: number, dto: UsuarioInput): Promise<Usuario> {
    const response = await api.put<Usuario>(`/usuarios/${id}`, dto);
    return response.data;
}

export async function ativarDesativarUsuario(id: number): Promise<void> {
    await api.patch(`/usuarios/${id}/ativar-desativar`);
}

export async function deletarUsuario(id: number): Promise<void> {
    await api.delete(`/usuarios/${id}`);
}
