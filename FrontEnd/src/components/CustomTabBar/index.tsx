import React from 'react';
import {TouchableOpacity, View} from "react-native";
import { BottomTabBarProps } from '@react-navigation/bottom-tabs';
import {styles} from "./styles";
import {FontAwesome} from "@expo/vector-icons";
import {useQuickActions} from "../../context/quickActionsContext";

export default function CustomTabBar({state, navigation,}: BottomTabBarProps) {

    const {onOpen} = useQuickActions();

    const go = (screenName:string)=>{
        navigation.navigate(screenName);
    }

    return (
        <View style={styles.tabArea}>
            <TouchableOpacity style={styles.tabItem} onPress={()=>go('Dashboard')}>
                <FontAwesome
                    name="tasks"
                    style={{fontSize:32}}
                />
            </TouchableOpacity>
            <TouchableOpacity style={styles.tabItem} onPress={()=>go('Orders')}>
              <FontAwesome
                  name="file-text-o"
                  style={{fontSize:32}}
              />
            </TouchableOpacity>
            <TouchableOpacity style={styles.tabItem} onPress={()=>onOpen()}>
                <FontAwesome
                    name="plus"
                    style={{fontSize:40}}
                />
            </TouchableOpacity>
            <TouchableOpacity style={styles.tabItem} onPress={()=>go('Stock')}>
                <FontAwesome
                    name="archive"
                    style={{fontSize:32}}
                />
            </TouchableOpacity>
            <TouchableOpacity style={styles.tabItem} onPress={()=>go('Profile')}>
                <FontAwesome
                    name="user"
                    style={{fontSize:32}}
                />
            </TouchableOpacity>
        </View>
    );
};