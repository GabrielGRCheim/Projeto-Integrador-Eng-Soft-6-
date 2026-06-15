import React, {useEffect, useState} from "react";

import {ScrollView , Text, View} from 'react-native';
import {StatCard} from "../../components/StatCard";
import {styles} from "./styles";
import {RevenueChart} from "../../components/Charts/RevenueChart";
import {DashboardData} from "../../@types/dashboard";
import {OrdersStatusChart} from "../../components/Charts/OrdersStatusChart";
import {api} from "../../api/api";
import {TopServicesChart} from "../../components/Charts/TopServicesChart";


export default function Dashboard() {

    const [dashboardData, setDashboardData] = useState<DashboardData | null>(null);

    const USE_MOCK = true;

    async function getDashboard(): Promise<DashboardData> {
        if (USE_MOCK) {
            return {
                revenue: [
                    { month: "Jan", value: 12000 },
                    { month: "Fev", value: 18000 }
                ],
                orderStatus: {
                    recebido: 10,
                    pendente: 5,
                    cancelado: 2,
                    emAnalise: 8
                },
                topService: [
                    { service: "Troca de Óleo",quantity: 20 },
                    { service: "Alinhamento",quantity: 50 },
                    { service: "Troca de Correia",quantity: 20 },
                    { service: "Suspensão",quantity: 5 },
                ],
            };
        }

        const response = await api.get("/dashboard");
        return response.data;
    }

    useEffect(() => {
        loadDashboard();
    }, []);

    const loadDashboard = async () => {
        const data = await getDashboard();

        setDashboardData(data);
    };

    if (!dashboardData) {
        return <Text>Carregando...</Text>
    }
        return (
            <ScrollView style={styles.container}>
                <View>
                    <View>
                        <Text>
                            Ola Dashboard!
                        </Text>
                    </View>
                    <View style={styles.boxCards}>

                        <StatCard
                            title="Veículos no Patio"
                            value="12"
                        />

                        <StatCard
                            title="Ordem de Serviços"
                            value="6"
                        />

                        <StatCard
                            title="Faturamento"
                            value="R$ 3.298"
                        />

                        <StatCard
                            title="Peças"
                            value="R$ 1.698"
                        />

                        <StatCard
                            title="Comissão"
                            value="R$ 144"
                        />

                        <StatCard
                            title="Custo"
                            value="R$ 1.013"
                        />

                        <StatCard
                            title="Lucro"
                            value="R$ 2.142"
                        />

                        <StatCard
                            title="Serviços em aberto"
                            value="5"
                        />

                    </View>

                    <View style={styles.section} >
                        <Text style={styles.sectionTitle}>Receita</Text>
                        <View style={styles.chartCard}>
                            <RevenueChart
                            labels={dashboardData.revenue.map(r => r.month)}
                            values={dashboardData.revenue.map(r => r.value)}
                        /></View>
                    </View>

                    <View style={styles.section}>
                        <Text style={styles.sectionTitle}>Status das Ordens De serviço</Text>
                        <View style={styles.chartCard}>
                            <OrdersStatusChart
                                data={dashboardData.orderStatus}
                            />
                        </View>
                    </View>

                    <View style={styles.section}>
                        <Text style={styles.sectionTitle}>Serviços mais realizados</Text>
                      <View style={styles.chartCard}>
                          <TopServicesChart
                              labels={dashboardData.topService.map(r=> r.service)}
                              values={dashboardData.topService.map(r=> r.quantity)}
                          />
                      </View>
                    </View>

                </View>
            </ScrollView>
        )
    }
