import {z} from 'zod'

// Campos alinhados com ClienteDTO.RequisicaoCliente do back-end.
// No back-end, apenas "nome" e "cpf" são obrigatórios; telefone/email/endereco
// são opcionais (sem @NotBlank na entidade), então mantemos esse mesmo padrão aqui.
export const ClientSchema = z.object({
    nome: z
        .string()
        .min(1, "campo obrigatório")
        .min(2, "Nome muito curto"),

    cpf: z
        .string()
        .min(1, "CPF é obrigatório")
        .regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, {
            message: "Formato inválido. Use XXX.XXX.XXX-XX",
        }),

    telefone: z
        .string()
        .regex(/^\(\d{2}\) \d{5}-\d{4}$/, {
            message: "Formato inválido. Use (XX) XXXXX-XXXX",
        })
        .optional()
        .or(z.literal("")),

    email: z
        .string()
        .email("email inválido")
        .optional()
        .or(z.literal("")),

    endereco: z.string().optional(),
});

export type ClientSchema = z.infer<typeof ClientSchema>;
