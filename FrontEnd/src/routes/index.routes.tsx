import React from "react";
import {createStackNavigator} from "@react-navigation/stack";
import Login from "../pages/login";
import BottomRoutes from "./bottom.routes";
import SingUpRegisterView from "../pages/register/SingUpRegister";
import VehicleRegister from "../pages/register/vehicleRegister";
import ClientRegister from "../pages/register/clientRegister";
import OrderRegister from "../pages/register/orderRegister";
import OrderDetails from "../pages/orderDetails";

export default function Routes() {
    const Stack = createStackNavigator();

    return (
        <Stack.Navigator
            initialRouteName="Login"
            screenOptions={{
                headerShown: false,
                cardStyle:{
                    backgroundColor: "#FFF"
                }
            }}
        >
            <Stack.Screen
                name="Login"
                component={Login}
            />

            <Stack.Screen
                name="BottomRoutes"
                component={BottomRoutes}
            />

            <Stack.Screen
                name="SingUpRegisterView"
                component={SingUpRegisterView}
            />

            <Stack.Screen
                name="VehicleRegister"
                component={VehicleRegister}
            />

            <Stack.Screen
                name="ClientRegister"
                component={ClientRegister}
            />

            <Stack.Screen
                name="OrderRegister"
                component={OrderRegister}
            />

            <Stack.Screen
                name="OrderDetails"
                component={OrderDetails}
            />

        </Stack.Navigator>
    )
}