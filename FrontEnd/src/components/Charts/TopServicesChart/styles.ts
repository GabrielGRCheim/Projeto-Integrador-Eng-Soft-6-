import { StyleSheet } from "react-native";


export const styles = StyleSheet.create({
    boxCards: {
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
    },
    graphStyle: {
        marginVertical: 16,
        borderRadius: 16,
        paddingRight: 16, // Essencial para o BarChart não cortar o eixo Y
    }
});