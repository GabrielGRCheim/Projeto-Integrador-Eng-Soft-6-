import {StyleSheet} from "react-native";
import {themas} from "../../../global/themes";

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        gap:10
    },
    boxTop:{
        width: '100%',
        alignItems: 'center',
        justifyContent: 'center',
    },
    boxBottom: {
        width: '100%',
        height: 200,
        alignItems: 'center',
        marginTop: 20,
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
    },

})