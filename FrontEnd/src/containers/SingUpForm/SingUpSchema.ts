import {z} from 'zod'

export const singUpSchema = z.object({
    fullname: z.string({message: "campo obrigatório"})
        .min(2,"Nome muito curto"),
    email: z.string({message: "campo obrigatório"})
        .email("email inválido"),
    password: z.string({message: "campo obrigatório"})
        .min(6,"senha muito curta"),
    confirmPassword: z.string({message: "campo obrigatório"})
        .min(6,"senha muito curta"),
}).refine(data => data.password === data.confirmPassword,{
    message: "senhas devem ser iguais",
    path: ["confirmPassword"],
});

export type SingUpSchema = z.infer<typeof singUpSchema>;