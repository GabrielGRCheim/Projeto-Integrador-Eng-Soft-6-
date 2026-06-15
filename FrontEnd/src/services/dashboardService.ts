import {DashboardStats, OrderStatusData, RevenueData} from "../@types/dashboard";
import {api} from "../api/api";

export async function getDashboardsStats(): Promise<DashboardStats> {

    const response = await api.get("/dashboards");

    return response.data;

}

export async function getOrderStatusChart(): Promise<OrderStatusData> {
    return {
        recebido: 10,
        pendente: 5,
        cancelado: 2,
        emAnalise: 8
    };
}

export async function getRevenueChart(): Promise<RevenueData[]> {

    /*const response =
        await api.get("/dashboard/revenue");

    return response.data;*/

    return [
        {
            month: "Jan",
            value: 12000
        },
        {
            month: "Fev",
            value: 18000
        },
        {
            month: "Mar",
            value: 15000
        },
        {
            month: "Abr",
            value: 22000
        },
        {
            month: "Mai",
            value: 26000
        }
    ];
}

