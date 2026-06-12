import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import Routes from "./src/routes/index.routes";
import {NavigationContainer} from "@react-navigation/native";

import Login from "./src/pages/login";

export default function App() {
  return (
      <NavigationContainer>
        <Routes />
        </NavigationContainer>
      );
}
