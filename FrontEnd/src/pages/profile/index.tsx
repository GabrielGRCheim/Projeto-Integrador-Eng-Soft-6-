import React from "react";

import {Text, View} from 'react-native';

// Esta tela depende da fase de autenticação ainda não construída no back-end
// (não há endpoint de login, nem conceito de "usuário logado" no front hoje —
// ver comentário em src/pages/login/index.tsx). Quando essa fase existir,
// aqui é onde os dados do usuário autenticado (nome, email, perfil) devem
// ser exibidos, usando GET /api/usuarios/{id} com o id da sessão.
export default function Profile() {
    return (
        <View style={{flex:1,justifyContent:'center',alignItems:'center', padding: 20}}>
            <Text style={{fontSize: 16, fontWeight: 'bold', marginBottom: 8}}>
                Perfil
            </Text>
            <Text style={{textAlign: 'center', color: '#6B7280'}}>
                Esta tela vai mostrar os dados do usuário logado assim que o
                back-end tiver um sistema de autenticação.
            </Text>
        </View>
    )
}
