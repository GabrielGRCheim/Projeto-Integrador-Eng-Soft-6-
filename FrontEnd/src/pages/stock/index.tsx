import React, {useCallback, useEffect, useRef, useState} from "react";
import {ActivityIndicator, Alert, FlatList, RefreshControl, Text, TouchableOpacity, View} from "react-native";
import {Modalize} from "react-native-modalize";
import {styles} from "./styles";
import {Input} from "../../components/Input";
import {Button} from "../../components/Button";
import {Peca, TipoMovimentacaoEstoque} from "../../@types/Peca";
import {ajustarEstoque, listarPecas} from "../../services/estoqueService";
import {getApiErrorMessage} from "../../api/api";
import { useFocusEffect } from "@react-navigation/native";

function formatMoeda(valor: number): string {
    return `R$ ${valor.toFixed(2).replace(".", ",")}`;
}

const TIPOS: {id: TipoMovimentacaoEstoque; label: string}[] = [
    {id: "ENTRADA", label: "Entrada"},
    {id: "SAIDA", label: "Saída"},
    {id: "AJUSTE", label: "Ajuste"},
];

export default function Stock() {

    const [pecas, setPecas] = useState<Peca[]>([]);
    const [loading, setLoading] = useState(true);
    const [refreshing, setRefreshing] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const [pecaSelecionada, setPecaSelecionada] = useState<Peca | null>(null);
    const [tipoMovimentacao, setTipoMovimentacao] = useState<TipoMovimentacaoEstoque>("ENTRADA");
    const [quantidade, setQuantidade] = useState("");
    const [motivo, setMotivo] = useState("");
    const [salvando, setSalvando] = useState(false);

    const modalRef = useRef<Modalize>(null);

    const loadPecas = useCallback(async () => {
        try {
            setErrorMessage(null);
            const data = await listarPecas();
            setPecas(data);
        } catch (error) {
            setErrorMessage(getApiErrorMessage(error));
        }
    }, []);

    useFocusEffect(useCallback(() => {
        loadPecas().finally(() => setLoading(false));
    }, [loadPecas]));


    async function onRefresh() {
        setRefreshing(true);
        await loadPecas();
        setRefreshing(false);
    }

    function abrirAjuste(peca: Peca) {
        setPecaSelecionada(peca);
        setTipoMovimentacao("ENTRADA");
        setQuantidade("");
        setMotivo("");
        modalRef.current?.open();
    }

    async function confirmarAjuste() {
        if (!pecaSelecionada) return;

        const quantidadeNumero = Number(quantidade);
        if (!quantidade || isNaN(quantidadeNumero) || quantidadeNumero <= 0) {
            Alert.alert("Atenção", "Informe uma quantidade válida.");
            return;
        }
        if (!motivo.trim()) {
            Alert.alert("Atenção", "Informe o motivo da movimentação.");
            return;
        }

        try {
            setSalvando(true);
            await ajustarEstoque(pecaSelecionada.id, {
                quantidade: quantidadeNumero,
                motivo,
                tipoMovimentacao,
            });
            modalRef.current?.close();
            await loadPecas();
        } catch (error) {
            Alert.alert("Não foi possível ajustar o estoque", getApiErrorMessage(error));
        } finally {
            setSalvando(false);
        }
    }

    if (loading) {
        return (
            <View style={[styles.container, {justifyContent: "center", alignItems: "center"}]}>
                <ActivityIndicator size="large"/>
            </View>
        );
    }

    if (errorMessage) {
        return (
            <View style={[styles.container, {justifyContent: "center", alignItems: "center"}]}>
                <Text style={{textAlign: "center", marginBottom: 12}}>{errorMessage}</Text>
                <TouchableOpacity onPress={loadPecas}>
                    <Text style={{color: "#2563EB", fontWeight: "bold"}}>Tentar novamente</Text>
                </TouchableOpacity>
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <FlatList
                data={pecas}
                keyExtractor={(item) => item.id.toString()}
                refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh}/>}
                ListEmptyComponent={
                    <Text style={{textAlign: "center", marginTop: 40, color: "#6B7280"}}>
                        Nenhuma peça cadastrada.
                    </Text>
                }
                renderItem={({item}) => (
                    <View style={styles.card}>
                        <View style={styles.cardHeader}>
                            <View>
                                <Text style={styles.nome}>{item.nome}</Text>
                                {item.codigo && <Text style={styles.codigo}>{item.codigo}</Text>}
                            </View>
                            {item.estoqueBaixo && (
                                <Text style={styles.estoqueBaixoBadge}>ESTOQUE BAIXO</Text>
                            )}
                        </View>

                        <View style={styles.linha}>
                            <Text style={styles.label}>Em estoque</Text>
                            <Text style={styles.valor}>
                                {item.quantidadeEstoque} {item.unidade ?? "un"}
                            </Text>
                        </View>

                        <View style={styles.linha}>
                            <Text style={styles.label}>Estoque mínimo</Text>
                            <Text style={styles.valor}>{item.quantidadeMinima}</Text>
                        </View>

                        <View style={styles.linha}>
                            <Text style={styles.label}>Preço de venda</Text>
                            <Text style={styles.valor}>{formatMoeda(item.precoVenda)}</Text>
                        </View>

                        <TouchableOpacity style={styles.ajustarButton} onPress={() => abrirAjuste(item)}>
                            <Text style={{color: "#2563EB", fontWeight: "bold"}}>Ajustar estoque</Text>
                        </TouchableOpacity>
                    </View>
                )}
            />

            <Modalize ref={modalRef} adjustToContentHeight>
                <View style={styles.modalContainer}>
                    <Text style={styles.modalTitle}>
                        Ajustar estoque{pecaSelecionada ? `: ${pecaSelecionada.nome}` : ""}
                    </Text>

                    <View style={styles.tipoRow}>
                        {TIPOS.map((tipo) => (
                            <TouchableOpacity
                                key={tipo.id}
                                style={[
                                    styles.tipoButton,
                                    tipoMovimentacao === tipo.id && styles.tipoButtonActive,
                                ]}
                                onPress={() => setTipoMovimentacao(tipo.id)}
                            >
                                <Text
                                    style={[
                                        styles.tipoButtonText,
                                        tipoMovimentacao === tipo.id && styles.tipoButtonTextActive,
                                    ]}
                                >
                                    {tipo.label}
                                </Text>
                            </TouchableOpacity>
                        ))}
                    </View>

                    <Input
                        title="Quantidade"
                        value={quantidade}
                        onChangeText={setQuantidade}
                        keyboardType="numeric"
                    />

                    <Input
                        title="Motivo"
                        value={motivo}
                        onChangeText={setMotivo}
                    />

                    <View style={{marginTop: 20, alignItems: "center"}}>
                        <Button text="Confirmar" loading={salvando} onPress={confirmarAjuste}/>
                    </View>
                </View>
            </Modalize>
        </View>
    )
}
