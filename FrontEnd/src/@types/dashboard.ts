// O back-end ainda não possui um endpoint de dashboard (não existe nenhum
// DashboardController). Os tipos abaixo descrevem dados calculados no
// próprio front a partir de GET /ordens-servico, que já traz status, valores
// e itens de cada OS. Quando o back-end ganhar agregações próprias, esses
// tipos podem passar a vir direto da API.

export interface RevenueData {
    month: string;
    value: number;
}

export interface OrderStatusData {
    ABERTA: number;
    EM_ANDAMENTO: number;
    AGUARDANDO_PECA: number;
    CONCLUIDA: number;
    CANCELADA: number;
}

export interface TopService {
    service: string;
    quantity: number;
}

export interface DashboardStats {
    veiculosNoPatio: number;
    ordensAbertas: number;
    faturamentoTotal: number;
    totalPecas: number;
}

export interface DashboardData {
    stats: DashboardStats;
    revenue: RevenueData[];
    orderStatus: OrderStatusData;
    topServices: TopService[];
}
