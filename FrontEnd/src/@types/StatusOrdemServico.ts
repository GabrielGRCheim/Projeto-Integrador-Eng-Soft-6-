// Espelha exatamente o enum StatusOrdemServico do back-end
// (com.senai.automotiva.enums.StatusOrdemServico). Mantenha os dois em sincronia.
export type StatusOrdemServico =
    | "ABERTA"
    | "EM_ANDAMENTO"
    | "AGUARDANDO_PECA"
    | "CONCLUIDA"
    | "CANCELADA";

export const STATUS_ORDEM_SERVICO_LABEL: Record<StatusOrdemServico, string> = {
    ABERTA: "Aberta",
    EM_ANDAMENTO: "Em andamento",
    AGUARDANDO_PECA: "Aguardando peça",
    CONCLUIDA: "Concluída",
    CANCELADA: "Cancelada",
};

export const STATUS_ORDEM_SERVICO_COLOR: Record<StatusOrdemServico, string> = {
    ABERTA: "#2563EB",
    EM_ANDAMENTO: "#F59E0B",
    AGUARDANDO_PECA: "#8B5CF6",
    CONCLUIDA: "#10B981",
    CANCELADA: "#6B7280",
};

// Usada apenas para desenhar o "stepper" visual na tela de serviço.
// A validação de quais transições são permitidas é feita pelo back-end
// (OrdemServicoService.validarTransicaoStatus) — aqui é só apresentação.
export const STATUS_ORDEM_SERVICO_STEPS: StatusOrdemServico[] = [
    "ABERTA",
    "EM_ANDAMENTO",
    "AGUARDANDO_PECA",
    "CONCLUIDA",
];
