import {z} from 'zod'

// Campos alinhados com UsuarioDTO.RequisicaoUsuario do back-end (nome, email,
// senha, perfil). Os campos antigos (telefone, CNPJ, nome fantasia) foram
// removidos porque a entidade Usuario do back-end não tem esse conceito —
// hoje ela representa um funcionário interno (perfil ADMINISTRADOR/MECANICO/
// ATENDENTE) de uma única oficina, não uma conta de empresa com CNPJ.
// confirmarSenha existe só no front, para validação local, e nunca é
// enviado ao back-end.
export const singUpSchema = z.object({
    nome: z
        .string()
        .min(1, "campo obrigatório")
        .min(2, "Nome muito curto"),

    email: z
        .string()
        .min(1, "campo obrigatório")
        .email("email inválido"),


    senha: z
        .string()
        .min(1, "campo obrigatório")
        .min(6, "senha muito curta"),

    confirmarSenha: z
        .string()
        .min(1, "campo obrigatório")
        .min(6, "senha muito curta"),
}).refine((data) => data.senha === data.confirmarSenha, {
    message: "senhas devem ser iguais",
    path: ["confirmarSenha"],
});

export type SingUpSchema = z.infer<typeof singUpSchema>;
