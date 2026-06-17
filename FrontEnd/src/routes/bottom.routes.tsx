import Dashboard from "../pages/dashboard";
import {createBottomTabNavigator} from "@react-navigation/bottom-tabs";
import CustomTabBar from "../components/CustomTabBar";
import Orders from "../pages/ordersList";
import Stock from "../pages/stock";
import Profile from "../pages/profile";
import {QuickActionsProvider} from "../context/quickActionsContext";

const Tab = createBottomTabNavigator();

export default function BottomRoutes() {
    return (
        <QuickActionsProvider>
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
        </QuickActionsProvider>
    );
}