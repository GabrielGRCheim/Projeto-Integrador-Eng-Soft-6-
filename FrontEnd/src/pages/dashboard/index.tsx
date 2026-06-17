import React, {useCallback, useEffect, useState} from "react";
import { useFocusEffect } from "@react-navigation/native";
import {ActivityIndicator, ScrollView, Text, TouchableOpacity, View} from 'react-native';
import {StatCard} from "../../components/StatCard";
import {styles} from "./styles";
import {RevenueChart} from "../../components/Charts/RevenueChart";
import {DashboardData} from "../../@types/dashboard";
import {OrdersStatusChart} from "../../components/Charts/OrdersStatusChart";
import {TopServicesChart} from "../../components/Charts/TopServicesChart";
import {getDashboardData} from "../../services/dashboardService";
import {getApiErrorMessage} from "../../api/api";
import {AppHeader} from "../../containers/AppHeader";

function formatMoeda(valor: number): string {
    return `R$ ${valor.toFixed(2).replace(".", ",")}`;
}

export default function Dashboard() {

    const [dashboardData, setDashboardData] = useState<DashboardData | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    // O back-end não tem (ainda) um endpoint de dashboard com agregações
    // prontas — getDashboardData() calcula tudo a partir de GET /ordens-servico,
    // que já é real. Veja src/services/dashboardService.ts.
    const loadDashboard = useCallback(async () => {
        try {
            setErrorMessage(null);
            const data = await getDashboardData();
            setDashboardData(data);
        } catch (error) {
            setErrorMessage(getApiErrorMessage(error));
        }
    }, []);

    useFocusEffect(
        useCallback(() => {
            loadDashboard();
        }, [loadDashboard])
    );

    if (errorMessage) {
        return (
            <View style={[styles.container, {justifyContent: "center", alignItems: "center", padding: 20}]}>
                <Text style={{textAlign: "center", marginBottom: 12}}>{errorMessage}</Text>
                <TouchableOpacity onPress={loadDashboard}>
                    <Text style={{color: "#2563EB", fontWeight: "bold"}}>Tentar novamente</Text>
                </TouchableOpacity>
            </View>
        );
    }

    if (!dashboardData) {
        return (
            <View style={[styles.container, {justifyContent: "center", alignItems: "center"}]}>
                <ActivityIndicator size="large"/>
            </View>
        );
    }

    return (
        <ScrollView style={styles.container}>
            <View>
                <View>
                    <AppHeader
                        title="Dashboard"
                        onLogout={() => console.log("logout")}
                    />
                </View>
                <View style={styles.boxCards}>

                    <StatCard
                        title="Veículos no Pátio"
                        value={String(dashboardData.stats.veiculosNoPatio)}
                    />

                    <StatCard
                        title="Ordens Abertas"
                        value={String(dashboardData.stats.ordensAbertas)}
                    />

                    <StatCard
                        title="Faturamento (OS concluídas)"
                        value={formatMoeda(dashboardData.stats.faturamentoTotal)}
                    />

                    <StatCard
                        title="Peças vendidas"
                        value={formatMoeda(dashboardData.stats.totalPecas)}
                    />

                </View>

                {dashboardData.revenue.length > 0 && (
                    <View style={styles.section}>
                        <Text style={styles.sectionTitle}>Receita (OS concluídas por mês)</Text>
                        <View style={styles.chartCard}>
                            <RevenueChart
                                labels={dashboardData.revenue.map(r => r.month)}
                                values={dashboardData.revenue.map(r => r.value)}
                            />
                        </View>
                    </View>
                )}

                <View style={styles.section}>
                    <Text style={styles.sectionTitle}>Status das Ordens De serviço</Text>
                    <View style={styles.chartCard}>
                        <OrdersStatusChart
                            data={dashboardData.orderStatus}
                        />
                    </View>
                </View>

                {dashboardData.topServices.length > 0 && (
                    <View style={styles.section}>
                        <Text style={styles.sectionTitle}>Serviços mais realizados</Text>
                        <View style={styles.chartCard}>
                            <TopServicesChart
                                labels={dashboardData.topServices.map(r => r.service)}
                                values={dashboardData.topServices.map(r => r.quantity)}
                            />
                        </View>
                    </View>
                )}

            </View>
        </ScrollView>
    )
}
