import React from 'react'
import {Text, View} from 'react-native'
import {styles} from "./styles";
import {StatusOrdemServico, STATUS_ORDEM_SERVICO_COLOR, STATUS_ORDEM_SERVICO_LABEL} from "../../@types/StatusOrdemServico";

type Order = {
    id: number;
    cliente: string;
    veiculo: string;
    placa: string;
    data: string;
    status: StatusOrdemServico;
};

type Props = {
    order: Order;
}

export function OrderCard({ order }: Props) {

    return (
        <View style={styles.container}>
            <Text style={styles.title}>OS #{order.id}</Text>
            <Text style={styles.value}>{order.cliente}</Text>
            <Text style={styles.value}>{order.veiculo}</Text>
            <Text style={styles.value}>{order.placa}</Text>
            <Text style={styles.value}>{order.data}</Text>
            <Text
                style={[
                    { color: STATUS_ORDEM_SERVICO_COLOR[order.status], fontWeight: "bold" }
                ]}
            >
                {STATUS_ORDEM_SERVICO_LABEL[order.status]}
            </Text>
        </View>
    )
}
