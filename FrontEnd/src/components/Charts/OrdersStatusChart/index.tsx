import { Dimensions } from "react-native";
import { PieChart } from "react-native-chart-kit";
import {OrderStatusData} from "../../../@types/dashboard";

type Props = {
    data: OrderStatusData;
};

const screenWidth = Dimensions.get("window").width;

export function OrdersStatusChart({ data }: Props) {

    const pieData = [
        {
            name: "Recebido",
            population: 10,
            color: "#10B981",
            legendFontColor: "#7F7F7F",
            legendFontSize: 15
        },
        {
            name: "Pendente",
            population: 5,
            color: "#F59E0B",
            legendFontColor: "#7F7F7F",
            legendFontSize: 15
        },
        {
            name: "Cancelado",
            population: 2,
            color: "#EF4444",
            legendFontColor: "#7F7F7F",
            legendFontSize: 15
        },
        {
            name: "Em análise",
            population: 8,
            color: "#2563EB",
            legendFontColor: "#7F7F7F",
            legendFontSize: 15
        }
    ];

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