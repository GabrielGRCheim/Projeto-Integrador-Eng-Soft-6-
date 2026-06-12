import { Dimensions } from "react-native";
import {BarChart} from "react-native-chart-kit";
import {styles} from "./styles";

type Props = {
    labels: string[];
    values: number[];
};

const screenWidth = Dimensions.get("window").width;

export function TopServicesChart({labels, values}: Props) {

    return (
        <BarChart
            style={styles.graphStyle}
            data={{
                labels,
                datasets: [
                    {
                        data: values
                    }
                ]
            }}
            width={screenWidth - 32}
            height={220}
            yAxisLabel="$"
            yAxisSuffix=""
            chartConfig={{
                backgroundGradientFrom: "#fff",
                backgroundGradientTo: "#fff",
                decimalPlaces: 0,
                color: (opacity = 1) =>
                    `rgba(37,99,235,${opacity})`
            }}
            showValuesOnTopOfBars
        />
    );
}