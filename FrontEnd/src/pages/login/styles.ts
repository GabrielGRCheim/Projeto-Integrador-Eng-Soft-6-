import {Dimensions, StyleSheet} from 'react-native';
import {themas} from "../../global/themes";



export const style = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    boxTop: {
        height: Dimensions.get("window").height/3,
        width: '100%',
        alignItems: 'center',
        justifyContent: 'center',
    },
    boxMid:{
        height: Dimensions.get("window").height/4,
        width: '100%',
        paddingHorizontal: 37,
    },
    boxBottom: {
        height: Dimensions.get("window").height/3,
        width: '100%',
        alignItems: 'center',
    },
    logo: {
        width: 120,
        height: 120,
    },
    text: {
        fontWeight: 'bold',
        marginTop: 40,
        fontSize: 18
    },
    textBottom: {
        fontSize: 16,
        color:themas.colors.gray,
    },
    textBottomCreate: {
        fontSize: 16,
        color:themas.colors.primary,
    }
})