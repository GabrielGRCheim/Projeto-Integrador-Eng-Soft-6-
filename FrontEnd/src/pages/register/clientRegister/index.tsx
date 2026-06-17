import React, {useState} from "react";

import {Alert, ScrollView, View} from "react-native";

import {Controller, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {styles} from "./styles";
import {Input} from "../../../components/Input";
import {Button} from "../../../components/Button";
import {ClientSchema} from "../../../containers/ClientForm/ClientSchema";
import {criarCliente} from "../../../services/clienteService";
import {getApiErrorMessage} from "../../../api/api";
import {useNavigation} from "@react-navigation/native";

export default function ClientRegister() {

    const navigation = useNavigation<any>();

    const [loading, setLoading] = useState(false);

    const {control, handleSubmit} = useForm<ClientSchema>({
        resolver: zodResolver(ClientSchema),
        defaultValues: {
            nome: "",
            cpf: "",
            telefone: "",
            email: "",
            endereco: "",
        },
    });

    async function onSubmit(data: ClientSchema) {
        try {
            setLoading(true);

            await criarCliente({
                nome: data.nome,
                cpf: data.cpf,
                telefone: data.telefone || undefined,
                email: data.email || undefined,
                endereco: data.endereco || undefined,
            });

            Alert.alert("Sucesso", "Cliente cadastrado com sucesso!");
            navigation.goBack();

        } catch (error) {
            Alert.alert("Não foi possível cadastrar", getApiErrorMessage(error));
        } finally {
            setLoading(false);
        }
    }

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <View style={styles.boxTop}>
                <Controller
                    control={control}
                    name="nome"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Nome Completo"
                            value={value}
                            onChangeText={onChange}
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="cpf"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="CPF"
                            value={value}
                            onChangeText={onChange}
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="telefone"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Telefone"
                            value={value}
                            onChangeText={onChange}
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="email"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Email"
                            value={value}
                            onChangeText={onChange}
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="endereco"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Endereço"
                            value={value}
                            onChangeText={onChange}
                        />
                    )}
                />
            </View>
            <View style={styles.boxBottom}>
                <Button
                    text="Cadastrar"
                    loading={loading}
                    onPress={handleSubmit(onSubmit)}
                />
            </View>
        </ScrollView>
    )
}
