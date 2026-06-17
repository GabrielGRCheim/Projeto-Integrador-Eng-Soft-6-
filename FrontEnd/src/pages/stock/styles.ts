import {StyleSheet} from "react-native";
import {themas} from "../../global/themes";

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: themas.colors.bdScreen,
        padding: 16,
    },
    card: {
        backgroundColor: "#FFF",
        borderRadius: 12,
        padding: 16,
        marginBottom: 10,
        elevation: 2,
    },
    cardHeader: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
    },
    nome: {
        fontSize: 16,
        fontWeight: "bold",
    },
    codigo: {
        color: themas.colors.gray,
        fontSize: 12,
    },
    linha: {
        flexDirection: "row",
        justifyContent: "space-between",
        marginTop: 8,
    },
    label: {
        color: themas.colors.gray,
    },
    valor: {
        fontWeight: "bold",
    },
    estoqueBaixoBadge: {
        backgroundColor: "#FEE2E2",
        color: "#B91C1C",
        fontSize: 11,
        fontWeight: "bold",
        paddingHorizontal: 8,
        paddingVertical: 2,
        borderRadius: 10,
        overflow: "hidden",
    },
    ajustarButton: {
        marginTop: 12,
        alignSelf: "flex-end",
    },
    modalContainer: {
        padding: 20,
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: "bold",
        marginBottom: 10,
    },
    tipoRow: {
        flexDirection: "row",
        gap: 8,
        marginTop: 10,
        marginBottom: 10,
    },
    tipoButton: {
        flex: 1,
        paddingVertical: 10,
        borderRadius: 8,
        borderWidth: 1,
        borderColor: themas.colors.lightGray,
        alignItems: "center",
    },
    tipoButtonActive: {
        backgroundColor: themas.colors.primary,
        borderColor: themas.colors.primary,
    },
    tipoButtonText: {
        color: "#000",
    },
    tipoButtonTextActive: {
        color: "#FFF",
        fontWeight: "bold",
    },
});
