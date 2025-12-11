import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Home from './src/screens/Home';
import Settings from './src/screens/Settings';
import { useFonts } from 'expo-font';

const Stack = createNativeStackNavigator();

export default function App() {
  const [loaded] = useFonts({
    Ionicons: require('@expo/vector-icons/build/vendor/react-native-vector-icons/Fonts/Ionicons.ttf'),
  });
  if (!loaded) return null;
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={Home} options={{ headerShown: false }} />
        <Stack.Screen name="Settings" component={Settings} options={{ title: 'Configurações' }} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
