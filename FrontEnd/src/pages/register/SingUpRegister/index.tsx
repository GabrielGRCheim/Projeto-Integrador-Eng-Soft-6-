import React, {} from "react";
import {View,} from "react-native";
import {Controller, useForm} from "react-hook-form";
import {Input} from "../../../components/Input";

type FormData = {
    fullname: string;
};


export default function SingUpRegisterView() {

    const { control, handleSubmit } = useForm<FormData>({
        defaultValues: {
            fullname: ""
        }
    });

    const onSubmit = (data: FormData) => {
        console.log(data);
    };

    return (
        <View>
            <Controller
                control={control}
                name="fullname"
                render={({ field: { onChange, value } }) => (
                    <Input
                        title="Nome Completo"
                        value={value}
                        onChangeText={onChange}
                    />
                )}
            />
        </View>
    )
}