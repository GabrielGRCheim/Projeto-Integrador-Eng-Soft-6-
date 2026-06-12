import {StyleSheet} from 'react-native';
import {themas} from "../global/themes";

export const styles = StyleSheet.create({
    container:{
        flex: 1,
        justifyContent: 'center',
        marginTop: 10,
        padding: 12,
        alignItems: 'center',
        gap: 12,
    },
    title: {
     fontSize: 36,
        fontWeight: '600',
 },
    button: {
        width: '100%',
        padding: 16,
        borderRadius: 20,
        backgroundColor: themas.colors.lightGray,
        borderColor: themas.colors.primary,
    }
})