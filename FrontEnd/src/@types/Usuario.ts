// Espelha com.senai.automotiva.dtos.UsuarioDTO e o enum PerfilUsuario do back-end.

export type Usuario = {
    id: number;
    nome: string;
    email: string;
    perfil: "ADMINISTRADOR";
    ativo: boolean;
    criadoEm?: string;
};

// Corresponde a UsuarioDTO.RequisicaoUsuario.
// Observação: o back-end hoje NÃO possui endpoint de login (apenas CRUD de usuário).
// Este tipo serve para o cadastro de usuário; autenticação real depende de uma
// fase futura no back-end (emissão de token / JWT).
export type UsuarioInput = {
    nome: string;
    email: string;
    senha: string;
    perfil: "ADMINISTRADOR";
};
