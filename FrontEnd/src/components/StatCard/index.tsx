import { View, Text } from "react-native";
import { styles } from "./styles";

type Props = {
    title: string;
    value: string;
};

export function StatCard({ title, value }: Props) {
    return (
        <View style={styles.container}>
            <Text style={styles.value}>{value}</Text>
            <Text style={styles.title}>{title}</Text>
        </View>
    );
}