// Espelha com.senai.automotiva.dtos.ClienteDTO do back-end.

export type Cliente = {
    id: number;
    nome: string;
    cpf: string;
    telefone?: string;
    email?: string;
    endereco?: string;
    ativo: boolean;
    totalCarros: number;
};

// Corresponde a ClienteDTO.RequisicaoCliente (corpo enviado em POST/PUT).
export type ClienteInput = {
    nome: string;
    cpf: string;
    telefone?: string;
    email?: string;
    endereco?: string;
};
