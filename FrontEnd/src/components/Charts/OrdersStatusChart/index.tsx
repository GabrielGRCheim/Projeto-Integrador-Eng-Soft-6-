import { Dimensions } from "react-native";
import { PieChart } from "react-native-chart-kit";
import {OrderStatusData} from "../../../@types/dashboard";
import {STATUS_ORDEM_SERVICO_COLOR, STATUS_ORDEM_SERVICO_LABEL} from "../../../@types/StatusOrdemServico";

type Props = {
    data: OrderStatusData;
};

const screenWidth = Dimensions.get("window").width;

export function OrdersStatusChart({ data }: Props) {

    const pieData = (Object.keys(data) as Array<keyof OrderStatusData>)
        .filter((status) => data[status] > 0)
        .map((status) => ({
            name: STATUS_ORDEM_SERVICO_LABEL[status],
            population: data[status],
            color: STATUS_ORDEM_SERVICO_COLOR[status],
            legendFontColor: "#7F7F7F",
            legendFontSize: 15,
        }));

    if (pieData.length === 0) {
        return null;
    }

    return (

        <PieChart
            data={pieData}
            width={screenWidth - 32}
            height={220}
            chartConfig={{
                color: (opacity = 1) =>
                    `rgba(0,0,0,${opacity})`,
                labelColor: (opacity = 1) =>
                    `rgba(0, 0, 0, ${opacity})`,
            }}
            accessor="population"
            backgroundColor="transparent"
            paddingLeft="15"
            center={[10, 50]}
            absolute
        />
    );
}
