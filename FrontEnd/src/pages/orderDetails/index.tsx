import {useRoute} from "@react-navigation/native";
import {ActivityIndicator, Text, View} from "react-native";
import {useCallback, useEffect, useState} from "react";
import {OrderTabs} from "./components/OrderTabs";
import {DataTab} from "./components/DataTab";
import {ServiceTab} from "./components/ServiceTab";
import {FinancialTab} from "./components/FinancialTab";
import {styles} from "./styles";
import {OrdemServico} from "../../@types/OrdemServico";
import {buscarOrdemPorId} from "../../services/ordemServicoService";
import {getApiErrorMessage} from "../../api/api";
import { useFocusEffect } from "@react-navigation/native";

export default function OrderDetails() {

    const [activeTab, setActiveTab] =
        useState("dados");

    const route = useRoute();

    const {orderId} = route.params as { orderId: number; };

    const [order, setOrder] = useState<OrdemServico | null>(null);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const loadOrder = useCallback(async () => {
        try {
            setErrorMessage(null);
            const data = await buscarOrdemPorId(orderId);
            setOrder(data);
        } catch (error) {
            setErrorMessage(getApiErrorMessage(error));
        }
    }, [orderId]);

    useFocusEffect(useCallback(() => {
        loadOrder().finally(() => setLoading(false));
    }, [loadOrder]));

    if (loading) {
        return (
            <View style={[styles.container, {justifyContent: "center"}]}>
                <ActivityIndicator size="large"/>
            </View>
        );
    }

    if (errorMessage) {
        return (
            <View style={[styles.container, {justifyContent: "center", padding: 20}]}>
                <Text style={{textAlign: "center"}}>{errorMessage}</Text>
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <View style={styles.boxTop}>
                <OrderTabs
                    activeTab={activeTab}
                    onChange={setActiveTab}
                />

                {activeTab === "dados" &&
                    <DataTab order={order}/>
                }

                {activeTab === "servico" &&
                    <ServiceTab order={order} onOrderUpdated={loadOrder}/>
                }

                {activeTab === "financeiro" &&
                    <FinancialTab order={order}/>
                }
            </View>
        </View>
    );
}
