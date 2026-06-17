import React, {
    useRef,
    useState
} from "react";

import {
    FlatList,
    Text,
    TouchableOpacity,
    View
} from "react-native";

import { Modalize } from "react-native-modalize";

import { Input } from "../Input";

type Props<T> = {
    title: string;
    data: T[];
    selectedItem: T | null;
    onSelect: (item: T) => void;

    getKey: (item: T) => string;
    getLabel: (item: T) => string;
};

export function SearchSelectModal<T>({title, data, selectedItem, onSelect,getKey,getLabel}: Props<T>) {

    const modalRef = useRef<Modalize>(null);

    const [search, setSearch] = useState("");

    const filteredData = data.filter(item =>
        getLabel(item)
            .toLowerCase()
            .includes(search.toLowerCase())
    );

    return (
        <>
            <TouchableOpacity
                onPress={() =>
                    modalRef.current?.open()
                }
            >
                <Input
                    title={title}
                    value={selectedItem?getLabel(selectedItem): ""}
                    editable={false}
                />
            </TouchableOpacity>

            <Modalize
                ref={modalRef}
                flatListProps={{
                    data: filteredData,
                    keyExtractor: getKey,
                    renderItem: ({ item }) => (
                        <TouchableOpacity
                            style={{
                                padding: 16,
                                borderBottomWidth: 1
                            }}
                            onPress={() => {
                                onSelect(item);
                                modalRef.current?.close();
                            }}
                        >
                            <Text>
                                {getLabel(item)}
                            </Text>
                        </TouchableOpacity>
                    )
                }}
            >
            </Modalize>
        </>
    );
}