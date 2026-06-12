import { Dimensions } from "react-native";
import { LineChart } from "react-native-chart-kit";

type Props = {
    labels: string[];
    values: number[];
};

const screenWidth = Dimensions.get("window").width;

export function RevenueChart({labels, values}: Props) {

    return (
        <LineChart
            data={{
                labels,
                datasets: [
                    {
                        data: values
                    }
                ]
            }}
            width={screenWidth - 20}
            height={220}
            yAxisSuffix="R$"
            chartConfig={{
                backgroundGradientFrom: "#fff",
                backgroundGradientTo: "#fff",
                decimalPlaces: 0,
                color: (opacity = 1) =>
                    `rgba(37,99,235,${opacity})`
            }}
        />
    );
}