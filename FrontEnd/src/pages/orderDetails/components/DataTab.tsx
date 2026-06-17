import {View} from "react-native";
import {StatCard} from "../../../components/StatCard";
import {styles} from "../styles";
import {OrdemServico} from "../../../@types/OrdemServico";
import {STATUS_ORDEM_SERVICO_COLOR, STATUS_ORDEM_SERVICO_LABEL} from "../../../@types/StatusOrdemServico";

type Props = {
    order: OrdemServico | null;
}

export function DataTab({order}: Props) {

    if (!order) {
        return null;
    }

    return (
        <View style={styles.boxCards}>
            <StatCard
                title="Nº da OS"
                value={order.numeroOs}
            />

            <StatCard
                title="Cliente"
                value={order.nomeCliente}
            />

            <StatCard
                title="Veículo"
                value={order.modeloCarro}
            />

            <StatCard
                title="Placa"
                value={order.placaCarro}
            />

            <StatCard
                title="Solicitação do Cliente"
                value={order.queixaCliente ?? "Não informado"}
            />

            <StatCard
                title="Data de Entrada"
                value={order.criadoEm}
            />

            <StatCard
                title="Quilometragem"
                value={order.quilometragem != null ? String(order.quilometragem) : "Não informado"}
            />

            <StatCard
                title="Cor"
                value={order.corCarro ?? "Não informado"}
            />

            <StatCard
                title="Responsável"
                value={order.nomeResponsavel ?? "Não atribuído"}
            />

            <StatCard
                title="Status"
                value={STATUS_ORDEM_SERVICO_LABEL[order.status]}
                valueColor={STATUS_ORDEM_SERVICO_COLOR[order.status]}
            />
        </View>
    );
}
