import Dashboard from "../pages/dashboard";
import {createBottomTabNavigator} from "@react-navigation/bottom-tabs";
import CustomTabBar from "../components/CustomTabBar";
import Orders from "../pages/orders";
import Stock from "../pages/stock";
import Profile from "../pages/profile";
import {AuthProviderList} from "../context/authContext_list";

const Tab = createBottomTabNavigator();

export default function BottomRoutes() {
    return (
        <AuthProviderList>
        <Tab.Navigator
            screenOptions={{
                headerShown: false,
            }}
            tabBar={props => <CustomTabBar{...props}/>}
        >
            <Tab.Screen
                name="Dashboard"
                component={Dashboard}
            />
            <Tab.Screen
                name="Orders"
                component={Orders}
            />
            <Tab.Screen
                name="Stock"
                component={Stock}
            />
            <Tab.Screen
                name="Profile"
                component={Profile}
            />
        </Tab.Navigator>
        </AuthProviderList>
    );
}