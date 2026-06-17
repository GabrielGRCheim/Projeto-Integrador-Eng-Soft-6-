import {z} from 'zod'

// Campos alinhados com OrdemServicoDTO.RequisicaoOrdem do back-end.
// Uma OS é criada a partir de um carroId (o cliente já está vinculado ao
// carro no cadastro de veículo) — por isso não existem mais clientId/vehicleId
// separados nem campos de checklist/orçamento aqui: itens (peças/serviços) são
// adicionados depois, OS já criada, via POST /ordens-servico/itens.
//
// queixaCliente e valorMaoObra não têm validação obrigatória no back-end;
// o mínimo de caracteres abaixo é uma escolha de UX do front, não uma regra
// do back-end.
export const OrderSchema = z.object({
    carroId: z.number({
        message: "Selecione um veículo"
    }),

    queixaCliente: z
        .string()
        .min(5, "Descreva a solicitação do cliente"),

    valorMaoObra: z
        .string()
        .regex(/^\d+(\.\d{1,2})?$/, {message: "Use um valor numérico, ex: 150.00"})
        .optional()
        .or(z.literal("")),
});

export type OrderSchema = z.infer<typeof OrderSchema>;
