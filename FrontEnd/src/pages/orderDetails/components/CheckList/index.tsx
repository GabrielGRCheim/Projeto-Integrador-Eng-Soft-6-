import {FlatList, Text, View} from "react-native";
import {Checkbox} from "expo-checkbox";
import {useState} from "react";


export function CheckList() {

    const [checklist, setChecklist] = useState([
        {
            id: 1,
            name: "Estepe",
            checked: false
        },
        {
            id: 2,
            name: "Macaco",
            checked: false
        },
        {
            id: 3,
            name: "Triângulo",
            checked: false
        },
    ]);

    return (
        <View>
            <Text> Checklist</Text>
            <FlatList
                data={checklist}
                keyExtractor={(item) => item.id.toString()}
                renderItem={({ item }) => (

                    <View
                        style={{
                            flexDirection: 'row',
                            alignItems: 'center',
                            marginBottom: 10
                        }}
                    >
                        <Checkbox
                            value={item.checked}
                            onValueChange={() => {

                                setChecklist(prev =>
                                    prev.map(check =>
                                        check.id === item.id
                                            ? {
                                                ...check,
                                                checked: !check.checked
                                            }
                                            : check
                                    )
                                );

                            }}
                        />

                        <Text
                            style={{
                                marginLeft: 10
                            }}
                        >
                            {item.name}
                        </Text>
                    </View>
                )}
            />
        </View>
    )
}