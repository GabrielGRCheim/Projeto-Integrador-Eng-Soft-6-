import axios from "axios";
import {Platform} from "react-native";

// O endereço do back-end varia de acordo com onde o app está rodando:
// - Emulador Android -> 10.0.2.2 é o "localhost" da máquina host dentro do emulador
// - Simulador iOS / Expo Web -> "localhost" funciona normalmente
// - Dispositivo físico (Expo Go/dev build) -> nenhum dos dois funciona; é preciso
//   usar o IP da máquina na rede local (ex: http://192.168.0.10:8080/api)
//
// Para não precisar editar código a cada cenário, defina EXPO_PUBLIC_API_URL
// num arquivo .env na raiz do projeto (o Expo expõe automaticamente qualquer
// variável que comece com EXPO_PUBLIC_ via process.env):
//   EXPO_PUBLIC_API_URL=http://192.168.0.10:8080/api
const ANDROID_EMULATOR_URL = "http://10.0.2.2:8080/api";
const LOCALHOST_URL = "http://localhost:8080/api";

function resolveBaseURL(): string {
    const fromEnv = process.env.EXPO_PUBLIC_API_URL;
    if (fromEnv) {
        return fromEnv;
    }
    return Platform.OS === "android" ? ANDROID_EMULATOR_URL : LOCALHOST_URL;
}

export const api = axios.create({
    baseURL: resolveBaseURL(),
    timeout: 15000,
});

// Formato de erro padronizado devolvido pelo GlobalExceptionHandler do back-end:
// { timestamp, status, erro, mensagem }
export type ApiErrorBody = {
    timestamp?: string;
    status?: number;
    erro?: string;
    mensagem?: string;
};

// Extrai uma mensagem amigável de um erro do axios, priorizando a mensagem
// que o back-end já devolve pronta em português (campo "mensagem").
export function getApiErrorMessage(
    error: unknown,
    fallback = "Não foi possível completar a operação. Tente novamente."
): string {
    if (axios.isAxiosError(error)) {
        if (!error.response) {
            return "Não foi possível conectar ao servidor. Verifique sua internet e tente novamente.";
        }
        const body = error.response.data as ApiErrorBody | undefined;
        if (body?.mensagem) {
            return body.mensagem;
        }
    }
    return fallback;
}
