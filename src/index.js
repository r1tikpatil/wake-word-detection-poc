import { NativeModules, NativeEventEmitter, Platform } from 'react-native';
import Tts from 'react-native-tts';

const { GarvisModule, ServiceStarter } = NativeModules;
const garvisEvents = new NativeEventEmitter(GarvisModule);

export function initGarvisListeners() {
  garvisEvents.addListener('GarvisSpeech', text => {
    console.log('User said:', text);
    Tts.speak(`You said: ${text}`);
  });

  const wakeEvt = new NativeEventEmitter(ServiceStarter);
  wakeEvt.addListener('GarvisWake', () => {
    console.log('Hey Garvis detected!');
    GarvisModule.startSpeech();
  });
}

export function startGarvisService() {
  if (Platform.OS === 'android') {
    ServiceStarter.startGarvisService();
  }
}
