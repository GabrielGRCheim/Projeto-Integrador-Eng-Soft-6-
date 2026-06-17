import {View} from "react-native";
import {styles} from "../styles";
import {StatCard} from "../../../components/StatCard";
import {OrdemServico} from "../../../@types/OrdemServico";

type Props = {
    order: OrdemServico | null;
};

function formatMoeda(valor: number): string {
    return `R$ ${valor.toFixed(2).replace(".", ",")}`;
}

export function FinancialTab({order}: Props) {

    if (!order) {
        return null;
    }

    const totalPecas = order.itens
        .filter((item) => item.pecaId)
        .reduce((soma, item) => soma + item.valorTotal, 0);

    const totalServicos = order.itens
        .filter((item) => item.servicoId)
        .reduce((soma, item) => soma + item.valorTotal, 0);

    return (
        <View style={styles.boxCards}>
            <StatCard
                title="Peças"
                value={formatMoeda(totalPecas)}
            />
            <StatCard
                title="Serviços"
                value={formatMoeda(totalServicos)}
            />
            <StatCard
                title="Total"
                value={formatMoeda(order.valorTotal)}
            />
        </View>
    );
}
