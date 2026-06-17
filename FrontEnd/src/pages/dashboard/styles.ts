import { StyleSheet } from "react-native";
import {themas} from "../../global/themes";

export const styles = StyleSheet.create({
    container: {
            flex: 1,
            backgroundColor: "#F6F7FB",
            paddingHorizontal: 16,
    },
    boxCards: {
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
    },
    section: {
        marginTop: 20,
    },
    sectionTitle: {
        fontSize: 16,
        fontWeight: "600",
        marginBottom: 10,
        color: "#374151",
    },
    chartCard: {
        backgroundColor: "#fff",
        borderRadius: 16,
        padding: 12,

        shadowColor: "#000",
        shadowOpacity: 0.08,
        shadowRadius: 10,
        shadowOffset: { width: 0, height: 4 },

        elevation: 3,
    },
});