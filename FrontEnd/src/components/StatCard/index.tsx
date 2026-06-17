import { View, Text } from "react-native";
import { styles } from "./styles";

type Props = {
    title: string;
    value: string;
    valueColor?: string;
};

export function StatCard({ title, value, valueColor }: Props) {
    return (
        <View style={styles.container}>
            <Text style={[styles.value, valueColor ? {color: valueColor} : null]}>{value}</Text>
            <Text style={styles.title}>{title}</Text>
        </View>
    );
}
