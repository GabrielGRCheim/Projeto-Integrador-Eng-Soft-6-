export interface DashboardStats {
    totalVehicles: number;
    openOrders: number;
    pendingApprovals: number;
    monthlyRevenue: number;
}

export interface DashboardData {
    revenue: RevenueData[];
    orderStatus: OrderStatusData;
    topService: TopServices[];
}

export interface RevenueData {
    month: string;
    value: number;
}

export interface OrderStatusData {
    recebido: number;
    pendente: number;
    cancelado: number;
    emAnalise: number;
}

export interface TopServices {
    service: string;
    quantity: number;
}

export interface RecentOrder {
    id: number;
    vehicle: string;
    customer: string;
    status: string;
}