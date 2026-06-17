import React, {useEffect, useState} from "react";

import {Alert, ScrollView, View} from "react-native";
import {VehicleSchema} from "../../../containers/VehicleForm/VehicleSchema";
import {Controller, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {styles} from "./styles";
import {Input} from "../../../components/Input";
import {Button} from "../../../components/Button";
import {SearchSelectModal} from "../../../components/SearchSelectModal";
import {criarCarro} from "../../../services/carroService";
import {listarClientes} from "../../../services/clienteService";
import {Cliente} from "../../../@types/Cliente";
import {getApiErrorMessage} from "../../../api/api";
import {useNavigation} from "@react-navigation/native";

export default function VehicleRegister() {

    const navigation = useNavigation<any>();

    const [loading, setLoading] = useState(false);
    const [clientes, setClientes] = useState<Cliente[]>([]);
    const [selectedCliente, setSelectedCliente] = useState<Cliente | null>(null);

    const {control, handleSubmit, setValue} = useForm<VehicleSchema>({
        resolver: zodResolver(VehicleSchema),
        defaultValues: {
            marca: "",
            modelo: "",
            ano: "",
            placa: "",
            cor: "",
            chassi: "",
            quilometragem: "",
        },
    });

    useEffect(() => {
        async function loadClientes() {
            try {
                const data = await listarClientes();
                setClientes(data);
            } catch (error) {
                Alert.alert("Erro ao carregar clientes", getApiErrorMessage(error));
            }
        }

        loadClientes();
    }, []);

    async function onSubmit(data: VehicleSchema) {
        try {
            setLoading(true);

            await criarCarro({
                marca: data.marca,
                modelo: data.modelo,
                ano: Number(data.ano),
                placa: data.placa,
                cor: data.cor || undefined,
                chassi: data.chassi || undefined,
                quilometragem: data.quilometragem ? Number(data.quilometragem) : undefined,
                clienteId: data.clienteId,
            });

            Alert.alert("Sucesso", "Veículo cadastrado com sucesso!");
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
                <SearchSelectModal
                    title="Cliente"
                    data={clientes}
                    selectedItem={selectedCliente}
                    onSelect={(cliente) => {
                        setSelectedCliente(cliente);
                        setValue("clienteId", cliente.id);
                    }}
                    getKey={(cliente) => cliente.id.toString()}
                    getLabel={(cliente) => cliente.nome}
                />
                <Controller
                    control={control}
                    name="marca"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Fabricante"
                            value={value}
                            onChangeText={onChange}
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="modelo"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Modelo"
                            value={value}
                            onChangeText={onChange}
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="ano"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Ano"
                            value={value}
                            onChangeText={onChange}
                            keyboardType="numeric"
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="placa"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Placa"
                            value={value}
                            onChangeText={onChange}
                            autoCapitalize="characters"
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="cor"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Cor"
                            value={value}
                            onChangeText={onChange}
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="chassi"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Chassi"
                            value={value}
                            onChangeText={onChange}
                            autoCapitalize="characters"
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="quilometragem"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Quilometragem"
                            value={value}
                            onChangeText={onChange}
                            keyboardType="numeric"
                        />
                    )}
                />
            </View>
            <View style={styles.boxBottom}>
                <Button
                    text="Cadastrar"
                    loading={loading}
                    onPress={() => {
                        handleSubmit(
                            onSubmit,
                            (errors) => console.log("ERROS ZOD:", errors)
                        )();
                    }}
                />
            </View>
        </ScrollView>
    )
}
