import React from "react";
import { TouchableOpacity, View, Text } from "react-native";
import { styles } from "../styles";

type Props = {
    activeTab: string;
    onChange: (tab: string) => void;
};

const tabs = [
    { key: "dados", label: "Dados" },
    { key: "servico", label: "Serviço" },
    { key: "financeiro", label: "Financeiro" },
];

export function OrderTabs({ activeTab, onChange }: Props) {
    return (
        <View style={styles.tabsContainer}>
            {tabs.map((tab) => {
                const active = activeTab === tab.key;

                return (
                    <TouchableOpacity
                        key={tab.key}
                        onPress={() => onChange(tab.key)}
                        style={[styles.tabButton, active && styles.tabButtonActive]}
                    >
                        <Text style={[styles.tabText, active && styles.tabTextActive]}>
                            {tab.label}
                        </Text>

                        {active && <View style={styles.indicator} />}
                    </TouchableOpacity>
                );
            })}
        </View>
    );
}