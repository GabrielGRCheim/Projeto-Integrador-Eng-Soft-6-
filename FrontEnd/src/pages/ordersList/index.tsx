import React, {useCallback, useEffect, useState} from "react";
import { useFocusEffect } from "@react-navigation/native";
import {ActivityIndicator, FlatList, RefreshControl, Text, TouchableOpacity, View} from 'react-native';
import {OrderCard} from "../../components/OrderCard";
import {styles} from "./styles";
import {OrdemServico} from "../../@types/OrdemServico";
import {listarOrdens} from "../../services/ordemServicoService";
import {getApiErrorMessage} from "../../api/api";

export default function OrdersList({navigation}: any) {

    const [orders, setOrders] = useState<OrdemServico[]>([]);
    const [loading, setLoading] = useState(true);
    const [refreshing, setRefreshing] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const loadOrders = useCallback(async () => {
        try {
            setErrorMessage(null);
            const data = await listarOrdens();
            setOrders(data);
        } catch (error) {
            setErrorMessage(getApiErrorMessage(error));
        }
    }, []);

    useFocusEffect(useCallback(() => {
        loadOrders().finally(() => setLoading(false));
    }, [loadOrders]));


    async function onRefresh() {
        setRefreshing(true);
        await loadOrders();
        setRefreshing(false);
    }

    if (loading) {
        return (
            <View style={[styles.container, {justifyContent: "center", alignItems: "center"}]}>
                <ActivityIndicator size="large"/>
            </View>
        );
    }

    if (errorMessage) {
        return (
            <View style={[styles.container, {justifyContent: "center", alignItems: "center", padding: 20}]}>
                <Text style={{textAlign: "center", marginBottom: 12}}>{errorMessage}</Text>
                <TouchableOpacity onPress={loadOrders}>
                    <Text style={{color: "#2563EB", fontWeight: "bold"}}>Tentar novamente</Text>
                </TouchableOpacity>
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <FlatList
                data={orders}
                refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh}/>}
                contentContainerStyle={{
                    paddingVertical: 10,
                }}
                keyExtractor={(item) => item.id.toString()}
                ListEmptyComponent={
                    <Text style={{textAlign: "center", marginTop: 40, color: "#6B7280"}}>
                        Nenhuma ordem de serviço encontrada.
                    </Text>
                }
                renderItem={({item}) => (
                    <TouchableOpacity
                        onPress={() =>
                            navigation.navigate("OrderDetails", {
                                orderId: item.id
                            })
                        }>
                        <View style={styles.boxCards}>
                            <OrderCard
                                order={{
                                    id: item.id,
                                    cliente: item.nomeCliente,
                                    veiculo: item.modeloCarro,
                                    placa: item.placaCarro,
                                    data: item.criadoEm,
                                    status: item.status
                                }}
                            />
                        </View>
                    </TouchableOpacity>
                )}
            />
        </View>
    )
}
