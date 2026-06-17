import React, {useState} from "react";
import {Alert, ScrollView, View} from "react-native";
import {Controller, useForm} from "react-hook-form";
import {Input} from "../../../components/Input";
import {Button} from "../../../components/Button";
import {singUpSchema, SingUpSchema} from "../../../containers/SingUpForm/SingUpSchema";
import {zodResolver} from "@hookform/resolvers/zod";
import {FontAwesome} from "@expo/vector-icons";
import {styles} from "./styles";
import {criarUsuario} from "../../../services/usuarioService";
import {getApiErrorMessage} from "../../../api/api";
import {useNavigation} from "@react-navigation/native";


export default function SingUpRegisterView() {

    const navigation = useNavigation<any>();

    const [showPassword, setShowPassword] = useState(true);
    const [loading, setLoading] = useState(false);
    const [selectedPerfil, setSelectedPerfil] = useState<{nome: string} | null>(null);

    const {control, handleSubmit, setValue} = useForm<SingUpSchema>({
        resolver: zodResolver(singUpSchema),
    });

    async function onSubmit(data: SingUpSchema) {
        try {
            setLoading(true);

            await criarUsuario({
                nome: data.nome,
                email: data.email,
                senha: data.senha,
                perfil: "ADMINISTRADOR"
            });

            // O back-end ainda não tem endpoint de login/autenticação — por isso
            // não é possível "logar automaticamente" depois de cadastrar. O usuário
            // criado aqui só poderá ser usado de fato quando essa peça existir no
            // back-end. Por ora, voltamos para a tela de Login.
            Alert.alert("Conta criada", "Usuário cadastrado com sucesso!");
            navigation.navigate("Login");

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
                    name="senha"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Senha"
                            value={value}
                            onChangeText={onChange}
                            IconRigth={FontAwesome}
                            iconRightName={showPassword ? "eye-slash" : "eye"}
                            secureTextEntry={showPassword}
                            onIconRigthPress={() => setShowPassword(!showPassword)}
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="confirmarSenha"
                    render={({field: {onChange, value}}) => (
                        <Input
                            title="Confirmar Senha"
                            value={value}
                            onChangeText={onChange}
                            IconRigth={FontAwesome}
                            iconRightName={showPassword ? "eye-slash" : "eye"}
                            secureTextEntry={showPassword}
                            onIconRigthPress={() => setShowPassword(!showPassword)}
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
