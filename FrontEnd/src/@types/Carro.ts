// Espelha com.senai.automotiva.dtos.CarroDTO do back-end.

export type Carro = {
    id: number;
    marca: string;
    modelo: string;
    ano: number;
    placa: string;
    quilometragem?: number;
    cor?: string;
    chassi?: string;
    clienteId: number;
    nomeCliente: string;
};

// Corresponde a CarroDTO.RequisicaoCarro (corpo enviado em POST/PUT).
// clienteId é obrigatório no back-end (@NotNull "Cliente é obrigatório").
export type CarroInput = {
    marca: string;
    modelo: string;
    ano: number;
    placa: string;
    quilometragem?: number;
    cor?: string;
    chassi?: string;
    clienteId: number;
};
