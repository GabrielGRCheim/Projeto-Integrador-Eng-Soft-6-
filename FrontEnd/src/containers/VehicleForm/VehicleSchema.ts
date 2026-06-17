import {z} from 'zod'

// Campos alinhados com CarroDTO.RequisicaoCarro do back-end.
// Obrigatórios no back-end: marca, modelo, ano, placa, clienteId.
// Opcionais no back-end: quilometragem, cor, chassi.
//
// clienteId não existia no formulário original — sem ele, toda criação de
// veículo falhava no back-end (@NotNull "Cliente é obrigatório"). A tela
// agora exige a seleção de um cliente já cadastrado.
export const VehicleSchema = z.object({
    marca: z.string()
        .min(1, "campo obrigatório"),

    modelo: z.string()
        .min(1, "campo obrigatório"),

    ano: z.string()
        .min(4, "campo obrigatório")
        .regex(/^[0-9]{4}$/, {message: "Formato inválido. Use YYYY"}),

    placa: z.string()
        .min(1, "campo obrigatório")
        .regex(/^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$/, {
            message: "Formato inválido. Use ABC1234 ou ABC1D23",
        }),

    cor: z.string().optional(),

    chassi: z.string().optional(),

    quilometragem: z.string()
        .regex(/^[0-9]{1,6}$/, {message: "Use apenas números"})
        .optional()
        .or(z.literal("")),

    clienteId: z.number({message: "Selecione um cliente"}),
});

export type VehicleSchema = z.infer<typeof VehicleSchema>;
