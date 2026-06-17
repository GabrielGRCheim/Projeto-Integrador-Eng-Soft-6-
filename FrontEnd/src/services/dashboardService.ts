import {listarOrdens} from "./ordemServicoService";
import {DashboardData, OrderStatusData, RevenueData, TopService} from "../@types/dashboard";

// O back-end não tem (ainda) um DashboardController com agregações prontas.
// Em vez de mockar os números (como na versão anterior do app), calculamos
// tudo aqui a partir de dados reais já disponíveis em GET /ordens-servico
// (que já vem com status, valores e itens de cada OS).
//
// Quando o back-end ganhar endpoints de agregação de verdade, basta trocar
// a implementação de getDashboardData() por uma chamada à API — a forma
// (DashboardData) pode continuar a mesma.

const MESES_ABREVIADOS = [
    "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
    "Jul", "Ago", "Set", "Out", "Nov", "Dez",
];

// O back-end formata datas como "dd/MM/yyyy HH:mm:ss" (ver OrdemServicoService),
// formato que o construtor padrão de Date não interpreta de forma confiável.
function parseDataBR(valor?: string): Date | null {
    if (!valor) return null;
    const [dataParte, horaParte] = valor.split(" ");
    const [dia, mes, ano] = (dataParte ?? "").split("/").map(Number);
    if (!dia || !mes || !ano) return null;
    const [hora = 0, minuto = 0, segundo = 0] = (horaParte ?? "").split(":").map(Number);
    return new Date(ano, mes - 1, dia, hora, minuto, segundo);
}

export async function getDashboardData(): Promise<DashboardData> {
    const ordens = await listarOrdens();

    const orderStatus: OrderStatusData = {
        ABERTA: 0,
        EM_ANDAMENTO: 0,
        AGUARDANDO_PECA: 0,
        CONCLUIDA: 0,
        CANCELADA: 0,
    };

    const carrosEmAndamento = new Set<number>();
    let ordensAbertas = 0;
    let faturamentoTotal = 0;
    let totalPecas = 0;

    const receitaPorMes = new Map<string, {label: string; value: number; ordenacao: number}>();
    const quantidadePorServico = new Map<string, number>();

    for (const ordem of ordens) {
        orderStatus[ordem.status] += 1;

        const finalizada = ordem.status === "CONCLUIDA" || ordem.status === "CANCELADA";
        if (!finalizada) {
            ordensAbertas += 1;
            carrosEmAndamento.add(ordem.carroId);
        }

        for (const item of ordem.itens) {
            if (item.pecaId) {
                totalPecas += item.valorTotal;
            }
            if (item.servicoId && item.nomeServico) {
                const atual = quantidadePorServico.get(item.nomeServico) ?? 0;
                quantidadePorServico.set(item.nomeServico, atual + item.quantidade);
            }
        }

        if (ordem.status === "CONCLUIDA") {
            faturamentoTotal += ordem.valorTotal;

            const data = parseDataBR(ordem.concluidoEm) ?? parseDataBR(ordem.criadoEm);
            if (data) {
                const chave = `${data.getFullYear()}-${data.getMonth()}`;
                const label = MESES_ABREVIADOS[data.getMonth()];
                const ordenacao = data.getFullYear() * 12 + data.getMonth();
                const atual = receitaPorMes.get(chave);
                if (atual) {
                    atual.value += ordem.valorTotal;
                } else {
                    receitaPorMes.set(chave, {label, value: ordem.valorTotal, ordenacao});
                }
            }
        }
    }

    const revenue: RevenueData[] = Array.from(receitaPorMes.values())
        .sort((a, b) => a.ordenacao - b.ordenacao)
        .map(({label, value}) => ({month: label, value}));

    const topServices: TopService[] = Array.from(quantidadePorServico.entries())
        .map(([service, quantity]) => ({service, quantity}))
        .sort((a, b) => b.quantity - a.quantity)
        .slice(0, 5);

    return {
        stats: {
            veiculosNoPatio: carrosEmAndamento.size,
            ordensAbertas,
            faturamentoTotal,
            totalPecas,
        },
        revenue,
        orderStatus,
        topServices,
    };
}
