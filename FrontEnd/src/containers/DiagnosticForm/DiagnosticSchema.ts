import {z} from "zod";

export const DiagnosticSchema = z.object({
    diagnostics: z.string().min(1,"campo Obrigatorio")
});

export type DiagnosticSchema = z.infer<typeof DiagnosticSchema>