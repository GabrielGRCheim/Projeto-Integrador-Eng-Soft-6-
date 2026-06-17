import React, {useEffect, useState} from "react";

import {Alert, ScrollView, View} from "react-native";

import {Controller, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {styles} from "./styles";
import {Input} from "../../../components/Input";
import {Button} from "../../../components/Button";
import {OrderSchema} from "../../../containers/OrderForm/OrderSchema";
import {SearchSelectModal} from "../../../components/SearchSelectModal";
import {Cliente} from "../../../@types/Cliente";
import {Carro} from "../../../@types/Carro";
import {listarClientes} from "../../../services/clienteService";
import {listarCarrosPorCliente} from "../../../services/carroService";
import {criarOrdem} from "../../../services/ordemServicoService";
import {getApiErrorMessage} from "../../../api/api";
import {useNavigation} from "@react-navigation/native";

export default function OrderRegister() {

    const navigation = useNavigation<any>();

    const {
        control,
        handleSubmit,
        setValue,
    } = useForm<OrderSchema>({
        resolver: zodResolver(OrderSchema),
        defaultValues: {
            queixaCliente: "",
            valorMaoObra: "",
        }
    });

    const [loading, setLoading] = useState(false);

    const [clientes, setClientes] = useState<Cliente[]>([]);
    const [selectedCliente, setSelectedCliente] = useState<Cliente | null>(null);

    const [carros, setCarros] = useState<Carro[]>([]);
    const [selectedCarro, setSelectedCarro] = useState<Carro | null>(null);
    const [loadingCarros, setLoadingCarros] = useState(false);

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

    async function onSelectCliente(cliente: Cliente) {
        setSelectedCliente(cliente);
        setSelectedCarro(null);
        setCarros([]);

        try {
            setLoadingCarros(true);
            const data = await listarCarrosPorCliente(cliente.id);
            setCarros(data);
        } catch (error) {
            Alert.alert("Erro ao carregar veículos do cliente", getApiErrorMessage(error));
        } finally {
            setLoadingCarros(false);
        }
    }

    async function onSubmit(data: OrderSchema) {
        try {
            setLoading(true);

            await criarOrdem({
                carroId: data.carroId,
                queixaCliente: data.queixaCliente,
                valorMaoObra: data.valorMaoObra ? Number(data.valorMaoObra) : undefined,
            });

            Alert.alert("Sucesso", "Ordem de serviço aberta com sucesso!");
            navigation.goBack();

        } catch (error) {
            Alert.alert("Não foi possível abrir a OS", getApiErrorMessage(error));
        } finally {
            setLoading(false);
        }
    }

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <View style={{flex: 1, padding: 16}}>

                {/* CLIENTE — usado só para filtrar a lista de veículos abaixo;
                    o back-end não recebe clientId, apenas carroId. */}
                <SearchSelectModal
                    title="Cliente"
                    data={clientes}
                    selectedItem={selectedCliente}
                    onSelect={onSelectCliente}
                    getKey={(cliente) => cliente.id.toString()}
                    getLabel={(cliente) => cliente.nome}
                />

                {/* VEÍCULO */}
                <SearchSelectModal
                    title={loadingCarros ? "Carregando veículos..." : "Veículo"}
                    data={carros}
                    selectedItem={selectedCarro}
                    onSelect={(carro) => {
                        setSelectedCarro(carro);
                        setValue("carroId", carro.id);
                    }}
                    getKey={(carro) => carro.id.toString()}
                    getLabel={(carro) => `${carro.marca} ${carro.modelo} - ${carro.placa}`}
                />

                {/* SOLICITAÇÃO DO CLIENTE */}
                <Controller
                    control={control}
                    name="queixaCliente"
                    render={({field: {value, onChange}}) => (
                        <Input
                            title="Solicitação do Cliente"
                            value={value}
                            onChangeText={onChange}
                        />
                    )}
                />

                {/* VALOR DA MÃO DE OBRA (opcional, peças/serviços entram depois) */}
                <Controller
                    control={control}
                    name="valorMaoObra"
                    render={({field: {value, onChange}}) => (
                        <Input
                            title="Valor da Mão de Obra (opcional)"
                            value={value}
                            onChangeText={onChange}
                            keyboardType="numeric"
                        />
                    )}
                />

                <Button
                    text="Salvar Ordem de Serviço"
                    loading={loading}
                    onPress={handleSubmit(onSubmit)}
                />
            </View>
        </ScrollView>
    )
}
