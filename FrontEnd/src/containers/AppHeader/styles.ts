import { StyleSheet } from "react-native";

export const styles = StyleSheet.create({
    container: {
        width: "100%",
        paddingHorizontal: 16,
        paddingVertical: 14,
        backgroundColor: "#fff",

        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",

        borderBottomWidth: 1,
        borderBottomColor: "#E5E7EB",
    },

    company: {
        fontSize: 16,
        fontWeight: "700",
        color: "#111827",
    },

    page: {
        fontSize: 12,
        color: "#6B7280",
        marginTop: 2,
    },

    logoutButton: {
        padding: 8,
        borderRadius: 8,
    },
});