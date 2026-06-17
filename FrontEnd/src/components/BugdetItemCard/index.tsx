import React from "react";
import { View, Text, TouchableOpacity } from "react-native";

type Props = {
    name: string;
    quantity: number;
    unitPrice: number;
    onRemove?: () => void;
};

export function BudgetItemCard({
                                   name,
                                   quantity,
                                   unitPrice,
                                   onRemove,
                               }: Props) {

    const total = quantity * unitPrice;

    return (
        <View
            style={{
                padding: 12,
                marginTop: 10,
                borderWidth: 1,
                borderRadius: 8
            }}
        >
            <View style={{flexDirection: "row", justifyContent: "space-between"}}>
                <Text style={{fontWeight: "bold"}}>{name}</Text>
                {onRemove && (
                    <TouchableOpacity onPress={onRemove}>
                        <Text style={{color: "#EF4444"}}>Remover</Text>
                    </TouchableOpacity>
                )}
            </View>

            <Text>
                Quantidade: {quantity}
            </Text>

            <Text>
                Unitário: R$ {unitPrice.toFixed(2)}
            </Text>

            <Text>
                Total: R$ {total.toFixed(2)}
            </Text>
        </View>
    );
}
