import React, {useState} from "react";
import {
    Image,
    Text,
    View,
    Alert,
} from 'react-native';
import {style} from "./styles";
import Logo from "../../assets/imagens/logo.png";
import {FontAwesome} from '@expo/vector-icons';
import {Input} from "../../components/Input";
import {Button} from "../../components/Button";

export default function Login() {
    const [usuario,setUsuario] = useState('');
    const [password,setPassword] = useState('');
    const [showPassword,setShowPassword] = useState(true);
    const [loading,setLoading] = useState(false);

    async function getLogin(){
        try{
            setLoading(true);
            if(!usuario){
                return Alert.alert('Atenção','Informe os campos obrigatórios')
            }

            setTimeout(()=>{
                Alert.alert('Logado com sucesso!')
            }, 3000)


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
            <Text style={style.textBottom}>Não possui conta? <Text style={style.textBottomCreate}>Crie agora!</Text> </Text>
        </View>
    )
}