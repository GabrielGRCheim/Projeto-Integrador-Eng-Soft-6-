import React, {useState} from "react";
import {
    Image,
    Text,
    View,
    Alert, TouchableOpacity,
} from 'react-native';
import {style} from "./styles";
import Logo from "../../assets/imagens/logo.png";
import {FontAwesome} from '@expo/vector-icons';
import {Input} from "../../components/Input";
import {Button} from "../../components/Button";
import {useNavigation,NavigationProp} from "@react-navigation/native";

// ATENÇÃO: o back-end ainda não tem endpoint de login/autenticação (sem JWT,
// sem sessão — apenas CRUD de usuário em /api/usuarios). Por isso esta tela
// continua sem chamar a API: preencher os dois campos só avança para a área
// logada, sem validar usuário/senha de fato. Quando o back-end ganhar um
// endpoint de autenticação, troque getLogin() por uma chamada real e guarde
// o token retornado (ex: em SecureStore) para ser enviado nas próximas
// requisições.
export default function Login() {

    const navigation = useNavigation<NavigationProp<any>>();

    const [usuario,setUsuario] = useState('');
    const [password,setPassword] = useState('');
    const [showPassword,setShowPassword] = useState(true);
    const [loading,setLoading] = useState(false);

    const go = (screenName:string)=>{
        navigation.navigate(screenName);
    }

    async function getLogin(){
        try{
            setLoading(true);

            if(!usuario || !password){
                return Alert.alert('Atenção','Informe os campos obrigatórios')
            }

            navigation.reset({routes:[{name:"BottomRoutes"}]});

        } catch(error){
            console.log('erro');
        } finally{
            setLoading(false);
        }
    }

    return (
        <View style={style.container}>
            <View style={style.boxTop}>
                <Image
                    source={Logo}
                    style={style.logo}
                    resizeMode="contain"
                />
                <Text style={style.text}>Bem vindo!</Text>
            </View>
            <View style={style.boxMid}>
                <Input
                    value={usuario}
                    onChangeText={setUsuario}
                    title="Usuário"
                    IconRigth={FontAwesome}
                    iconRightName="user-o"
                />
                <Input
                    value={password}
                    onChangeText={setPassword}
                    title="Senha"
                    IconRigth={FontAwesome}
                    iconRightName={showPassword?"eye-slash":"eye"}
                    secureTextEntry={showPassword}
                    onIconRigthPress={()=>setShowPassword(!showPassword)}
                />
            </View>
            <View style={style.boxBottom}>
              <Button
              text="ENTRAR"
              loading={loading}
              onPress={()=>getLogin()}
              />
            </View>
            <View style={style.textBottomContainer}>
                <Text style={style.textBottom}>
                    Não possui conta?
                </Text>

                <TouchableOpacity onPress={() => go('SingUpRegisterView')}>
                    <Text style={style.textBottomCreate}>
                        Crie agora!
                    </Text>
                </TouchableOpacity>
            </View>
        </View>
    )
}
