import { StyleSheet } from "react-native";
import {themas} from "../../global/themes";

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "#F6F7FB",
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
    tabs: {
        flexDirection: 'row',
        marginBottom: 20,
        width: '100%',
    },
    title: {
        fontSize: 16,
        padding: 10,
    },
    button: {
        borderWidth: 1,
        borderColor: "#000",
        padding: 10,
    },
    boxTop: {
        flex: 1,
        marginTop: 200,
    },
    stepperContainer: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
        marginVertical: 20,
    },

    stepWrapper: {
        flex: 1,
        alignItems: "center",
    },

    circle: {
        width: 20,
        height: 20,
        borderRadius: 10,
        backgroundColor: "#D1D5DB",
    },

    circleActive: {
        backgroundColor: "#2563EB",
    },

    line: {
        position: "absolute",
        top: 10,
        left: "60%",
        width: "80%",
        height: 3,
        backgroundColor: "#D1D5DB",
    },

    lineActive: {
        backgroundColor: "#2563EB",
    },

    label: {
        marginTop: 8,
        fontSize: 10,
        color: "#6B7280",
        textAlign: "center",
    },

    labelActive: {
        color: "#2563EB",
        fontWeight: "bold",
    },tabsContainer: {
        flexDirection: "row",
        backgroundColor: "#fff",
        borderRadius: 12,
        padding: 4,

        shadowColor: "#000",
        shadowOpacity: 0.05,
        shadowRadius: 10,
        elevation: 2,
    },

    tabButton: {
        flex: 1,
        paddingVertical: 10,
        alignItems: "center",
        borderRadius: 10,
    },

    tabButtonActive: {
        backgroundColor: "#EFF6FF",
    },

    tabText: {
        fontSize: 13,
        color: "#6B7280",
        fontWeight: "500",
    },

    tabTextActive: {
        color: "#2563EB",
        fontWeight: "700",
    },

    indicator: {
        marginTop: 4,
        width: 20,
        height: 3,
        borderRadius: 2,
        backgroundColor: "#2563EB",
    },
});