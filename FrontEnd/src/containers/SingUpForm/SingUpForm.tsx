import { useForm, Controller } from 'react-hook-form';
import {singUpSchema, SingUpSchema} from "./SingUpSchema";
import {zodResolver} from "@hookform/resolvers/zod";

type SingUpFormProps = {
    onSubmit: () => void;
}

export function SingUpForm({onSubmit}: {onSubmit: SingUpFormProps}): void {

    const {control, handleSubmit} = useForm<SingUpSchema>({
        resolver: zodResolver(singUpSchema),
    });

    return
}