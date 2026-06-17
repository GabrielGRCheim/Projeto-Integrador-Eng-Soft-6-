import React, { useState } from "react";
import {
    View,
    Text,
    Button
} from "react-native";

import { Modalize } from "react-native-modalize";

import { Input } from "../Input";

type Props = {
    modalRef: React.RefObject<Modalize  | null>;
    item: any;
    onConfirm: (quantity: number) => void;
};

export function QuantityModal({modalRef, item, onConfirm}: Props) {

    const [quantity, setQuantity] =
        useState("1");

    return (
        <Modalize
            ref={modalRef}
            modalHeight={400}
        >
            <View
                style={{
                    padding: 20
                }}
            >
                <Text>
                    {item?.name}
                </Text>

                <Input
                    title="Quantidade"
                    value={quantity}
                    onChangeText={setQuantity}
                />

                <Button
                    title="Adicionar"
                    onPress={() => {

                        onConfirm(
                            Number(quantity)
                        );

                        setQuantity("1");

                        modalRef.current?.close();
                    }}
                />
            </View>
        </Modalize>
    );
}