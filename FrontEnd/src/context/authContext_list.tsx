import React, {createContext, useContext,useRef} from "react";
import {Alert, Dimensions, Text, TouchableOpacity, View} from "react-native";
import {Modalize} from "react-native-modalize";
import {styles} from "./styles";
import {useNavigation} from "@react-navigation/native";

export const AuthContextList:any = createContext({});

export const AuthProviderList = (props:any):any =>{
    const modalizeRef = useRef<Modalize>(null);

    const onOpen = ()=>{
       modalizeRef?.current?.open();
    }

    const navigation = useNavigation<any>();

    const go = (screenName:string)=>{
        navigation.navigate(screenName);
    }

    const _container = () => {
        return (
            <View style={styles.container}>
                <TouchableOpacity style={styles.button} onPress={()=>go('')}>
                    <Text style={styles.title}>Cadastrar Cliente</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={()=>go('')}>
                    <Text style={styles.title}>Cadastrar Ordem De Serviço</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={()=>go('')}>
                    <Text style={styles.title}>Cadastrar Nota Fiscal</Text>
                </TouchableOpacity>
            </View>
        )
    }

    return (
        <AuthContextList.Provider value={{onOpen}}>
            {props.children}
            <Modalize
                ref={modalizeRef}
                childrenStyle={{height:Dimensions.get("window").height/1.3}}
                adjustToContentHeight={true}
            >
                {_container()}
            </Modalize>
        </AuthContextList.Provider>
    )

}
export const useAuth = ()=> useContext(AuthContextList);