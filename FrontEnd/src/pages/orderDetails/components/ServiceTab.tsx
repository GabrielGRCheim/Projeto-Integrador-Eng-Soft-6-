import {View, Text, Alert} from "react-native";
import {styles} from "../styles";

import {CheckList} from "./CheckList";
import {Controller, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {DiagnosticSchema} from "../../../containers/DiagnosticForm/DiagnosticSchema";
import {Input} from "../../../components/Input";
import {Button} from "../../../components/Button";
import React, {useEffect, useRef, useState} from "react";
import {Modalize} from "react-native-modalize";
import {SearchSelectModal} from "../../../components/SearchSelectModal";
import {BudgetItemCard} from "../../../components/BugdetItemCard";
import {QuantityModal} from "../../../components/QuantityModal";
import {OrdemServico} from "../../../@types/OrdemServico";
import {Peca} from "../../../@types/Peca";
import {Servico} from "../../../@types/Servico";
import {listarPecas} from "../../../services/estoqueService";
import {listarServicos} from "../../../services/servicoService";
import {adicionarItem, atualizarOrdem, atualizarStatusOrdem, removerItem} from "../../../services/ordemServicoService";
import {getApiErrorMessage} from "../../../api/api";
import {
    STATUS_ORDEM_SERVICO_LABEL,
    STATUS_ORDEM_SERVICO_STEPS,
    StatusOrdemServico,
} from "../../../@types/StatusOrdemServico";

type CatalogItem = {
    id: number;
    name: string;
    price: number;
};

type Props = {
    order: OrdemServico | null;
    onOrderUpdated: () => void;
};

export function ServiceTab({order, onOrderUpdated}: Props) {

    const [parts, setParts] = useState<CatalogItem[]>([]);
    const [services, setServices] = useState<CatalogItem[]>([]);

    const [selectedPart, setSelectedPart] = useState<CatalogItem | null>(null);
    const [selectedService, setSelectedService] = useState<CatalogItem | null>(null);

    const [savingDiagnostico, setSavingDiagnostico] = useState(false);
    const [updatingStatus, setUpdatingStatus] = useState(false);

    const partModalRef = useRef<Modalize>(null);
    const serviceModalRef = useRef<Modalize>(null);

    const {control, handleSubmit, reset} = useForm<DiagnosticSchema>({
        resolver: zodResolver(DiagnosticSchema),
        defaultValues: {diagnostics: order?.diagnostico ?? ""},
    });

    // Mantém o campo de diagnóstico sincronizado quando a OS é recarregada
    // (por exemplo, depois de adicionar um item ou trocar de status).
    useEffect(() => {
        reset({diagnostics: order?.diagnostico ?? ""});
    }, [order?.diagnostico, reset]);

    useEffect(() => {
        async function loadCatalogs() {
            try {
                const [pecas, servicos] = await Promise.all([
                    listarPecas(),
                    listarServicos(),
                ]);

                setParts(
                    pecas.map((item: Peca) => ({
                        id: item.id,
                        name: item.nome,
                        price: item.precoVenda,
                    }))
                );

                setServices(
                    servicos.map((item: Servico) => ({
                        id: item.id,
                        name: item.nome,
                        price: item.precoBase,
                    }))
                );
            } catch (error) {
                Alert.alert("Erro ao carregar catálogo", getApiErrorMessage(error));
            }
        }

        loadCatalogs();
    }, []);

    if (!order) {
        return null;
    }

    // TypeScript não propaga a verificação acima para dentro das funções
    // aninhadas abaixo (closures), então usamos uma constante estável.
    const ordemAtual = order;

    const currentIndex = STATUS_ORDEM_SERVICO_STEPS.indexOf(order.status);
    const isFinalizada = order.status === "CONCLUIDA" || order.status === "CANCELADA";

    async function avancarStatus() {
        const proximo = STATUS_ORDEM_SERVICO_STEPS[currentIndex + 1] as StatusOrdemServico | undefined;
        if (!proximo) return;
        await mudarStatus(proximo);
    }

    async function cancelarOrdem() {
        Alert.alert(
            "Cancelar OS",
            "Tem certeza que deseja cancelar esta ordem de serviço? Essa ação não pode ser desfeita.",
            [
                {text: "Voltar", style: "cancel"},
                {text: "Cancelar OS", style: "destructive", onPress: () => mudarStatus("CANCELADA")},
            ]
        );
    }

    async function mudarStatus(novoStatus: StatusOrdemServico) {
        try {
            setUpdatingStatus(true);
            await atualizarStatusOrdem(ordemAtual.id, novoStatus);
            onOrderUpdated();
        } catch (error) {
            Alert.alert("Não foi possível atualizar o status", getApiErrorMessage(error));
        } finally {
            setUpdatingStatus(false);
        }
    }

    async function salvarDiagnostico(data: DiagnosticSchema) {
        try {
            setSavingDiagnostico(true);
            await atualizarOrdem(ordemAtual.id, {
                carroId: ordemAtual.carroId,
                diagnostico: data.diagnostics,
                queixaCliente: ordemAtual.queixaCliente,
                valorMaoObra: ordemAtual.valorMaoObra,
            });
            onOrderUpdated();
            Alert.alert("Sucesso", "Diagnóstico salvo.");
        } catch (error) {
            Alert.alert("Não foi possível salvar o diagnóstico", getApiErrorMessage(error));
        } finally {
            setSavingDiagnostico(false);
        }
    }

    async function confirmarAdicaoPeca(quantidade: number) {
        if (!selectedPart) return;
        try {
            await adicionarItem({
                ordemServicoId: ordemAtual.id,
                pecaId: selectedPart.id,
                quantidade,
                valorUnitario: selectedPart.price,
            });
            onOrderUpdated();
        } catch (error) {
            Alert.alert("Não foi possível adicionar a peça", getApiErrorMessage(error));
        }
    }

    async function confirmarAdicaoServico(quantidade: number) {
        if (!selectedService) return;
        try {
            await adicionarItem({
                ordemServicoId: ordemAtual.id,
                servicoId: selectedService.id,
                quantidade,
                valorUnitario: selectedService.price,
            });
            onOrderUpdated();
        } catch (error) {
            Alert.alert("Não foi possível adicionar o serviço", getApiErrorMessage(error));
        }
    }

    async function removerItemDaOrdem(itemId: number) {
        try {
            await removerItem(itemId);
            onOrderUpdated();
        } catch (error) {
            Alert.alert("Não foi possível remover o item", getApiErrorMessage(error));
        }
    }

    const itensPecas = order.itens.filter((item) => item.pecaId);
    const itensServicos = order.itens.filter((item) => item.servicoId);

    return (
        <View>
            <View style={styles.stepperContainer}>
                {STATUS_ORDEM_SERVICO_STEPS.map((step, index) => (
                    <View
                        key={step}
                        style={styles.stepWrapper}
                    >
                        <View
                            style={[
                                styles.circle,
                                index <= currentIndex &&
                                styles.circleActive
                            ]}
                        />

                        <Text
                            style={[
                                styles.label,
                                index <= currentIndex &&
                                styles.labelActive
                            ]}
                        >
                            {STATUS_ORDEM_SERVICO_LABEL[step]}
                        </Text>

                        {index < STATUS_ORDEM_SERVICO_STEPS.length - 1 && (
                            <View
                                style={[
                                    styles.line,
                                    index < currentIndex &&
                                    styles.lineActive
                                ]}
                            />
                        )}
                    </View>
                ))}
            </View>

            {order.status === "CANCELADA" && (
                <Text style={{color: "#EF4444", fontWeight: "bold", marginBottom: 10}}>
                    Esta ordem de serviço foi cancelada.
                </Text>
            )}

            <View>
                {/* O checklist abaixo é só local — o back-end ainda não tem
                    nenhum campo/endpoint para checklist de entrada do veículo. */}
                <CheckList/>
            </View>

            <View>
                <Controller
                    control={control}
                    name="diagnostics"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Diagnóstico"
                            value={value}
                            onChangeText={onChange}
                            editable={!isFinalizada}
                        />
                    )}
                />
                {!isFinalizada && (
                    <Button
                        text="Salvar Diagnóstico"
                        loading={savingDiagnostico}
                        onPress={handleSubmit(salvarDiagnostico)}
                    />
                )}
            </View>

            {!isFinalizada && (
                <View>
                    <SearchSelectModal
                        title="Peça"
                        data={parts}
                        selectedItem={null}
                        onSelect={(part) => {
                            setSelectedPart(part);
                            partModalRef.current?.open();
                        }}
                        getKey={(part) => part.id.toString()}
                        getLabel={(part) => part.name}
                    />

                    <QuantityModal
                        modalRef={partModalRef}
                        item={selectedPart}
                        onConfirm={confirmarAdicaoPeca}
                    />

                    <SearchSelectModal
                        title="Serviço"
                        data={services}
                        selectedItem={null}
                        onSelect={(service) => {
                            setSelectedService(service);
                            serviceModalRef.current?.open();
                        }}
                        getKey={(service) => service.id.toString()}
                        getLabel={(service) => service.name}
                    />

                    <QuantityModal
                        modalRef={serviceModalRef}
                        item={selectedService}
                        onConfirm={confirmarAdicaoServico}
                    />
                </View>
            )}

            <Text style={{marginTop: 10, fontWeight: "bold"}}>
                Peças adicionadas
            </Text>

            {itensPecas.length === 0 && (
                <Text style={{color: "#6B7280"}}>Nenhuma peça adicionada.</Text>
            )}

            {itensPecas.map((item) => (
                <BudgetItemCard
                    key={item.id}
                    name={item.nomePeca ?? "Peça"}
                    quantity={item.quantidade}
                    unitPrice={item.valorUnitario}
                    onRemove={isFinalizada ? undefined : () => removerItemDaOrdem(item.id)}
                />
            ))}

            <Text style={{marginTop: 10, fontWeight: "bold"}}>
                Serviços adicionados
            </Text>

            {itensServicos.length === 0 && (
                <Text style={{color: "#6B7280"}}>Nenhum serviço adicionado.</Text>
            )}

            {itensServicos.map((item) => (
                <BudgetItemCard
                    key={item.id}
                    name={item.nomeServico ?? "Serviço"}
                    quantity={item.quantidade}
                    unitPrice={item.valorUnitario}
                    onRemove={isFinalizada ? undefined : () => removerItemDaOrdem(item.id)}
                />
            ))}

            {!isFinalizada && (
                <View style={{marginTop: 20, gap: 10, alignItems: "center"}}>
                    {currentIndex < STATUS_ORDEM_SERVICO_STEPS.length - 1 && (
                        <Button
                            text={`Avançar para "${STATUS_ORDEM_SERVICO_LABEL[STATUS_ORDEM_SERVICO_STEPS[currentIndex + 1]]}"`}
                            loading={updatingStatus}
                            onPress={avancarStatus}
                        />
                    )}
                    <Text style={{color: "#EF4444"}} onPress={cancelarOrdem}>
                        Cancelar OS
                    </Text>
                </View>
            )}
        </View>
    );
}
