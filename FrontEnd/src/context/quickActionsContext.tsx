import React, {createContext, useContext, useRef} from "react";
import {Dimensions, Text, TouchableOpacity, View} from "react-native";
import {Modalize} from "react-native-modalize";
import {styles} from "./styles";
import {useNavigation} from "@react-navigation/native";

// Observação: este contexto NÃO tem relação com autenticação (no código
// original ele se chamava "AuthContext", o que é enganoso). Ele só controla
// o menu de ações rápidas (bottom-sheet) acionado pelo botão "+" da tab bar,
// com atalhos para cadastrar veículo, cliente e ordem de serviço.

type QuickActionsContextValue = {
    onOpen: () => void;
};

export const QuickActionsContext = createContext<QuickActionsContextValue>({
    onOpen: () => {},
});

export const QuickActionsProvider = (props: {children: React.ReactNode}) => {
    const modalizeRef = useRef<Modalize>(null);

    const onOpen = () => {
        modalizeRef?.current?.open();
    };

    const navigation = useNavigation<any>();

    const go = (screenName: string) => {
        modalizeRef?.current?.close();
        navigation.navigate(screenName);
    };

    const _container = () => {
        return (
            <View style={styles.container}>
                <TouchableOpacity style={styles.button} onPress={() => go("VehicleRegister")}>
                    <Text style={styles.title}>Cadastrar Veiculo</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={() => go("ClientRegister")}>
                    <Text style={styles.title}>Cadastrar Cliente</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={() => go("OrderRegister")}>
                    <Text style={styles.title}>Cadastrar Ordem de Serviço</Text>
                </TouchableOpacity>
            </View>
        );
    };

    return (
        <QuickActionsContext.Provider value={{onOpen}}>
            {props.children}
            <Modalize
                ref={modalizeRef}
                childrenStyle={{height: Dimensions.get("window").height / 1.3}}
                adjustToContentHeight={true}
            >
                {_container()}
            </Modalize>
        </QuickActionsContext.Provider>
    );
};

export const useQuickActions = () => useContext(QuickActionsContext);
