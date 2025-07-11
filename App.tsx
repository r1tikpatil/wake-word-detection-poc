import React, { useEffect } from 'react';
import { View, Text, Button, PermissionsAndroid, Platform } from 'react-native';
// import { startGarvisService, initGarvisListeners } from './src/index';

export default function App() {
  // useEffect(() => {
  //   if (Platform.OS === 'android') {
  //     PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.RECORD_AUDIO);
  //   }
  //   initGarvisListeners();
  // }, []);

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text style={{ fontSize: 18, marginBottom: 20 }}>
        Hey Garvis Assistant
      </Text>
      {/* <Button title="Start Garvis Service" onPress={startGarvisService} /> */}
    </View>
  );
}
