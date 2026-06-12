import React, {forwardRef, LegacyRef} from "react";
import {
    View, Text, TextInput, TextInputProps, TextStyle, StyleProp, TouchableOpacity,
} from "react-native";


import {FontAwesome} from '@expo/vector-icons';
import {themas} from "../../global/themes";
import {styles} from "./styles";

type IconComponent = React.ComponentType<React.ComponentProps<typeof FontAwesome>>


type Props = TextInputProps & {
    IconLeft?: IconComponent,
    IconRigth?: IconComponent,
    iconLeftName?: string,
    iconRightName?: string,
    title?: string,
    onIconLeftPress?: () => void,
    onIconRigthPress?: () => void,
    height?:number,
    labelStyle?:StyleProp<TextStyle>
}

export const Input = forwardRef((props: Props, ref: LegacyRef<TextInput> | null) =>{

   const {IconLeft, IconRigth, iconLeftName, iconRightName, title, onIconLeftPress, onIconRigthPress, height,labelStyle,...rest } = props;

    const calculateSizeWidth = () => {
        if (IconLeft && IconRigth) {
            return '80%';
        } else if (IconLeft || IconRigth) {
            return '90%';
        } else {
            return '100%';
        }
    };

    const calculateSizePaddingLeft = () => {
        if (IconLeft && IconRigth) {
            return 0;
        } else if (IconLeft || IconRigth) {
            return 10;
        } else {
            return 20;
        }
    };

    return (
        <>
        {title&&<Text style={styles.titleinput}>{title}</Text>}
            <View style ={[styles.boxInput,{paddingLeft:calculateSizePaddingLeft()}]}>
                {IconLeft && iconLeftName &&(
                    <TouchableOpacity onPress={onIconLeftPress} style={styles.button}>
                        <IconLeft name={iconLeftName as any} size = {20} color={themas.colors.gray} style= {styles.Icon} />
                    </TouchableOpacity>
                )}
            <TextInput
                style ={[
                    styles.input,
                    {width:calculateSizeWidth()}
                ]}
                {...rest}
            />
                {IconRigth && iconRightName &&(
                    <TouchableOpacity onPress={onIconRigthPress} style={styles.button}>
                        <IconRigth name={iconRightName as any} size = {20} color={themas.colors.gray} style= {styles.Icon} />
                    </TouchableOpacity>
                )}
            </View>
        </>
    );
})