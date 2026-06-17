import React from "react";
import { View, Text, TouchableOpacity } from "react-native";
import { styles } from "./styles";
import { Ionicons } from "@expo/vector-icons";

type Props = {
    title: string; // nome da tela/aba
    onLogout: () => void;
};

export function AppHeader({ title, onLogout }: Props) {
    return (
        <View style={styles.container}>
            <View>
                <Text style={styles.company}>Oficina</Text>
                <Text style={styles.page}>{title}</Text>
            </View>

            <TouchableOpacity onPress={onLogout} style={styles.logoutButton}>
                <Ionicons name="log-out-outline" size={22} color="#EF4444" />
            </TouchableOpacity>
        </View>
    );
}